package cn.cloud9.controller.system;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.contants.HttpStatus;
import cn.cloud9.domain.SimpleUser;
import cn.cloud9.domain.SystemMenu;
import cn.cloud9.service.SystemMenuService;
import cn.cloud9.vo.ActiveUser;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.LoginBodyDTO;
import cn.cloud9.vo.MenuTreeVO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月23日 下午 05:39
 */
@Log4j2
@RestController
@RequestMapping("/login")
public class LoginController {

    @Resource
    private SystemMenuService systemMenuService;

    /**
     * 登录方法
     *
     * @return 结果
     */
    @PostMapping("/doLogin")
    public AjaxResult login(@RequestBody LoginBodyDTO loginBodyDto, HttpServletRequest request) {
        AjaxResult ajax = AjaxResult.success();
        String username = loginBodyDto.getUsername();
        String password = loginBodyDto.getPassword();
        //构造用户名和密码的token
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            //得到会话的token==也就是redis里面存的
            Serializable webToken = subject.getSession().getId();
            ajax.put(ApiConstant.TOKEN, webToken);
        } catch (Exception e) {
            log.error("用户名或密码不正确", e);
            ajax = AjaxResult.error(HttpStatus.ERROR, "用户名或密码不正确");
        }
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        // 获取当前登录用户
        Subject subject = SecurityUtils.getSubject();
        ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
        AjaxResult ajax = AjaxResult.success();
        ajax.put("username", activeUser.getSystemUser().getUserName());
        ajax.put("picture", activeUser.getSystemUser().getPicture());
        ajax.put("roles", activeUser.getRoles());
        ajax.put("permissions", activeUser.getPermissions());
        return ajax;
    }

    /**
     * 用户退出
     */
    @GetMapping("/logout")
    public AjaxResult logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return AjaxResult.success("用户退出成功");
    }

    /**
     * 获取应该显示的菜单信息
     *
     * @return 菜单信息
     */
    @GetMapping("/getMenus")
    public AjaxResult getSystemMenus() {
        Subject subject = SecurityUtils.getSubject();
        ActiveUser activeUser = (ActiveUser) subject.getPrincipal();
        boolean isAdmin = activeUser.getSystemUser().getUserType().equals(ApiConstant.USER_ADMIN);
        SimpleUser simpleUser = null;
        if (!isAdmin) simpleUser = new SimpleUser(activeUser.getSystemUser().getUserId(), activeUser.getSystemUser().getUserName());
        List<SystemMenu> menus = systemMenuService.selectMenuTree(isAdmin, simpleUser);
        List<MenuTreeVO> menuVos = new ArrayList<>();
        menus.forEach(menu -> menuVos.add(new MenuTreeVO(menu.getMenuId().toString(), menu.getPath())));
        return AjaxResult.success(menuVos);
    }



}
