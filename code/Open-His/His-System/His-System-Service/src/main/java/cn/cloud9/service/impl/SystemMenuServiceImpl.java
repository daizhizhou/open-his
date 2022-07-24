package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.SimpleUser;
import cn.cloud9.domain.SystemMenu;
import cn.cloud9.mapper.SystemMenuMapper;
import cn.cloud9.service.SystemMenuService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemMenuServiceImpl extends ServiceImpl<SystemMenuMapper, SystemMenu> implements SystemMenuService{

    @Override
    public List<SystemMenu> selectMenuTree(boolean isAdmin, SimpleUser simpleUser) {
        if (isAdmin) return baseMapper.selectList(
            new LambdaQueryWrapper<SystemMenu>()
            .eq(SystemMenu::getStatus, ApiConstant.STATUS_TRUE) // 有效的菜单
            .in(SystemMenu::getMenuType, ApiConstant.MENU_TYPE_M, ApiConstant.MENU_TYPE_C) // 菜单类型在M+C
            .orderByAsc(SystemMenu::getParentId)
        );
        //根据用户id查询用户拥有的菜单信息，因为RBAC还没开发，暂时返回一样的菜单，即全部菜单
        return baseMapper.selectList(
            new LambdaQueryWrapper<SystemMenu>()
            .eq(SystemMenu::getStatus, ApiConstant.STATUS_TRUE)
            .in(SystemMenu::getMenuType, ApiConstant.MENU_TYPE_M, ApiConstant.MENU_TYPE_C)
            .orderByAsc(SystemMenu::getParentId)
        );
    }

    @Override
    public List<SystemMenu> listAllMenus(SystemMenu menuDto) {
        return this.lambdaQuery()
            .like(StringUtils.isNotBlank(menuDto.getMenuName()), SystemMenu::getMenuName, menuDto.getMenuName())
            .eq(StringUtils.isNotBlank(menuDto.getStatus()), SystemMenu::getStatus, menuDto.getStatus())
            .list();
    }

    @Override
    public SystemMenu getOne(Long menuId) {
        return this.baseMapper.selectById(menuId);
    }

    @Override
    public int addMenu(SystemMenu menuDto) {
        SystemMenu menu = new SystemMenu();
        BeanUtil.copyProperties(menuDto, menu);
        //设置创建时间创建人
        menu.setCreateBy(menuDto.getSimpleUser().getUserName());
        menu.setCreateTime(DateUtil.date());
        return this.baseMapper.insert(menu);
    }

    @Override
    public int updateMenu(SystemMenu menuDto) {
        SystemMenu menu = new SystemMenu();
        BeanUtil.copyProperties(menuDto, menu);
        //设置修改人
        menu.setUpdateBy(menuDto.getSimpleUser().getUserName());
        return this.baseMapper.updateById(menu);
    }

    @Override
    public int deleteMenuById(Long menuId) {
        //先删除role_menu的中间表的数据【后面再加】
        //再删除菜单或权限
        return this.baseMapper.deleteById(menuId);
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        return this.baseMapper.queryChildCountByMenuId(menuId) > 0L;
    }

    @Override
    public List<Long> getMenusIdsByRoleId(Long roleId) {
        return this.baseMapper.queryMenuIdsByRoleId(roleId);
    }
}
