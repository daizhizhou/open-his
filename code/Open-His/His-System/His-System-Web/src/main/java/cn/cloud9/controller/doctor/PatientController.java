package cn.cloud9.controller.doctor;

import cn.cloud9.domain.Patient;
import cn.cloud9.domain.PatientFile;
import cn.cloud9.service.PatientService;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月28日 下午 09:33
 */
@RestController
@RequestMapping("doctor/patient")
public class PatientController {
    @Reference
    private PatientService patientService;

    /**
     * 分页查询
     */
    @GetMapping("listPatientForPage")
    @HystrixCommand
    public AjaxResult listPatientForPage(Patient patientDto) {
        DataGridViewVO gridView = this.patientService.listPatientForPage(patientDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }

    /**
     * 查询一个
     */
    @GetMapping("getPatientById/{patientId}")
    @HystrixCommand
    public AjaxResult getPatientById(@PathVariable String patientId) {
        Patient patient = this.patientService.getPatientById(patientId);
        return AjaxResult.success(patient);
    }

    /**
     * 根据ID查询患者档案
     */
    @GetMapping("getPatientFileById/{patientId}")
    @HystrixCommand
    public AjaxResult getPatientFileById(@PathVariable String patientId) {
        PatientFile patientFile = this.patientService.getPatientFileById(patientId);
        return AjaxResult.success(patientFile);
    }

    /**
     * 根据患者ID查询患者信息 患者档案信息  历史病例
     */
    @GetMapping("getPatientAllMessageByPatientId/{patientId}")
    public AjaxResult getPatientAllMessageByPatientId(@PathVariable String patientId) {
        return AjaxResult.success();
    }
}
