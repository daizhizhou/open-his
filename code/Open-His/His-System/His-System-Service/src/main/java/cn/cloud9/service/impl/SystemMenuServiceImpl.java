package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.SimpleUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cloud9.mapper.SystemMenuMapper;
import cn.cloud9.domain.SystemMenu;
import cn.cloud9.service.SystemMenuService;
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
}
