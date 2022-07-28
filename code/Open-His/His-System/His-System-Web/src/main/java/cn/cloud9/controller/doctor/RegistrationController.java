package cn.cloud9.controller.doctor;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.Patient;
import cn.cloud9.domain.Registration;
import cn.cloud9.domain.RegistrationForm;
import cn.cloud9.domain.SystemDepartment;
import cn.cloud9.service.PatientService;
import cn.cloud9.service.RegistrationService;
import cn.cloud9.service.SchedulingService;
import cn.cloud9.service.SystemDepartmentService;
import cn.cloud9.util.ShiroSecurityUtil;
import cn.cloud9.utils.IdGeneratorSnowflakeUtil;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.date.DateUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月28日 下午 09:52
 */
@Log4j2
@RestController
@RequestMapping("doctor/registration")
public class RegistrationController {

    @Reference
    private RegistrationService registrationService;

    @Reference
    private SchedulingService schedulingService;

    @Reference
    private PatientService patientService;

    @Resource
    private SystemDepartmentService departmentService;

    /**
     * 分页查询出有排班的部门列表
     */
    @GetMapping("listDeptForScheduling")
    public AjaxResult listDeptForScheduling(Registration registrationQueryDto) {
        Long deptId = registrationQueryDto.getDeptId();
        String schedulingDay = registrationQueryDto.getSchedulingDay();
        String schedulingType = registrationQueryDto.getSchedulingType();
        String subsectionType = registrationQueryDto.getSubsectionType();
        List<Long> deptIds = this.schedulingService.queryHasSchedulingDeptIds(deptId, schedulingDay, schedulingType, subsectionType);
        if (deptIds == null || deptIds.size() == 0) {
            return AjaxResult.success(Collections.EMPTY_LIST);
        } else {
            List<SystemDepartment> list = this.departmentService.listDeptByDeptIds(deptIds);
            return AjaxResult.success(list);
        }
    }

    /**
     * 根据身份证号查询患者信息
     */
    @GetMapping("getPatientByIdCard/{idCard}")
    public AjaxResult getPatientByIdCard(@PathVariable String idCard) {
        Patient patient = this.patientService.getPatientByIdCard(idCard);
        if (null == patient) {
            return AjaxResult.fail(idCard + "对应的患者不存在，请在下面新建患者信息");
        } else {
            return AjaxResult.success(patient);
        }
    }

    /**
     * 添加挂号信息
     * 如果患者ID为空，则先添加患者信息
     */
    @PostMapping("addRegistration")
    public AjaxResult addRegistration(@RequestBody @Validated RegistrationForm registrationFormDto) {
        Patient patientDto = registrationFormDto.getPatientDto();
        Registration registrationDto = registrationFormDto.getRegistrationDto();
        Patient patient = null;
        //保存患者信息
        if (StringUtils.isBlank(patientDto.getPatientId())) {
            patientDto.setPatientId(IdGeneratorSnowflakeUtil.generatorIdWithProfix("HZ"));
            patientDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
            patient = patientService.addPatient(patientDto);
        } else {
            patient = this.patientService.getPatientById(patientDto.getPatientId());
        }
        if (null == patient) {
            return AjaxResult.fail("当前患者ID不存在，请确认后再提交");
        }
        //修改部门挂号
        SystemDepartment dept = departmentService.getOne(registrationDto.getDeptId());
        //保存挂号信息
        registrationDto.setRegId(IdGeneratorSnowflakeUtil.generatorIdWithProfix("GH"));
        registrationDto.setPatientId(patient.getPatientId());
        registrationDto.setPatientName(patient.getName());
        registrationDto.setRegNumber(dept.getRegNumber() + 1);
        registrationDto.setVisitDate(registrationDto.getVisitDate().substring(0, 10));
        registrationDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        this.registrationService.addRegistration(registrationDto);
        //修改部门挂号信息编号
        dept.setRegNumber(dept.getRegNumber() + 1);
        this.departmentService.updateDeptRegNumber(dept.getDeptId(), dept.getRegNumber());
        //返回挂号编号
        return AjaxResult.success("", registrationDto.getRegId());
    }


    //分页加载挂号列表【默认当天的】
    @HystrixCommand
    @GetMapping("queryRegistrationForPage")
    public AjaxResult queryRegistrationForPage(Registration registrationDto) {
        if (registrationDto.getBeginTime() == null || registrationDto.getEndTime() == null) {
            registrationDto.setVisitDate(DateUtil.format(DateUtil.date(), "yyyy-MM-dd"));
        }
        DataGridViewVO gridView = this.registrationService.queryRegistrationForPage(registrationDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }

    /**
     * 收费
     */
    @PostMapping("collectFee/{registrationId}")
    public AjaxResult collectFee(@PathVariable String registrationId) {
        Registration registration = this.registrationService.queryRegistrationByRegId(registrationId);
        if (null == registration) {
            return AjaxResult.fail("当前挂号单ID对应的挂号单不存在，请核对后再查询");
        }
        if (!registration.getRegStatus().equals(ApiConstant.REG_STATUS_0)) {
            return AjaxResult.fail("当前挂号单状态不是未收费状态，不能收费");
        }
        registration.setRegStatus(ApiConstant.REG_STATUS_1);
        return AjaxResult.toAjax(this.registrationService.updateRegistrationById(registration));

    }
    
    /**
     * 作废
     */
    @PostMapping("doInvalid/{regId}")
    @HystrixCommand
    public AjaxResult doInvalid(@PathVariable String regId){
        Registration registration=this.registrationService.queryRegistrationByRegId(regId);
        if(null==registration){
            return AjaxResult.fail("当前挂号单【"+regId+"】对应的挂号单不存在，请核对后再查询");
        }
        //如果挂号单的状态不是未收费
        if(!registration.getRegStatus().equals(ApiConstant.REG_STATUS_0)){
            return AjaxResult.fail("当前挂号单【"+regId+"】的状态不是未收费状态，不能作废");
        }
        //收费，更新挂号单的状态
        registration.setRegStatus(ApiConstant.REG_STATUS_5);
        return AjaxResult.toAjax(this.registrationService.updateRegistrationByRegId(registration));
    }

    /**
     * 退号
     */
    @PostMapping("doReturn/{regId}")
    @HystrixCommand
    public AjaxResult doReturn(@PathVariable String regId){
        Registration registration=this.registrationService.queryRegistrationByRegId(regId);
        if(null==registration){
            return AjaxResult.fail("当前挂号单【"+regId+"】对应的挂号单不存在，请核对后再查询");
        }
        //如果挂号单的状态不是未收费
        if(!registration.getRegStatus().equals(Constants.REG_STATUS_1)){
            return AjaxResult.fail("当前挂号单【"+regId+"】的状态不是待就诊状态，不能退号");
        }
        //收费，更新挂号单的状态
        registration.setRegStatus(Constants.REG_STATUS_4);
        return AjaxResult.toAjax(this.registrationService.updateRegistrationByRegId(registration));
    }
}
