package cn.cloud9.mapper;

import cn.cloud9.domain.SystemMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

public interface SystemMenuMapper extends BaseMapper<SystemMenu> {
    /**
     * 根据菜单ID查询它的子节点个数
     *
     * @param menuId
     * @return
     */
    @Select("SELECT count(1) FROM `sys_menu` WHERE `parent_id` = #{menuId}")
    Long queryChildCountByMenuId(Long menuId);

}