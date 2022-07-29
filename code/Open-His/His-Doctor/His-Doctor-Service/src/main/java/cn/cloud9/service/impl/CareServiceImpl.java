package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.*;
import cn.cloud9.mapper.CareHistoryMapper;
import cn.cloud9.mapper.CareOrderItemMapper;
import cn.cloud9.mapper.CareOrderMapper;
import cn.cloud9.mapper.RegistrationMapper;
import cn.cloud9.service.CareService;
import cn.cloud9.utils.IdGeneratorSnowflakeUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
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
    public List<CareOrderItem> queryCareOrderItemsByCoId(String coId) {
        QueryWrapper<CareOrderItem> qw = new QueryWrapper<>();
        qw.eq(CareOrderItem.COL_CO_ID, coId);
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
}
