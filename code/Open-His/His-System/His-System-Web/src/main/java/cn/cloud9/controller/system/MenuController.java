package cn.cloud9.controller.system;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.SystemMenu;
import cn.cloud9.service.SystemMenuService;
import cn.cloud9.util.ShiroSecurityUtil;
import cn.cloud9.vo.AjaxResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月24日 下午 03:15
 */
@RestController
@RequestMapping("system/menu")
public class MenuController {

    @Resource
    private SystemMenuService systemMenuService;

    /**
     * 查询所有菜单及权限信息
     */
    @GetMapping("listAllMenus")
    public AjaxResult listAllMenus(SystemMenu menuDto){
        List<SystemMenu> list = this.systemMenuService.listAllMenus(menuDto);
        return AjaxResult.success(list);
    }


    /**
     * 查询菜单的下拉树
     */
    @GetMapping("selectMenuTree")
    public AjaxResult selectMenuTree(){
        SystemMenu menuDto = new SystemMenu();
        menuDto.setStatus(ApiConstant.STATUS_TRUE);//只查询可用的
        return AjaxResult.success(this.systemMenuService.listAllMenus(menuDto));
    }

    /**
     * 添加菜单
     */
    @PostMapping("addMenu")
    public AjaxResult addMenu(@Validated SystemMenu menuDto){
        menuDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemMenuService.addMenu(menuDto));
    }

    /**
     * 修改菜单
     */
    @PutMapping("updateMenu")
    public AjaxResult updateMenu(@Validated SystemMenu menuDto){
        menuDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemMenuService.updateMenu(menuDto));
    }

    /**
     * 根据菜单ID查询一个
     */
    @GetMapping("getMenuById/{menuId}")
    public AjaxResult getMenuById(@PathVariable Long menuId){
        SystemMenu menu = this.systemMenuService.getOne(menuId);
        return AjaxResult.success(menu);
    }

    /**
     * 根据菜单ID删除
     */
    @DeleteMapping("deleteMenuById/{menuId}")
    public AjaxResult deleteMenuById(@PathVariable Long menuId){
        //删除之前要判断当前菜单有没有子节点
        return this.systemMenuService.hasChildByMenuId(menuId) ?
            AjaxResult.fail("当前要删除的菜单有子节点，请先删除子节点") :
            AjaxResult.toAjax(this.systemMenuService.deleteMenuById(menuId));
    }
}
