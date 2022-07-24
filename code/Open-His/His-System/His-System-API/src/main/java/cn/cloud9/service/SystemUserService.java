package cn.cloud9.service;

import cn.cloud9.domain.SystemUser;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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


    /**
     * 分页查询用户
     * @param userDto
     * @return
     */
    DataGridViewVO listUserForPage(SystemUser userDto);

    /**
     * 添加用户
     * @param userDto
     * @return
     */
    int addUser(SystemUser userDto);

    /**
     * 修改用户
     * @param userDto
     * @return
     */
    int updateUser(SystemUser userDto);

    /**
     * 删除用户
     * @param userIds
     * @return
     */
    int deleteUserByIds(Long[] userIds);

    /**
     * 查询所有可用的用户
     * @return
     */
    List<SystemUser> getAllUsers();

    /**
     * 重置用户密码
     * @param userIds
     */
    void resetPassWord(Long[] userIds);

}
