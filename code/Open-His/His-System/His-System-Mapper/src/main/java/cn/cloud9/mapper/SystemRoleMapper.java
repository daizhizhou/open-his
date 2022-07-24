package cn.cloud9.mapper;

import cn.cloud9.domain.SystemRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

public interface SystemRoleMapper extends BaseMapper<SystemRole> {
    /**
     * 根据角色IDS删除sys_role_menu中间表的数据
     *
     * @param ids
     */
    void deleteRoleMenuByRoleIds(@Param("ids") List<Long> ids);

    /**
     * 根据角色IDS删除sys_role_user中间表的数据
     *
     * @param ids
     */
    void deleteRoleUserByRoleIds(@Param("ids") List<Long> ids);

    /**
     * 保存角色和菜单之关的关系
     * @param roleId
     * @param menuId
     */
    void saveRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);
}