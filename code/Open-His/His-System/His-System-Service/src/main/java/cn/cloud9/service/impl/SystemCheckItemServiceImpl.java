package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.SystemCheckItem;
import cn.cloud9.mapper.SystemCheckItemMapper;
import cn.cloud9.service.SystemCheckItemService;
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
public class SystemCheckItemServiceImpl extends ServiceImpl<SystemCheckItemMapper, SystemCheckItem> implements SystemCheckItemService {

    @Override
    public DataGridViewVO listCheckItemPage(SystemCheckItem checkItemDto) {
        Page<SystemCheckItem> page = new Page<>(checkItemDto.getPageNum(), checkItemDto.getPageSize());
        QueryWrapper<SystemCheckItem> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(checkItemDto.getCheckItemName()), SystemCheckItem.COL_CHECK_ITEM_NAME, checkItemDto.getCheckItemName());
        qw.like(StringUtils.isNotBlank(checkItemDto.getKeywords()), SystemCheckItem.COL_KEYWORDS, checkItemDto.getKeywords());
        qw.eq(StringUtils.isNotBlank(checkItemDto.getTypeId()), SystemCheckItem.COL_TYPE_ID, checkItemDto.getTypeId());
        qw.eq(StringUtils.isNotBlank(checkItemDto.getStatus()), SystemCheckItem.COL_STATUS, checkItemDto.getStatus());
        this.baseMapper.selectPage(page, qw);
        return new DataGridViewVO(page.getTotal(), page.getRecords());
    }

    @Override
    public SystemCheckItem getOne(Long checkItemId) {
        return this.baseMapper.selectById(checkItemId);
    }

    @Override
    public int addCheckItem(SystemCheckItem checkItemDto) {
        checkItemDto.setCreateTime(DateUtil.date());
        checkItemDto.setCreateBy(checkItemDto.getSimpleUser().getUserName());
        return this.baseMapper.insert(checkItemDto);
    }

    @Override
    public int updateCheckItem(SystemCheckItem checkItemDto) {
        checkItemDto.setUpdateBy(checkItemDto.getSimpleUser().getUserName());
        return this.baseMapper.updateById(checkItemDto);
    }

    @Override
    public int deleteCheckItemByIds(Long[] checkItemIds) {
        List<Long> ids = Arrays.asList(checkItemIds);
        return CollectionUtil.isEmpty(ids) ? 0 : this.baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<SystemCheckItem> queryAllCheckItems() {
        QueryWrapper<SystemCheckItem> qw = new QueryWrapper<>();
        qw.eq(SystemCheckItem.COL_STATUS, ApiConstant.STATUS_TRUE);
        return this.baseMapper.selectList(qw);
    }
}
