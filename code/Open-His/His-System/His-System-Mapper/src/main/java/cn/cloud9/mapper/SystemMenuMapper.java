package cn.cloud9.mapper;

import cn.cloud9.domain.SystemMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SystemMenuMapper extends BaseMapper<SystemMenu> {
    /**
     * 根据菜单ID查询它的子节点个数
     *
     * @param menuId
     * @return
     */
    Long queryChildCountByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据角色ID查询所有选中的权限菜单ID【只查子节点的】
     *
     * @param roleId
     * @return
     */
    List<Long> queryMenuIdsByRoleId(@Param("roleId") Long roleId);
}