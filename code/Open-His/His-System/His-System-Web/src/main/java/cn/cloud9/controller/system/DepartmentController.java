package cn.cloud9.controller.system;

import cn.cloud9.aspect.annotation.SystemLog;
import cn.cloud9.aspect.enums.BusinessType;
import cn.cloud9.domain.SystemDepartment;
import cn.cloud9.service.SystemDepartmentService;
import cn.cloud9.util.ShiroSecurityUtil;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月24日 下午 02:58
 */
@RestController
@RequestMapping("system/dept")
public class DepartmentController {

    @Resource
    private SystemDepartmentService systemDepartmentService;

    /**
     * 分页查询
     */
    @GetMapping("listDeptForPage")
    public AjaxResult listDeptForPage(SystemDepartment deptDto) {
        DataGridViewVO gridView = this.systemDepartmentService.listPage(deptDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }

    /**
     * 不分页面查询有效的
     */
    @GetMapping("selectAllDept")
    public AjaxResult selectAllDept() {
        List<SystemDepartment> lists = this.systemDepartmentService.list();
        return AjaxResult.success(lists);
    }

    /**
     * 查询一个
     */
    @GetMapping("getDeptById/{deptId}")
    public AjaxResult getDeptById(@PathVariable @Validated @NotEmpty(message = "科室ID为空") Long deptId) {
        SystemDepartment dept = this.systemDepartmentService.getOne(deptId);
        return AjaxResult.success(dept);
    }

    /**
     * 添加
     */
    @PostMapping("addDept")
    @SystemLog(title = "科室管理", businessType = BusinessType.INSERT)
    public AjaxResult addDept(@Validated SystemDepartment deptDto) {
        deptDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemDepartmentService.addDept(deptDto));
    }

    /**
     * 修改
     */
    @SystemLog(title = "科室管理", businessType = BusinessType.UPDATE)
    @PutMapping("updateDept")
    public AjaxResult updateDept(@Validated SystemDepartment deptDto) {
        deptDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemDepartmentService.updateDept(deptDto));
    }

    /**
     * 删除
     */
    @SystemLog(title = "科室管理", businessType = BusinessType.DELETE)
    @DeleteMapping("deleteDeptByIds/{deptIds}")
    public AjaxResult delete(@PathVariable @Validated @NotEmpty(message = "科室ID为空") Long[] deptIds) {
        return AjaxResult.toAjax(this.systemDepartmentService.deleteDeptByIds(deptIds));
    }


}
