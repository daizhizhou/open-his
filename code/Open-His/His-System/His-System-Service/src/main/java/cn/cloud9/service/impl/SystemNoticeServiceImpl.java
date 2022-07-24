package cn.cloud9.service.impl;

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
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cloud9.mapper.SystemNoticeMapper;
import cn.cloud9.domain.SystemNotice;
import cn.cloud9.service.SystemNoticeService;

@Service
public class SystemNoticeServiceImpl extends ServiceImpl<SystemNoticeMapper, SystemNotice> implements SystemNoticeService {

    @Override
    public DataGridViewVO listNoticePage(SystemNotice noticeDto) {
        Page<SystemNotice> page = new Page<>(noticeDto.getPageNum(), noticeDto.getPageSize());
        QueryWrapper<SystemNotice> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(noticeDto.getNoticeTitle()), SystemNotice.COL_NOTICE_TITLE, noticeDto.getNoticeTitle());
        qw.like(StringUtils.isNotBlank(noticeDto.getCreateBy()), SystemNotice.COL_CREATE_BY, noticeDto.getCreateBy());
        qw.eq(StringUtils.isNotBlank(noticeDto.getNoticeType()), SystemNotice.COL_NOTICE_TYPE, noticeDto.getNoticeType());
        qw.eq(StringUtils.isNotBlank(noticeDto.getStatus()), SystemNotice.COL_STATUS, noticeDto.getStatus());
        qw.orderByDesc(SystemNotice.COL_CREATE_TIME);
        this.baseMapper.selectPage(page, qw);
        return new DataGridViewVO(page.getTotal(), page.getRecords());
    }

    @Override
    public SystemNotice getOne(Long noticeId) {
        return this.baseMapper.selectById(noticeId);
    }

    @Override
    public int addNotice(SystemNotice noticeDto) {
        noticeDto.setCreateTime(DateUtil.date());
        noticeDto.setCreateBy(noticeDto.getSimpleUser().getUserName());
        return this.baseMapper.insert(noticeDto);
    }

    @Override
    public int updateNotice(SystemNotice noticeDto) {
        noticeDto.setUpdateBy(noticeDto.getSimpleUser().getUserName());
        noticeDto.setUpdateTime(DateUtil.date());
        return this.baseMapper.updateById(noticeDto);
    }

    @Override
    public int deleteNoticeByIds(Long[] noticeIds) {
        List<Long> ids = Arrays.asList(noticeIds);
        return CollectionUtil.isEmpty(ids) ?
                0 : this.baseMapper.deleteBatchIds(ids);
    }
}
