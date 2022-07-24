package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.SystemRegistItem;
import cn.cloud9.mapper.SystemRegistItemMapper;
import cn.cloud9.service.SystemRegistItemService;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SystemRegistItemServiceImpl extends ServiceImpl<SystemRegistItemMapper, SystemRegistItem> implements SystemRegistItemService {

    @Override
    public DataGridViewVO listRegisteredItemPage(SystemRegistItem registeredItemDto) {
        Page<SystemRegistItem> page = new Page<>(registeredItemDto.getPageNum(), registeredItemDto.getPageSize());
        QueryWrapper<SystemRegistItem> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(registeredItemDto.getRegItemName()), SystemRegistItem.COL_REG_ITEM_NAME, registeredItemDto.getRegItemName());
        qw.eq(StringUtils.isNotBlank(registeredItemDto.getStatus()), SystemRegistItem.COL_STATUS, registeredItemDto.getStatus());
        this.baseMapper.selectPage(page, qw);
        return new DataGridViewVO(page.getTotal(), page.getRecords());
    }

    @Override
    public SystemRegistItem getOne(Long registeredItemId) {
        return this.baseMapper.selectById(registeredItemId);
    }

    @Override
    public int addRegisteredItem(SystemRegistItem registeredItemDto) {
        registeredItemDto.setCreateTime(DateUtil.date());
        registeredItemDto.setCreateBy(registeredItemDto.getSimpleUser().getUserName());
        return this.baseMapper.insert(registeredItemDto);
    }

    @Override
    public int updateRegisteredItem(SystemRegistItem registeredItemDto) {
        registeredItemDto.setUpdateBy(registeredItemDto.getSimpleUser().getUserName());
        registeredItemDto.setCreateTime(DateUtil.date());
        return this.baseMapper.updateById(registeredItemDto);
    }

    @Override
    public int deleteRegisteredItemByIds(Long[] registeredItemIds) {
        List<Long> ids = Arrays.asList(registeredItemIds);
        return CollectionUtil.isEmpty(ids) ? 0 : this.baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<SystemRegistItem> queryAllRegisteredItems() {
        QueryWrapper<SystemRegistItem> qw = new QueryWrapper<>();
        qw.eq(SystemRegistItem.COL_STATUS, ApiConstant.STATUS_TRUE);
        return this.baseMapper.selectList(qw);
    }
}
