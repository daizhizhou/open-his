package cn.cloud9.service.impl;

import cn.cloud9.domain.InventoryLog;
import cn.cloud9.mapper.InventoryLogMapper;
import cn.cloud9.service.InventoryLogService;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月26日 下午 11:54
 */
@Component
@Service
public class InventoryLogServiceImpl
        extends ServiceImpl<InventoryLogMapper, InventoryLog>
        implements InventoryLogService {

    @Override
    public DataGridViewVO listInventoryLogPage(InventoryLog inventoryLogDto) {
        Page<InventoryLog> page = new Page<>(inventoryLogDto.getPageNum(), inventoryLogDto.getPageSize());
        QueryWrapper<InventoryLog> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(inventoryLogDto.getPurchaseId()), InventoryLog.COL_PURCHASE_ID, inventoryLogDto.getPurchaseId());
        qw.like(StringUtils.isNotBlank(inventoryLogDto.getMedicinesName()), InventoryLog.COL_MEDICINES_NAME, inventoryLogDto.getMedicinesName());
        qw.eq(StringUtils.isNotBlank(inventoryLogDto.getMedicinesType()), InventoryLog.COL_MEDICINES_TYPE, inventoryLogDto.getMedicinesType());
        qw.eq(StringUtils.isNotBlank(inventoryLogDto.getPrescriptionType()), InventoryLog.COL_PRESCRIPTION_TYPE, inventoryLogDto.getPrescriptionType());
        qw.eq(StringUtils.isNotBlank(inventoryLogDto.getProducterId()), InventoryLog.COL_PRODUCTER_ID, inventoryLogDto.getProducterId());
        qw.eq(StringUtils.isNotBlank(inventoryLogDto.getPrescriptionType()), InventoryLog.COL_PRESCRIPTION_TYPE, inventoryLogDto.getPrescriptionType());

        qw.ge(inventoryLogDto.getBeginTime() != null, InventoryLog.COL_CREATE_TIME, inventoryLogDto.getBeginTime());
        qw.le(inventoryLogDto.getEndTime() != null, InventoryLog.COL_CREATE_TIME, inventoryLogDto.getEndTime());
        qw.orderByDesc(InventoryLog.COL_CREATE_TIME);
        this.baseMapper.selectPage(page, qw);
        return new DataGridViewVO(page.getTotal(), page.getRecords());
    }
}
