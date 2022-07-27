package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.utils.AppMd5Util;
import cn.cloud9.utils.CheckUtil;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cloud9.mapper.SystemUserMapper;
import cn.cloud9.domain.SystemUser;
import cn.cloud9.service.SystemUserService;

@Service
public class SystemUserServiceImpl
        extends ServiceImpl<SystemUserMapper, SystemUser>
        implements SystemUserService {


    @Override
    public SystemUser queryUserByPhone(String phone) {
        return this.baseMapper.selectOne(
                new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getPhone, phone)
        );
    }

    @Override
    public SystemUser getOne(Long userId) {
        return this.baseMapper.selectById(userId);
    }

    @Override
    public DataGridViewVO listUserForPage(SystemUser userDto) {
        Page<SystemUser> page = new Page<>(userDto.getPageNum(), userDto.getPageSize());
        QueryWrapper<SystemUser> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(userDto.getUserName()), SystemUser.COL_USER_NAME, userDto.getUserName());
        qw.like(StringUtils.isNotBlank(userDto.getPhone()), SystemUser.COL_PHONE, userDto.getPhone());
        qw.eq(StringUtils.isNotBlank(userDto.getStatus()), SystemUser.COL_STATUS, userDto.getStatus());
        qw.eq(!CheckUtil.isEmpty(userDto.getDeptId()), SystemUser.COL_DEPT_ID, userDto.getDeptId());
        qw.ge(!CheckUtil.isEmpty(userDto.getBeginTime()), SystemUser.COL_CREATE_TIME, userDto.getBeginTime());
        qw.le(!CheckUtil.isEmpty(userDto.getEndTime()), SystemUser.COL_CREATE_TIME, userDto.getEndTime());
        qw.orderByAsc(SystemUser.COL_USER_ID);
        this.baseMapper.selectPage(page, qw);
        return new DataGridViewVO(page.getTotal(), page.getRecords());
    }

    @Override
    public int addUser(SystemUser userDto) {
        userDto.setUserType(ApiConstant.USER_NORMAL);
        String defaultPwd = userDto.getPhone().substring(5);
        userDto.setCreateBy(userDto.getSimpleUser().getUserName());
        userDto.setCreateTime(DateUtil.date());
        userDto.setSalt(AppMd5Util.createSalt());
        userDto.setPassword(AppMd5Util.md5(defaultPwd, userDto.getSalt(), 2));
        return this.baseMapper.insert(userDto);
    }

    @Override
    public int updateUser(SystemUser userDto) {
        SystemUser user = this.baseMapper.selectById(userDto.getUserId());
        if (CheckUtil.isEmpty(user)) return 0;
        user.setUpdateBy(userDto.getSimpleUser().getUserName());
        user.setUpdateTime(DateUtil.date());
        return this.baseMapper.updateById(user);
    }

    @Override
    public int deleteUserByIds(Long[] userIds) {
        return this.baseMapper.deleteBatchIds(Arrays.asList(userIds));
    }

    @Override
    public List<SystemUser> getAllUsers() {
        QueryWrapper<SystemUser> qw = new QueryWrapper<>();
        qw.eq(SystemUser.COL_STATUS, ApiConstant.STATUS_TRUE);
        qw.eq(SystemUser.COL_USER_TYPE, ApiConstant.USER_NORMAL);
        qw.orderByAsc(SystemUser.COL_USER_ID);
        return this.baseMapper.selectList(qw);
    }

    @Override
    public void resetPassWord(Long[] userIds) {
        for (Long userId : userIds) {
            SystemUser user = this.baseMapper.selectById(userId);
            String defaultPwd = user.getUserType().equals(ApiConstant.USER_ADMIN) ?
                    "123456" : user.getPhone().substring(5);
            user.setSalt(AppMd5Util.createSalt());
            user.setPassword(AppMd5Util.md5(defaultPwd, user.getSalt(), 2));
            this.baseMapper.updateById(user);
        }
    }

    @Override
    public List<SystemUser> querySchedulingUsers(Long userId, Long deptId) {
        QueryWrapper<SystemUser> qw = new QueryWrapper<>();
        qw.eq(null != deptId, SystemUser.COL_DEPT_ID, deptId);
        qw.eq(null != userId, SystemUser.COL_USER_ID, userId);
        qw.eq(SystemUser.COL_SCHEDULING_FLAG, ApiConstant.SCHEDULING_FLAG_TRUE);
        return this.baseMapper.selectList(qw);
    }
}
