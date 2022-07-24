package cn.cloud9.service.impl;

import cn.cloud9.utils.CheckUtil;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cloud9.mapper.SystemLoginInfoMapper;
import cn.cloud9.domain.SystemLoginInfo;
import cn.cloud9.service.SystemLoginInfoService;
@Service
public class SystemLoginInfoServiceImpl extends ServiceImpl<SystemLoginInfoMapper, SystemLoginInfo> implements SystemLoginInfoService{

    @Override
    public int insertLoginInfo(SystemLoginInfo loginInfo) {
        return this.baseMapper.insert(loginInfo);
    }

    @Override
    public DataGridViewVO listForPage(SystemLoginInfo loginInfoDto) {
        Page<SystemLoginInfo> page = this.baseMapper.selectPage(
            new Page<>(loginInfoDto.getPageNum(),loginInfoDto.getPageSize()),
            new LambdaQueryWrapper<SystemLoginInfo>()
            .like(StringUtils.isNotBlank(loginInfoDto.getUserName()), SystemLoginInfo::getUserName, loginInfoDto.getUserName())
            .like(StringUtils.isNotBlank(loginInfoDto.getIpAddr()), SystemLoginInfo::getIpAddr, loginInfoDto.getIpAddr())
            .like(StringUtils.isNotBlank(loginInfoDto.getLoginAccount()), SystemLoginInfo::getLoginAccount, loginInfoDto.getLoginAccount())
            .eq(StringUtils.isNotBlank(loginInfoDto.getLoginStatus()), SystemLoginInfo::getLoginStatus, loginInfoDto.getLoginStatus())
            .eq(StringUtils.isNotBlank(loginInfoDto.getLoginType()), SystemLoginInfo::getLoginType, loginInfoDto.getLoginType())
            .between(
                !CheckUtil.isEmpty(loginInfoDto.getBeginTime()) && !CheckUtil.isEmpty(loginInfoDto.getEndTime()),
                SystemLoginInfo::getLoginTime,
                loginInfoDto.getBeginTime(),
                loginInfoDto.getEndTime()
            )
            .orderByDesc(SystemLoginInfo::getLoginTime)
        );
        return new DataGridViewVO(page.getTotal(),page.getRecords());
    }

    @Override
    public int deleteLoginInfoByIds(Long[] infoIds) {
        return CheckUtil.isEmptyArray(infoIds) ?
            0 : this.baseMapper.deleteBatchIds(Arrays.asList(infoIds));
    }

    @Override
    public int clearLoginInfo() {
        return this.baseMapper.delete(null);
    }
}
