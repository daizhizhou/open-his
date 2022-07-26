package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cloud9.domain.Provider;
import cn.cloud9.mapper.ProviderMapper;
import cn.cloud9.service.ProviderService;

@Component
@Service(methods = {@Method(name = "addProvider", retries = 0)})
public class ProviderServiceImpl extends ServiceImpl<ProviderMapper, Provider> implements ProviderService {

    @Override
    public DataGridViewVO listProviderPage(Provider providerDto) {
        Page<Provider> page = new Page<>(providerDto.getPageNum(), providerDto.getPageSize());
        QueryWrapper<Provider> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(providerDto.getProviderName()), Provider.COL_PROVIDER_NAME, providerDto.getProviderName());
        qw.like(StringUtils.isNotBlank(providerDto.getContactName()), Provider.COL_CONTACT_NAME, providerDto.getContactName());
        qw.and(StringUtils.isNotBlank(providerDto.getContactTel()), new Consumer<QueryWrapper<Provider>>() {
            @Override //(tel like ? or mobile like ?)
            public void accept(QueryWrapper<Provider> providerQueryWrapper) {
                providerQueryWrapper.like(Provider.COL_CONTACT_TEL, providerDto.getContactTel())
                        .or().like(Provider.COL_CONTACT_MOBILE, providerDto.getContactTel());
            }
        });
        qw.eq(StringUtils.isNotBlank(providerDto.getStatus()), Provider.COL_STATUS, providerDto.getStatus());
        this.baseMapper.selectPage(page, qw);
        return new DataGridViewVO(page.getTotal(), page.getRecords());
    }

    @Override
    public Provider getOne(Long providerId) {
        return this.baseMapper.selectById(providerId);
    }

    @Override
    public int addProvider(Provider providerDto) {
        providerDto.setCreateTime(DateUtil.date());
        providerDto.setCreateBy(providerDto.getSimpleUser().getUserName());
        providerDto.setUpdateTime(DateUtil.date());
        providerDto.setUpdateBy(providerDto.getSimpleUser().getUserName());
        return this.baseMapper.insert(providerDto);
    }

    @Override
    public int updateProvider(Provider providerDto) {
        providerDto.setUpdateTime(DateUtil.date());
        providerDto.setUpdateBy(providerDto.getSimpleUser().getUserName());
        return this.baseMapper.updateById(providerDto);
    }

    @Override
    public int deleteProviderByIds(Long[] providerIds) {
        List<Long> ids = Arrays.asList(providerIds);
        if (ids.size() > 0) {
            return this.baseMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public List<Provider> selectAllProvider() {
        QueryWrapper<Provider> qw = new QueryWrapper<>();
        qw.eq(Provider.COL_STATUS, ApiConstant.STATUS_TRUE);
        return this.baseMapper.selectList(qw);
    }
}
