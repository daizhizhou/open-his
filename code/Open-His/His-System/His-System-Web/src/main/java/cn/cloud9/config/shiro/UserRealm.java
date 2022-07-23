package cn.cloud9.config.shiro;

import cn.cloud9.domain.SystemUser;
import cn.cloud9.service.SystemUserService;
import cn.cloud9.utils.CheckUtil;
import cn.cloud9.vo.ActiveUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月23日 下午 05:18
 */
public class UserRealm extends AuthorizingRealm {

    @Resource
    SystemUserService systemUserService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 身份得到的就是上一个方法的返回值的值 第一个参数 activeUser
        ActiveUser activeUser = (ActiveUser) principalCollection.getPrimaryPrincipal();
        return new SimpleAuthorizationInfo();
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        final String phone = authenticationToken.getPrincipal().toString();
        final SystemUser systemUser = systemUserService.queryUserByPhone(phone);

        // 1、找不到这个用户直接返回null
        if (CheckUtil.isEmpty(systemUser)) return null;
        // 2、判断密码
        return new SimpleAuthenticationInfo(
            new ActiveUser().setSystemUser(systemUser),
            systemUser.getPassword(),
            ByteSource.Util.bytes(systemUser.getSalt()),
            this.getName()
        );
    }
}
