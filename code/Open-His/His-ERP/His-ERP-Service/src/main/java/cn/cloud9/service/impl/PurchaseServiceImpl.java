package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.*;
import cn.cloud9.mapper.InventoryLogMapper;
import cn.cloud9.mapper.MedicinesMapper;
import cn.cloud9.mapper.PurchaseItemMapper;
import cn.cloud9.utils.IdGeneratorSnowflakeUtil;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cloud9.mapper.PurchaseMapper;
import cn.cloud9.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional
public class PurchaseServiceImpl extends ServiceImpl<PurchaseMapper, Purchase> implements PurchaseService {

    @Resource
    private PurchaseItemMapper purchaseItemMapper;

    @Resource
    private InventoryLogMapper inventoryLogMapper;

    @Resource
    private MedicinesMapper medicinesMapper;

    @Override
    public DataGridViewVO listPurchasePage(Purchase purchaseDto) {
        Page<Purchase> page = new Page<>(purchaseDto.getPageNum(), purchaseDto.getPageSize());
        QueryWrapper<Purchase> qw = new QueryWrapper<>();
        qw.eq(StringUtils.isNotBlank(purchaseDto.getProviderId()), Purchase.COL_PROVIDER_ID, purchaseDto.getProviderId());
        qw.eq(StringUtils.isNotBlank(purchaseDto.getStatus()), Purchase.COL_STATUS, purchaseDto.getStatus());
        qw.like(StringUtils.isNotBlank(purchaseDto.getApplyUserName()), Purchase.COL_APPLY_USER_NAME, purchaseDto.getApplyUserName());
        qw.orderByDesc(Purchase.COL_CREATE_TIME);
        this.baseMapper.selectPage(page, qw);
        return new DataGridViewVO(page.getTotal(), page.getRecords());
    }

    @Override
    public Purchase getPurchaseById(String purchaseId) {
        return this.baseMapper.selectById(purchaseId);
    }

    @Override
    public int doAudit(String purchaseId, SimpleUser simpleUser) {
        Purchase purchase = this.baseMapper.selectById(purchaseId);
        purchase.setStatus(ApiConstant.STOCK_PURCHASE_STATUS_2);//设置状态为待审核
        purchase.setApplyUserName(simpleUser.getUserName());
        purchase.setApplyUserId(Long.valueOf(simpleUser.getUserId().toString()));
        purchase.setUpdateTime(DateUtil.date());
        purchase.setUpdateBy(purchase.getSimpleUser().getUserName());
        return this.baseMapper.updateById(purchase);
    }

    @Override
    public int doInvalid(String purchaseId) {
        Purchase purchase = this.baseMapper.selectById(purchaseId);
        purchase.setStatus(ApiConstant.STOCK_PURCHASE_STATUS_5);//设置状态为作废
        purchase.setUpdateTime(DateUtil.date());
        purchase.setUpdateBy(purchase.getSimpleUser().getUserName());
        return this.baseMapper.updateById(purchase);
    }

    @Override
    public int auditPass(String purchaseId) {
        Purchase purchase = this.baseMapper.selectById(purchaseId);
        purchase.setStatus(ApiConstant.STOCK_PURCHASE_STATUS_3);//设置状态为审核通过
        purchase.setUpdateTime(DateUtil.date());
        purchase.setUpdateBy(purchase.getSimpleUser().getUserName());
        return this.baseMapper.updateById(purchase);
    }

    @Override
    public int auditNoPass(String purchaseId, String auditMsg) {
        Purchase purchase = this.baseMapper.selectById(purchaseId);
        purchase.setStatus(ApiConstant.STOCK_PURCHASE_STATUS_4);//设置状态为审核不通过
        purchase.setAuditMsg(auditMsg);
        purchase.setUpdateTime(DateUtil.date());
        purchase.setUpdateBy(purchase.getSimpleUser().getUserName());
        return this.baseMapper.updateById(purchase);
    }

    @Override
    public List<PurchaseItem> getPurchaseItemById(String purchaseId) {
        if (null != purchaseId) {
            QueryWrapper<PurchaseItem> qw = new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID, purchaseId);
            return purchaseItemMapper.selectList(qw);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public int addPurchaseAndItem(PurchaseForm purchaseFormDto) {
        final Purchase formDtoPurchase = purchaseFormDto.getPurchase();
        Purchase purchase = this.baseMapper.selectById(formDtoPurchase.getPurchaseId());
        if (null != purchase) {
            //删除之前的数据
            this.baseMapper.deleteById(purchase.getPurchaseId());
            //删除之前的详情数据
            QueryWrapper<PurchaseItem> qw = new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID, purchase.getPurchaseId());
            this.purchaseItemMapper.delete(qw);
        }
        //保存采购单主表数据
        formDtoPurchase.setStatus(ApiConstant.STOCK_PURCHASE_STATUS_1);//未提交状态
        formDtoPurchase.setCreateBy(purchaseFormDto.getPurchase().getSimpleUser().getUserName());
        formDtoPurchase.setCreateTime(DateUtil.date());
        int a = this.baseMapper.insert(formDtoPurchase);
        //保存详情数据
        purchaseFormDto.getPurchaseItemList().forEach(purchaseItem -> {
            purchaseItem.setPurchaseId(formDtoPurchase.getPurchaseId());
            purchaseItem.setItemId(IdGeneratorSnowflakeUtil.snowflakeId().toString());
            this.purchaseItemMapper.insert(purchaseItem);
        });
        return a;
    }

