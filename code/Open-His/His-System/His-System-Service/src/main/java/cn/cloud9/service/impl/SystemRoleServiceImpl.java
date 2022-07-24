package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cloud9.mapper.SystemRoleMapper;
import cn.cloud9.domain.SystemRole;
import cn.cloud9.service.SystemRoleService;
@Service
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleMapper, SystemRole> implements SystemRoleService{

    @Override
    public DataGridViewVO listRolePage(SystemRole roleDto) {
        Page<SystemRole> page = new Page<>(roleDto.getPageNum(), roleDto.getPageSize());
        QueryWrapper<SystemRole> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(roleDto.getRoleName()), SystemRole.COL_ROLE_NAME, roleDto.getRoleName());
        qw.like(StringUtils.isNotBlank(roleDto.getRoleCode()), SystemRole.COL_ROLE_CODE, roleDto.getRoleCode());
        qw.eq(StringUtils.isNotBlank(roleDto.getStatus()), SystemRole.COL_STATUS, roleDto.getStatus());
        qw.ge(null!= roleDto.getBeginTime(), SystemRole.COL_CREATE_TIME, roleDto.getBeginTime());
        qw.le(null!= roleDto.getEndTime(), SystemRole.COL_CREATE_TIME, roleDto.getEndTime());
        qw.orderByAsc(SystemRole.COL_ROLE_SORT);
        this.baseMapper.selectPage(page,qw);
        return new DataGridViewVO(page.getTotal(),page.getRecords());
    }

    @Override
    public List<SystemRole> listAllRoles() {
        QueryWrapper<SystemRole> qw=new QueryWrapper<>();
        qw.eq(SystemRole.COL_STATUS, ApiConstant.STATUS_TRUE);
        qw.orderByAsc(SystemRole.COL_ROLE_SORT);
        return this.baseMapper.selectList(qw);
    }

    @Override
    public SystemRole getOne(Long roleId) {
        return this.baseMapper.selectById(roleId);
    }

    @Override
    public int addRole(SystemRole roleDto) {
        SystemRole role = new SystemRole();
        BeanUtil.copyProperties(roleDto,role);
        //设置创建人和创建时间
        role.setCreateBy(roleDto.getSimpleUser().getUserName());
        role.setCreateTime(DateUtil.date());
        return this.baseMapper.insert(role);
    }

    @Override
    public int updateRole(SystemRole roleDto) {
        SystemRole role = new SystemRole();
        BeanUtil.copyProperties(roleDto,role);
        //设置修改人
        role.setUpdateBy(roleDto.getSimpleUser().getUserName());
        return this.baseMapper.updateById(role);
    }

    @Override
    public int deleteRoleByIds(Long[] roleIds) {
        List<Long> ids = Arrays.asList(roleIds);
        SystemRole role = new SystemRole();
        if(!CollectionUtil.isEmpty(ids)) {
            //还要删除sys_role_menu
            this.baseMapper.deleteRoleMenuByRoleIds(ids);
            //还要删除sys_role_user
            this.baseMapper.deleteRoleUserByRoleIds(ids);
            return this.baseMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public void saveRoleMenu(Long roleId, Long[] menuIds) {
        //根据角色ID删除sys_role_menu的数据
        this.baseMapper.deleteRoleMenuByRoleIds(Collections.singletonList(roleId));
        Arrays.asList(menuIds).forEach(menuId -> baseMapper.saveRoleMenu(roleId,menuId));
    }
}
