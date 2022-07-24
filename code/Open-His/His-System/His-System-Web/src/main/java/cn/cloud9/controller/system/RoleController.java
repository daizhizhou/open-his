package cn.cloud9.controller.system;

import cn.cloud9.domain.SystemRole;
import cn.cloud9.service.SystemRoleService;
import cn.cloud9.util.ShiroSecurityUtil;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月24日 下午 03:36
 */
@RestController
@RequestMapping("system/role")
public class RoleController {
    @Resource
    private SystemRoleService systemRoleService;

    /**
     * 分页查询
     */
    @GetMapping("listRoleForPage")
    public AjaxResult listRoleForPage(SystemRole roleDto){
        DataGridViewVO gridView = this.systemRoleService.listRolePage(roleDto);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }

    /**
     * 不分页面查询有效的
     */
    @GetMapping("selectAllRoles")
    public AjaxResult selectAllRoles(){
        List<SystemRole> lists = this.systemRoleService.listAllRoles();
        return AjaxResult.success(lists);
    }

    /**
     * 查询一个
     */
    @GetMapping("getRoleById/{roleId}")
    public AjaxResult getRoleById(@PathVariable Long roleId){
        SystemRole role = this.systemRoleService.getOne(roleId);
        return AjaxResult.success(role);
    }

    /**
     * 添加
     */
    @PostMapping("addRole")
    public AjaxResult addRole(@Validated SystemRole roleDto){
        roleDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemRoleService.addRole(roleDto));
    }

    /**
     * 修改
     */
    @PutMapping("updateRole")
    public AjaxResult updateRole(@Validated SystemRole roleDto){
        roleDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemRoleService.updateRole(roleDto));
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteRoleByIds/{roleIds}")
    public AjaxResult deleteRoleByIds(@PathVariable Long[] roleIds){
        return AjaxResult.toAjax(this.systemRoleService.deleteRoleByIds(roleIds));
    }
}
