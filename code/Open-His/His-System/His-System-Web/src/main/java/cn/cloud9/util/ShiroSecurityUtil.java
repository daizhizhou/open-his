package cn.cloud9.util;


import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.SimpleUser;
import cn.cloud9.domain.SystemUser;
import cn.cloud9.vo.ActiveUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.List;
/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月23日 下午 10:34
 */
public class ShiroSecurityUtil {

    /***
     * @Description: 得到当前登陆的用户对象的ActiveUser
     */
    public static ActiveUser getCurrentActiveUser(){
        Subject subject= SecurityUtils.getSubject();
        return (ActiveUser) subject.getPrincipal();
    }

    /***
     * @Description: 得到当前登陆的用户对象User
     */
    public static SystemUser getCurrentUser(){
        return getCurrentActiveUser().getSystemUser();
    }

    /***
     * @Description: 得到当前登陆的用户对象SimpleUser
     */
    public static SimpleUser getCurrentSimpleUser(){
        SystemUser user = getCurrentActiveUser().getSystemUser();
        return new SimpleUser(user.getUserId(),user.getUserName());
    }

    /***
     * @Description: 得到当前登陆的用户名称
     */
    public static String getCurrentUserName(){
        return getCurrentActiveUser().getSystemUser().getUserName();
    }

    /***
     * @Description: 得到当前登陆对象的角色编码
     */
    public static List<String> getCurrentUserRoles(){
        return getCurrentActiveUser().getRoles();
    }


    /***
     * @Description: 得到当前登陆对象的权限编码
     */
    public static List<String> getCurrentUserPermissions(){
        return getCurrentActiveUser().getPermissions();
    }

    /***
     * @Description: 判断当前用户是否是超管
     */
    public static boolean isAdmin(){
        return getCurrentUser().getUserType().equals(ApiConstant.USER_ADMIN);
    }
}
