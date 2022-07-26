package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.PurchaseItem;
import cn.cloud9.domain.SimpleUser;
import cn.cloud9.mapper.PurchaseItemMapper;
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
import cn.cloud9.domain.Purchase;
import cn.cloud9.mapper.PurchaseMapper;
import cn.cloud9.service.PurchaseService;
@Component
@Service
public class PurchaseServiceImpl extends ServiceImpl<PurchaseMapper, Purchase> implements PurchaseService{

    @Resource
    private PurchaseItemMapper purchaseItemMapper;

    @Override
    public DataGridViewVO listPurchasePage(Purchase purchaseDto) {
        Page<Purchase> page=new Page<>(purchaseDto.getPageNum(),purchaseDto.getPageSize());
        QueryWrapper<Purchase> qw=new QueryWrapper<>();
        qw.eq(StringUtils.isNotBlank(purchaseDto.getProviderId()),Purchase.COL_PROVIDER_ID,purchaseDto.getProviderId());
        qw.eq(StringUtils.isNotBlank(purchaseDto.getStatus()),Purchase.COL_STATUS,purchaseDto.getStatus());
        qw.like(StringUtils.isNotBlank(purchaseDto.getApplyUserName()),Purchase.COL_APPLY_USER_NAME,purchaseDto.getApplyUserName());
        qw.orderByDesc(Purchase.COL_CREATE_TIME);
        this.baseMapper.selectPage(page,qw);
        return new DataGridViewVO(page.getTotal(),page.getRecords());
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
        if(null!=purchaseId){
            QueryWrapper<PurchaseItem> qw=new QueryWrapper<>();
            qw.eq(PurchaseItem.COL_PURCHASE_ID,purchaseId);
            return purchaseItemMapper.selectList(qw);
        }
        return Collections.EMPTY_LIST;
    }
}
