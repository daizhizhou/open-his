package cn.cloud9.service;

import cn.cloud9.domain.SimpleUser;
import cn.cloud9.domain.SystemMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SystemMenuService extends IService<SystemMenu> {
    /**
     * 查询菜单信息
     * 如查用户是超级管理员，那么查询所以菜单和权限
     * 如果用户是普通用户，那么根据用户ID关联角色和权限
     * @param isAdmin 是否是超级管理员
     * @param  simpleUser  如果isAdmin=true  simpleUser可以为空
     */
    List<SystemMenu> selectMenuTree(boolean isAdmin, SimpleUser simpleUser);

}
