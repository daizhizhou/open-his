package cn.cloud9.service;

import cn.cloud9.domain.SystemRole;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SystemRoleService extends IService<SystemRole>{

    /**
     * 分页查询角色
     * @param roleDto
     * @return
     */
    DataGridViewVO listRolePage(SystemRole roleDto);

    /**
     * 查询所有可用角色
     * @return
     */
    List<SystemRole> listAllRoles();

    /**
     * 根据ID查询角色
     * @param roleId
     * @return
     */
    SystemRole getOne(Long roleId);

    /**
     * 添加一个角色
     * @param roleDto
     * @return
     */
    int addRole(SystemRole roleDto);

    /**
     * 修改角色
     * @param roleDto
     * @return
     */
    int updateRole(SystemRole roleDto);

    /**
     * 根据角色ID删除角色
     * @param roleIds
     * @return
     */
    int deleteRoleByIds(Long[] roleIds);

}
