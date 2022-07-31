package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.CareOrderItem;
import cn.cloud9.domain.OrderChargeItem;
import cn.cloud9.domain.OrderRefund;
import cn.cloud9.domain.OrderRefundItem;
import cn.cloud9.domain.form.OrderRefundForm;
import cn.cloud9.mapper.*;
import cn.cloud9.service.OrderRefundService;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service(methods = {@Method(name = "saveOrderAndItems", retries = 1),
        @Method(name = "backSuccess", retries = 1)})
public class OrderRefundServiceImpl extends ServiceImpl<OrderRefundMapper, OrderRefund> implements OrderRefundService {


    @Resource
    private OrderRefundItemMapper orderRefundItemMapper;

    @Resource
    private OrderChargeMapper orderChargeMapper;

    @Resource
    private OrderChargeItemMapper orderChargeItemMapper;

    @Resource
    private CareOrderItemMapper careOrderItemMapper;

    @Override
    public void saveOrderAndItems(OrderRefundForm orderBackfeeFormDto) {
        OrderRefund orderBackfeeDto = orderBackfeeFormDto.getOrderBackfeeDto();
        List<OrderRefundItem> orderBackfeeItemDtoList = orderBackfeeFormDto.getOrderBackfeeItemDtoList();

        orderBackfeeDto.setBackStatus(ApiConstant.ORDER_STATUS_0);
        orderBackfeeDto.setCreateTime(DateUtil.date());
        orderBackfeeDto.setCreateBy(orderBackfeeFormDto.getSimpleUser().getUserName());
        int i = this.baseMapper.insert(orderBackfeeDto);
        //保存详情
        for (OrderRefundItem orderBackfeeItemDto : orderBackfeeItemDtoList) {
            //订单关联退费订单ID
            orderBackfeeItemDto.setBackId(orderBackfeeDto.getBackId());
            orderBackfeeItemDto.setStatus(ApiConstant.ORDER_DETAILS_STATUS_0);
            this.orderRefundItemMapper.insert(orderBackfeeItemDto);
        }
    }

    @Override
    public void backSuccess(String backId, String backPlatformId, String backType) {
        //根据退费订单ID查询退费订单
        OrderRefund orderBackfee = this.baseMapper.selectById(backId);
        //设置平台交易编号
        orderBackfee.setBackPlatformId(backPlatformId);
        //设置退费类型
        orderBackfee.setBackType(backType);
        //设置退费时间
        orderBackfee.setBackTime(DateUtil.date());
        //修改订单状态
        orderBackfee.setBackStatus(ApiConstant.ORDER_BACKFEE_STATUS_1);//已退费
        //更新订单状态
        this.baseMapper.updateById(orderBackfee);
        //根据退费订单号查询退费订单详情
        QueryWrapper<OrderRefundItem> qw = new QueryWrapper<>();
        qw.eq(OrderRefundItem.COL_BACK_ID, backId);
        List<OrderRefundItem> orderBackfeeItems = this.orderRefundItemMapper.selectList(qw);
        List<String> allItemIds = new ArrayList<>();
        for (OrderRefundItem orderBackfeeItem : orderBackfeeItems) {
            allItemIds.add(orderBackfeeItem.getItemId());
        }
        //更新退费单的详情状态
        OrderRefundItem orderBackItemObj = new OrderRefundItem();
        orderBackItemObj.setStatus(ApiConstant.ORDER_DETAILS_STATUS_2);//已退费
        QueryWrapper<OrderRefundItem> orderBackItemQw = new QueryWrapper<>();
        orderBackItemQw.in(OrderRefundItem.COL_ITEM_ID, allItemIds);
        this.orderRefundItemMapper.update(orderBackItemObj, orderBackItemQw);

        //更新收费详情的状态
        OrderChargeItem orderItemObj = new OrderChargeItem();
        orderItemObj.setStatus(ApiConstant.ORDER_DETAILS_STATUS_2);//已退费
        QueryWrapper<OrderChargeItem> orderItemQw = new QueryWrapper<>();
        orderItemQw.in(OrderChargeItem.COL_ITEM_ID, allItemIds);
        this.orderChargeItemMapper.update(orderItemObj, orderItemQw);

        //更新处方详情的状态
        CareOrderItem careItemObj = new CareOrderItem();
        careItemObj.setStatus(ApiConstant.ORDER_DETAILS_STATUS_2);//已退费
        QueryWrapper<CareOrderItem> careItemQw = new QueryWrapper<>();
        careItemQw.in(CareOrderItem.COL_ITEM_ID, allItemIds);
        this.careOrderItemMapper.update(careItemObj, careItemQw);
    }

    @Override
    public DataGridViewVO queryAllOrderBackfeeForPage(OrderRefund orderBackfeeDto) {
        Page<OrderRefund> page=new Page<>(orderBackfeeDto.getPageNum(),orderBackfeeDto.getPageSize());
        QueryWrapper<OrderRefund> qw=new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(orderBackfeeDto.getPatientName()),OrderRefund.COL_PATIENT_NAME,orderBackfeeDto.getPatientName());
        qw.like(StringUtils.isNotBlank(orderBackfeeDto.getRegId()),OrderRefund.COL_REG_ID,orderBackfeeDto.getRegId());
        qw.orderByDesc(OrderRefund.COL_CREATE_TIME);
        this.baseMapper.selectPage(page,qw);
        return new DataGridViewVO(page.getTotal(),page.getRecords());
    }

    @Override
    public List<OrderRefundItem> queryrderBackfeeItemByBackId(String backId) {
        QueryWrapper<OrderRefundItem> qw=new QueryWrapper<>();
        qw.eq(OrderRefundItem.COL_BACK_ID, backId);
        return this.orderRefundItemMapper.selectList(qw);
    }

}