    @Override
    public int addPurchaseAndItemToAudit(PurchaseForm purchaseFormDto) {
        final Purchase formDtoPurchase = purchaseFormDto.getPurchase();
        Purchase purchase = this.baseMapper.selectById(formDtoPurchase.getPurchaseId());
        if (null != purchase) {
            //删除之前的数据
            this.baseMapper.deleteById(purchase.getPurchaseId());
            //删除之前的详情数据
            QueryWrapper<PurchaseItem> qw = new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID, purchase.getPurchaseId());
            this.purchaseItemMapper.delete(qw);
        }
        //保存采购单主表数据
        formDtoPurchase.setStatus(ApiConstant.STOCK_PURCHASE_STATUS_2);//待审核
        formDtoPurchase.setCreateBy(purchaseFormDto.getPurchase().getSimpleUser().getUserName());
        formDtoPurchase.setCreateTime(DateUtil.date());
        //设置申请人和申请人的ID
        formDtoPurchase.setApplyUserId(Long.valueOf(formDtoPurchase.getSimpleUser().getUserId().toString()));
        formDtoPurchase.setApplyUserName(formDtoPurchase.getSimpleUser().getUserName());
        int a = this.baseMapper.insert(formDtoPurchase);
        //保存详情数据
        for (PurchaseItem item : purchaseFormDto.getPurchaseItemList()) {
            item.setPurchaseId(formDtoPurchase.getPurchaseId());
            item.setItemId(IdGeneratorSnowflakeUtil.snowflakeId().toString());
            this.purchaseItemMapper.insert(item);
        }
        return a;
    }

    @Override
    public int doInventory(String purchaseId, SimpleUser currentSimpleUser) {
        Purchase purchase = this.baseMapper.selectById(purchaseId);
        if (null != purchase || purchase.getStatus().equals(ApiConstant.STOCK_PURCHASE_STATUS_3)) {
            //查询详情
            QueryWrapper<PurchaseItem> qw = new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID, purchase.getPurchaseId());
            List<PurchaseItem> purchaseItems = this.purchaseItemMapper.selectList(qw);
            for (PurchaseItem purchaseItem : purchaseItems) {
                InventoryLog inventoryLog = new InventoryLog();
                inventoryLog.setInventoryLogId(purchaseItem.getItemId());
                inventoryLog.setPurchaseId(purchaseItem.getPurchaseId());
                inventoryLog.setMedicinesId(purchaseItem.getMedicinesId());
                inventoryLog.setInventoryLogNum(purchaseItem.getPurchaseNumber());
                inventoryLog.setTradePrice(purchaseItem.getTradePrice());
                inventoryLog.setTradeTotalAmount(purchaseItem.getTradeTotalAmount());
                inventoryLog.setBatchNumber(purchaseItem.getBatchNumber());
                inventoryLog.setMedicinesName(purchaseItem.getMedicinesName());
                inventoryLog.setMedicinesType(purchaseItem.getMedicinesType());
                inventoryLog.setPrescriptionType(purchaseItem.getPrescriptionType());
                inventoryLog.setProducterId(purchaseItem.getProducterId());
                inventoryLog.setConversion(purchaseItem.getConversion());
                inventoryLog.setUnit(purchaseItem.getUnit());
                inventoryLog.setCreateTime(DateUtil.date());
                inventoryLog.setCreateBy(currentSimpleUser.getUserName());
                //保存数据
                inventoryLogMapper.insert(inventoryLog);

                //更新药品库存
                Medicines medicines = this.medicinesMapper.selectById(purchaseItem.getMedicinesId());
                medicines.setMedicinesStockNum(medicines.getMedicinesStockNum() + purchaseItem.getPurchaseNumber());
                medicines.setUpdateBy(currentSimpleUser.getUserName());
                this.medicinesMapper.updateById(medicines);
            }
            //入库成功  修改单据的状态为入库成功
            purchase.setStatus(ApiConstant.STOCK_PURCHASE_STATUS_6);//入库成功
            purchase.setStorageOptTime(DateUtil.date());
            purchase.setStorageOptUser(currentSimpleUser.getUserName());
            return this.baseMapper.updateById(purchase);
        }
        return -1;
    }
}
