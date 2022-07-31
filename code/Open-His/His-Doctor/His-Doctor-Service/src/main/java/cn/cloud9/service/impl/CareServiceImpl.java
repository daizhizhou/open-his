package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.*;
import cn.cloud9.domain.form.CareOrderForm;
import cn.cloud9.mapper.*;
import cn.cloud9.service.CareService;
import cn.cloud9.service.MedicinesService;
import cn.cloud9.utils.IdGeneratorSnowflakeUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月29日 下午 08:56
 */
@Service
@Component
public class CareServiceImpl
        extends ServiceImpl<CareHistoryMapper, CareHistory>
        implements CareService {

    @Resource
    private CareOrderItemMapper careOrderItemMapper;
    @Resource
    private CareOrderMapper careOrderMapper;
    @Resource
    private RegistrationMapper registrationMapper;
    @Resource
    private OrderChargeItemMapper orderChargeItemMapper;

    @Reference
    private MedicinesService medicinesService;


    @Override
    public List<CareHistory> queryCareHistoryByPatientId(String patientId) {
        QueryWrapper<CareHistory> qw = new QueryWrapper<>();
        qw.eq(CareHistory.COL_PATIENT_ID, patientId);
        return this.baseMapper.selectList(qw);
    }

    @Override
    public CareHistory saveOrUpdateCareHistory(CareHistory careHistoryDto) {
        if (StringUtils.isNotBlank(careHistoryDto.getChId())) {
            this.baseMapper.updateById(careHistoryDto);
        } else {
            careHistoryDto.setChId(IdGeneratorSnowflakeUtil.generatorIdWithProfix(ApiConstant.ID_PREFIX_CH));
            this.baseMapper.insert(careHistoryDto);
        }
        return careHistoryDto;
    }

    @Override
    public CareHistory queryCareHistoryByRegId(String regId) {
        QueryWrapper<CareHistory> qw = new QueryWrapper<>();
        qw.eq(CareHistory.COL_REG_ID, regId);
        return this.baseMapper.selectOne(qw);
    }

    @Override
    public List<CareOrder> queryCareOrdersByChId(String chId) {
        QueryWrapper<CareOrder> qw = new QueryWrapper<>();
        qw.eq(CareOrder.COL_CH_ID, chId);
        return this.careOrderMapper.selectList(qw);
    }

    @Override
    public List<CareOrderItem> queryCareOrderItemsByCoId(String coId, String status) {
        QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
        qw.eq(CareOrderItem.COL_CO_ID, coId);
        qw.eq(StringUtils.isNotBlank(status), CareOrderItem.COL_STATUS, status);
        return this.careOrderItemMapper.selectList(qw);
    }

    @Override
    public CareHistory queryCareHistoryByChId(String chId) {
        return this.baseMapper.selectById(chId);
    }

    @Override
    public int saveCareOrderItem(CareOrderForm careOrderFormDto) {
        CareOrder careOrderDto = careOrderFormDto.getCareOrder();
        careOrderDto.setCreateBy(careOrderDto.getSimpleUser().getUserName());
        careOrderDto.setCreateTime(DateUtil.date());
        int i = this.careOrderMapper.insert(careOrderDto);//保存处方主表
        List<CareOrderItem> careOrderItems = careOrderFormDto.getCareOrderItems();
        //保存详情数据
        for (CareOrderItem careOrderItemDto : careOrderItems) {
            careOrderItemDto.setCoId(careOrderDto.getCoId());
            careOrderItemDto.setCreateTime(DateUtil.date());
            careOrderItemDto.setStatus(ApiConstant.ORDER_DETAILS_STATUS_0);//未支付
            careOrderItemDto.setItemId(IdGeneratorSnowflakeUtil.generatorIdWithProfix(ApiConstant.ID_PREFIX_ITEM));
            this.careOrderItemMapper.insert(careOrderItemDto);
        }
        return i;
    }

    @Override
    public CareOrderItem queryCareOrderItemByItemId(String itemId) {
        return this.careOrderItemMapper.selectById(itemId);
    }


    @Override
    public int deleteCareOrderItemByItemId(String itemId) {
        //注意点，如果删除了，要更新careOrder主表的all_amount
        CareOrderItem careOrderItem = this.careOrderItemMapper.selectById(itemId);
        String coId = careOrderItem.getCoId();//取出主表ID
        //删除
        int i = this.careOrderItemMapper.deleteById(itemId);

        //再根据coID查询还存在的详情数据
        QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
        qw.eq(CareOrderItem.COL_CO_ID, coId);
        List<CareOrderItem> careOrderItems = this.careOrderItemMapper.selectList(qw);
        if (careOrderItems != null && careOrderItems.size() > 0) {
            //重新计算总价格
            BigDecimal allAmount = new BigDecimal("0");
            for (CareOrderItem orderItem : careOrderItems) {
                allAmount = allAmount.add(orderItem.getAmount());
            }
            //再根据coId查询主表的数据
            CareOrder careOrder = this.careOrderMapper.selectById(coId);
            //更新主表的数据
            careOrder.setAllAmount(allAmount);
            this.careOrderMapper.updateById(careOrder);
        } else {
            //说明没有详情了，直接干掉主表里面的数据
            this.careOrderMapper.deleteById(coId);
        }
        return i;
    }

    @Override
    public int visitComplete(String regId) {
        Registration registration = new Registration();
        registration.setRegId(regId);
        registration.setRegStatus(ApiConstant.REG_STATUS_3);
        return this.registrationMapper.updateById(registration);
    }


    /**
     * 发药
     * 思路
     * 1，根据详情ID查询处方项目
     * 2，扣减库存(如果库存够就扣减 返回受影响的行数，如果不够就返回0)
     * 3，如果返回0 说是库存不够，停止发药
     * 4，如果返回>0 更新处方详情   支持详情的状态 为3
     *
     * @param itemIds
     * @return
     */
    @Override
    public String doMedicine(List<String> itemIds) {
        //根据详情ID查询处方详情
        QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
        qw.in(CareOrderItem.COL_ITEM_ID, itemIds);
        List<CareOrderItem> careOrderItems = this.careOrderItemMapper.selectList(qw);
        StringBuffer sb = new StringBuffer();
        for (CareOrderItem careOrderItem : careOrderItems) {
            //库存扣减
            int i = this.medicinesService.deductionMedicinesStorage(Long.valueOf(careOrderItem.getItemRefId()), careOrderItem.getNum().longValue());
            if (i > 0) {//说明库存够
                //更新处方详情状态
                careOrderItem.setStatus(ApiConstant.ORDER_DETAILS_STATUS_3);//已完成
                this.careOrderItemMapper.updateById(careOrderItem);
                //更新收费详情状态
                OrderChargeItem orderChargeItem = new OrderChargeItem();
                orderChargeItem.setItemId(careOrderItem.getItemId());
                orderChargeItem.setStatus(ApiConstant.ORDER_DETAILS_STATUS_3);
                this.orderChargeItemMapper.updateById(orderChargeItem);
            } else {
                sb.append("【" + careOrderItem.getItemName() + "】发药失败\n");
            }
        }
        if (StringUtils.isBlank(sb.toString())) {
            return null;
        } else {
            sb.append("原因：库存不足");
            return sb.toString();
        }
    }

    @Override
    public List<CareOrderItem> queryCareOrderItemsByStatus(String coType, String status) {
        QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
        qw.eq(StringUtils.isNotBlank(coType), CareOrderItem.COL_ITEM_TYPE, coType);
        qw.eq(StringUtils.isNotBlank(status), CareOrderItem.COL_STATUS, status);
        return this.careOrderItemMapper.selectList(qw);
    }

    @Override
    public CareOrder queryCareOrderByCoId(String coId) {
        return this.careOrderMapper.selectById(coId);
    }
}
