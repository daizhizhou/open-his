package cn.cloud9.controller.system;

import cn.cloud9.aspect.annotation.SystemLog;
import cn.cloud9.aspect.enums.BusinessType;
import cn.cloud9.domain.SystemUser;
import cn.cloud9.service.SystemUserService;
import cn.cloud9.util.ShiroSecurityUtil;
import cn.cloud9.utils.CheckUtil;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月24日 下午 05:45
 */
@RestController
@RequestMapping("system/user")
public class SystemUserController {
    @Resource
    private SystemUserService systemUserService;

    /**
     * 分页查询
     */
    @GetMapping("listUserForPage")
    public AjaxResult listUserForPage(SystemUser userDto){
        DataGridViewVO gridView = this.systemUserService.listUserForPage(userDto);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }
    /**
     * 添加
     */
    @PostMapping("addUser")
    @SystemLog(title = "添加用户",businessType = BusinessType.INSERT)
    public AjaxResult addUser(@Validated SystemUser userDto) {
        userDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemUserService.addUser(userDto));
    }

    /**
     * 修改
     */
    @PutMapping("updateUser")
    @SystemLog(title = "修改用户",businessType = BusinessType.UPDATE)
    public AjaxResult updateUser(@Validated SystemUser userDto) {
        userDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemUserService.updateUser(userDto));
    }


    /**
     * 根据ID查询一个用户信息
     */
    @GetMapping("getUserById/{userId}")
    public AjaxResult getUserById(@PathVariable @Validated @NotNull(message = "用户ID不能为空") Long userId) {
        return AjaxResult.success(this.systemUserService.getOne(userId));
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteUserByIds/{userIds}")
    @SystemLog(title = "删除用户",businessType = BusinessType.DELETE)
    public AjaxResult deleteUserByIds(@PathVariable @Validated @NotEmpty(message = "要删除的ID不能为空") Long[] userIds) {
        return AjaxResult.toAjax(this.systemUserService.deleteUserByIds(userIds));
    }

    /**
     * 查询所有可用的用户
     */
    @GetMapping("selectAllUser")
    public AjaxResult selectAllUser(){
        return AjaxResult.success(this.systemUserService.getAllUsers());
    }

    /**
     * 重置密码
     */
    @PostMapping("resetPwd/{userIds}")
    public AjaxResult resetPwd(@PathVariable Long[] userIds){
        if(!CheckUtil.isEmptyArray(userIds) && userIds.length > 0){
            this.systemUserService.resetPassWord(userIds);
            return AjaxResult.success("重置成功");
        }
        return AjaxResult.fail("重置失败,没有选择用户");
    }

}
