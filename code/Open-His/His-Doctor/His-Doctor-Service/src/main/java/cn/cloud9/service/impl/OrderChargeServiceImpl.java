package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.CareOrderItem;
import cn.cloud9.domain.form.OrderChargeFrom;
import cn.cloud9.domain.OrderChargeItem;
import cn.cloud9.mapper.CareOrderItemMapper;
import cn.cloud9.mapper.OrderChargeItemMapper;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cloud9.mapper.OrderChargeMapper;
import cn.cloud9.domain.OrderCharge;
import cn.cloud9.service.OrderChargeService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@Service(
        methods = {
                @Method(name = "saveOrderAndItems", retries = 1),
                @Method(name = "paySuccess", retries = 1)
        }
)
public class OrderChargeServiceImpl extends ServiceImpl<OrderChargeMapper, OrderCharge> implements OrderChargeService {

    @Resource
    private OrderChargeItemMapper orderChargeItemMapper;

    @Resource
    private CareOrderItemMapper careOrderItemMapper;

    /**
     * 保存订单及详情
     *
     * @param orderChargeFromDto
     */
    @Override
    public void saveOrderAndItems(OrderChargeFrom orderChargeFromDto) {
        OrderCharge orderChargeDto = orderChargeFromDto.getOrderChargeDto();
        List<OrderChargeItem> orderChargeItemDtoList = orderChargeFromDto.getOrderChargeItemDtoList();

        orderChargeDto.setOrderStatus(ApiConstant.ORDER_STATUS_0);
        orderChargeDto.setCreateTime(DateUtil.date());
        orderChargeDto.setCreateBy(orderChargeFromDto.getSimpleUser().getUserName());
        int i = this.baseMapper.insert(orderChargeDto);
        //保存详情
        for (OrderChargeItem orderChargeItemDto : orderChargeItemDtoList) {
            OrderChargeItem orderChargeItem = new OrderChargeItem();
            BeanUtil.copyProperties(orderChargeItemDto, orderChargeItem);
            //订单关联订单ID
            orderChargeItem.setOrderId(orderChargeDto.getOrderId());
            orderChargeItem.setStatus(ApiConstant.ORDER_DETAILS_STATUS_0);
            this.orderChargeItemMapper.insert(orderChargeItem);
        }
    }

    /**
     * 支付成功的回调
     *
     * @param orderId       支付订单ID
     * @param payPlatformId 平台交易ID 如果是现金，则为空
     */
    @Override
    public void paySuccess(String orderId, String payPlatformId, String payType) {
        //根据支付订单ID查询支付订单
        OrderCharge orderCharge = this.baseMapper.selectById(orderId);
        //设置平台交易编号
        orderCharge.setPayPlatformId(payPlatformId);
        //设置支付时间
        orderCharge.setPayTime(DateUtil.date());
        //修改订单状态
        orderCharge.setOrderStatus(ApiConstant.ORDER_STATUS_1);//已支付

        orderCharge.setPayType(payType);

        //更新订单状态
        this.baseMapper.updateById(orderCharge);
        //根据支付订单号查询支付订单详情
        QueryWrapper<OrderChargeItem> qw = new QueryWrapper<>();
        qw.eq(OrderChargeItem.COL_ORDER_ID, orderId);
        List<OrderChargeItem> orderChargeItems = this.orderChargeItemMapper.selectList(qw);
        List<String> allItemIds = new ArrayList<>();
        for (OrderChargeItem orderChargeItem : orderChargeItems) {
            allItemIds.add(orderChargeItem.getItemId());
        }

        //更新收费详情的状态
        OrderChargeItem orderItemObj = new OrderChargeItem();
        orderItemObj.setStatus(ApiConstant.ORDER_DETAILS_STATUS_1);
        QueryWrapper<OrderChargeItem> orderItemQw = new QueryWrapper<>();
        orderItemQw.in(OrderChargeItem.COL_ITEM_ID, allItemIds);
        this.orderChargeItemMapper.update(orderItemObj, orderItemQw);
        //更新处方详情的状态
        CareOrderItem careItemObj = new CareOrderItem();
        careItemObj.setStatus(ApiConstant.ORDER_DETAILS_STATUS_1);
        QueryWrapper<CareOrderItem> careItemQw = new QueryWrapper<>();
        careItemQw.in(CareOrderItem.COL_ITEM_ID, allItemIds);
        this.careOrderItemMapper.update(careItemObj, careItemQw);
    }

    @Override
    public OrderCharge queryOrderChargeByOrderId(String orderId) {
        return this.baseMapper.selectById(orderId);
    }

    @Override
    public DataGridViewVO queryAllOrderChargeForPage(OrderCharge orderChargeDto) {
        Page<OrderCharge> page=new Page<>(orderChargeDto.getPageNum(),orderChargeDto.getPageSize());
        QueryWrapper<OrderCharge> qw=new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(orderChargeDto.getPatientName()),OrderCharge.COL_PATIENT_NAME,orderChargeDto.getPatientName());
        qw.like(StringUtils.isNotBlank(orderChargeDto.getRegId()),OrderCharge.COL_REG_ID,orderChargeDto.getRegId());
        qw.orderByDesc(OrderCharge.COL_CREATE_TIME);
        this.baseMapper.selectPage(page,qw);
        return new DataGridViewVO(page.getTotal(),page.getRecords());
    }

    @Override
    public List<OrderChargeItem> queryOrderChargeItemByOrderId(String orderId) {
        QueryWrapper<OrderChargeItem> qw=new QueryWrapper<>();
        qw.eq(OrderChargeItem.COL_ORDER_ID,orderId);
        return this.orderChargeItemMapper.selectList(qw);
    }

    @Override
    public OrderChargeItem queryOrderChargeItemByItemId(String itemId) {
        return this.orderChargeItemMapper.selectById(itemId);
    }
}
