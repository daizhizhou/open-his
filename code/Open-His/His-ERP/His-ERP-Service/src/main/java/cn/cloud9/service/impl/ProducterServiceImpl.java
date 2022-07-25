package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;


import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cloud9.domain.Producter;
import cn.cloud9.mapper.ProducterMapper;
import cn.cloud9.service.ProducterService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * 注意！ 不是Spring的@Service注册Bean
 * 是Dubbo的服务注册Bean，同样Spring也需要管理，换@Component注册
 */
@Component
@Service
public class ProducterServiceImpl extends ServiceImpl<ProducterMapper, Producter> implements ProducterService {

    @Override
    public DataGridViewVO listProducterPage(Producter producterDto) {
        Page<Producter> page = new Page<>(producterDto.getPageNum(), producterDto.getPageSize());
        QueryWrapper<Producter> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(producterDto.getProducterName()), Producter.COL_PRODUCTER_NAME, producterDto.getProducterName());
        qw.like(StringUtils.isNotBlank(producterDto.getKeywords()), Producter.COL_KEYWORDS, producterDto.getKeywords());
        qw.eq(StringUtils.isNotBlank(producterDto.getProducterTel()), Producter.COL_PRODUCTER_TEL, producterDto.getProducterTel());
        qw.eq(StringUtils.isNotBlank(producterDto.getStatus()), Producter.COL_STATUS, producterDto.getStatus());
        qw.ge(producterDto.getBeginTime() != null, Producter.COL_CREATE_TIME, producterDto.getBeginTime());
        qw.le(producterDto.getEndTime() != null, Producter.COL_CREATE_TIME, producterDto.getEndTime());
        this.baseMapper.selectPage(page, qw);
        return new DataGridViewVO(page.getTotal(), page.getRecords());
    }

    @Override
    public Producter getOne(Long producterId) {
        return this.baseMapper.selectById(producterId);
    }

    @Override
    public int addProducter(Producter producterDto) {
        producterDto.setCreateTime(DateUtil.date());
        producterDto.setCreateBy(producterDto.getSimpleUser().getUserName());
        producterDto.setUpdateTime(DateUtil.date());
        producterDto.setUpdateBy(producterDto.getSimpleUser().getUserName());
        return this.baseMapper.insert(producterDto);
    }

    @Override
    public int updateProducter(Producter producterDto) {
        producterDto.setUpdateTime(DateUtil.date());
        producterDto.setUpdateBy(producterDto.getSimpleUser().getUserName());
        return this.baseMapper.updateById(producterDto);
    }

    @Override
    public int deleteProducterByIds(Long[] producterIds) {
        List<Long> ids = Arrays.asList(producterIds);
        if (ids.size() > 0) {
            return this.baseMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public List<Producter> selectAllProducter() {
        QueryWrapper<Producter> qw = new QueryWrapper<>();
        qw.eq(Producter.COL_STATUS, ApiConstant.STATUS_TRUE);
        return this.baseMapper.selectList(qw);
    }
}

