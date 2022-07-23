package cn.cloud9.service;

import cn.cloud9.domain.SystemUser;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SystemUserService extends IService<SystemUser>{
    /**
     * 根据手机号查询用户
     * @param phone 手机号
     * @return
     */
    SystemUser queryUserByPhone(String phone);

    /**
     * 根据用户ID查询用户
     * @param userId 用户编号
     * @return
     */
    SystemUser getOne(Long userId);

}
