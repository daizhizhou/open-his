package cn.cloud9.controller.doctor;

import cn.cloud9.config.alipay.AlipayConfig;
import cn.cloud9.config.spring.HystrixSupport;
import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.*;
import cn.cloud9.domain.form.OrderChargeFrom;
import cn.cloud9.service.AlipayService;
import cn.cloud9.service.CareService;
import cn.cloud9.service.OrderChargeService;
import cn.cloud9.util.ShiroSecurityUtil;
import cn.cloud9.utils.IdGeneratorSnowflakeUtil;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.bean.BeanUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 上午 08:27
 */
@RestController
@RequestMapping("doctor/charge")
public class OrderChargeController extends HystrixSupport {
    @Reference
    private CareService careService;

    @Reference
    private OrderChargeService orderChargeService;

    /**
     * 创建现金收费订单
     */
    @PostMapping("createOrderChargeWithCash")
    public AjaxResult createOrderChargeWithCash(@RequestBody @Validated OrderChargeFrom orderChargeFromDto) {
        //1,保存订单
        orderChargeFromDto.getOrderChargeDto().setPayType(ApiConstant.PAY_TYPE_0);//现金支付
        orderChargeFromDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        String orderId = IdGeneratorSnowflakeUtil.generatorIdWithProfix(ApiConstant.ID_PREFIX_ODC);
        orderChargeFromDto.getOrderChargeDto().setOrderId(orderId);
        this.orderChargeService.saveOrderAndItems(orderChargeFromDto);
        //2,因为是现金支付，所有直接更新详情状态
        this.orderChargeService.paySuccess(orderId, null, ApiConstant.PAY_TYPE_0);
        return AjaxResult.success("创建订单并现金支付成功");
    }

    /**
     * 根据挂号ID查询未支付的处方信息及详情
     */
    @GetMapping("getNoChargeCareHistoryByRegId/{regId}")
    @HystrixCommand
    public AjaxResult getNoChargeCareHistoryByRegId(@PathVariable String regId) {
        //声明返回的Map对象
        Map<String, Object> res = new HashMap<>();
        //根据挂号单ID查询病例信息
        CareHistory careHistory = this.careService.queryCareHistoryByRegId(regId);
        if (null == careHistory) {
            return AjaxResult.fail("【" + regId + "】的挂号单没有对应的病例信息，请核对后再查询");
        }
        //放入默认值
        res.put("careHistory", careHistory);
        res.put("careOrders", Collections.EMPTY_LIST);
        //声明一个可以存放careOrders的集合
        List<Map<String, Object>> mapList = new ArrayList<>();
        //根据病例编号查询处方
        List<CareOrder> careOrders = this.careService.queryCareOrdersByChId(careHistory.getChId());
        if (careOrders.isEmpty()) {
            return AjaxResult.fail("【" + regId + "】的挂号单没相关的处方信息，请核对后再查询");
        }
        for (CareOrder careOrder : careOrders) {
            Map<String, Object> beanToMap = BeanUtil.beanToMap(careOrder);
            beanToMap.put("careOrderItems", Collections.EMPTY_LIST);
            BigDecimal allAmount = new BigDecimal("0");
            //根据处方ID查询未支持的处方详情列表
            List<CareOrderItem> careOrderItems = this.careService.queryCareOrderItemsByCoId(careOrder.getCoId(), ApiConstant.ORDER_DETAILS_STATUS_0);
            //如果当前处方未支付的详情为空 结束当前循环
            if (careOrderItems.isEmpty()) {
                continue;
            } else {
                //重新计算总价
                for (CareOrderItem careOrderItem : careOrderItems) {
                    allAmount = allAmount.add(careOrderItem.getAmount());
                }
                beanToMap.put("careOrderItems", careOrderItems);
                beanToMap.put("allAmount", allAmount);
                mapList.add(beanToMap);
            }
        }
        if (mapList.isEmpty()) {
            return AjaxResult.fail("【" + regId + "】的挂号单没未支付的处方信息，请核对后再查询");
        } else {
            res.put("careOrders", mapList);
            return AjaxResult.success(res);
        }
    }

    /**
     * 创建支付宝收费订单
     */
    @PostMapping("createOrderChargeWithZfb")
    public AjaxResult createOrderChargeWithZfb(@RequestBody @Validated OrderChargeFrom orderChargeFromDto) {
        //1,保存订单
        orderChargeFromDto.getOrderChargeDto().setPayType(ApiConstant.PAY_TYPE_1);//支付宝支付
        orderChargeFromDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        String orderId = IdGeneratorSnowflakeUtil.generatorIdWithProfix(ApiConstant.ID_PREFIX_ODC);
        orderChargeFromDto.getOrderChargeDto().setOrderId(orderId);
        this.orderChargeService.saveOrderAndItems(orderChargeFromDto);

        //2,因为是支付宝支付，所以我们要返回给页面一个二维码
        String outTradeNo = orderId;
        String subject = "Cloud9's医疗管理系统支付平台";
        String totalAmount = orderChargeFromDto.getOrderChargeDto().getOrderAmount().toString();
        String undiscountableAmount = null;
        String body = "";
        List<OrderChargeItem> orderChargeItemDtoList = orderChargeFromDto.getOrderChargeItemDtoList();
        for (OrderChargeItem orderChargeItemDto : orderChargeItemDtoList) {
            body += orderChargeItemDto.getItemName() + "-" + orderChargeItemDto.getItemPrice() + " ";
        }
        String notifyUrl = AlipayConfig.notifyUrl + outTradeNo;
        Map<String, Object> pay = AlipayService.pay(outTradeNo, subject, totalAmount, undiscountableAmount, body, notifyUrl);
        String qrCode = pay.get("qrCode").toString();
        if (StringUtils.isNotBlank(qrCode)) {
            //创建支付成功
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("allAmount", totalAmount);
            map.put("payUrl", qrCode);
            return AjaxResult.success(map);
        } else {
            return AjaxResult.fail(pay.get("msg").toString());
        }
    }

    /**
     * 根据订单ID查询订单信息【验证是否支付成功】
     */
    @GetMapping("queryOrderChargeOrderId/{orderId}")
    public AjaxResult queryOrderChargeOrderId(@PathVariable String orderId) {
        OrderCharge orderCharge = this.orderChargeService.queryOrderChargeByOrderId(orderId);
        if (null == orderCharge) {
            return AjaxResult.fail("【" + orderId + "】订单号所在的订单不存在，请核对后再输入");
        }
        // if (!orderCharge.getPayType().equals(ApiConstant.PAY_TYPE_1)) {
        //     return AjaxResult.fail("【" + orderId + "】订单号所在的订单不是支付宝支付的订单，请核对后再输入");
        // }
        return AjaxResult.success(orderCharge);
    }

    /**
     * 分页查询所有收费单
     */
    @GetMapping("queryAllOrderChargeForPage")
    public AjaxResult queryAllOrderChargeForPage(OrderCharge orderChargeDto) {
        DataGridViewVO dataGridView = this.orderChargeService.queryAllOrderChargeForPage(orderChargeDto);
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());
    }

    /**
     * 根据收费单的ID查询收费详情信息
     */
    @GetMapping("queryOrderChargeItemByOrderId/{orderId}")
    public AjaxResult queryOrderChargeItemByOrderId(@PathVariable String orderId) {
        List<OrderChargeItem> list = this.orderChargeService.queryOrderChargeItemByOrderId(orderId);
        return AjaxResult.success(list);
    }

    /**
     * 订单列表现金支付订单
     */
    @GetMapping("payWithCash/{orderId}")
    public AjaxResult payWithCash(@PathVariable String orderId) {
        OrderCharge orderCharge = this.orderChargeService.queryOrderChargeByOrderId(orderId);
        if (null == orderCharge) {
            return AjaxResult.fail("【" + orderId + "】订单号所在的订单不存在，请核对后再输入");
        }
        if (orderCharge.getOrderStatus().equals(ApiConstant.ORDER_STATUS_1)) {
            return AjaxResult.fail("【" + orderId + "】订单号不是未支付状态，请核对后再输入");
        }
        this.orderChargeService.paySuccess(orderId, null, ApiConstant.PAY_TYPE_0);
        return AjaxResult.success();
    }

    /**
     * 订单列表里再次支付宝支付
     */
    @GetMapping("toPayOrderWithZfb/{orderId}")
    public AjaxResult toPayOrderWithZfb(@PathVariable String orderId) {
        OrderCharge orderCharge = this.orderChargeService.queryOrderChargeByOrderId(orderId);
        if (null == orderCharge) {
            return AjaxResult.fail("【" + orderId + "】订单号所在的订单不存在，请核对后再输入");
        }
        if (orderCharge.getOrderStatus().equals(ApiConstant.ORDER_STATUS_1)) {
            return AjaxResult.fail("【" + orderId + "】订单号不是未支付状态，请核对后再输入");
        }
        //现转支付宝  支付宝转现金的问题
        String outTradeNo = orderId;
        String subject = "Cloud9's医疗管理系统支付平台";
        String totalAmount = orderCharge.getOrderAmount().toString();
        String undiscountableAmount = null;
        String body = "";
        String notifyUrl = AlipayConfig.notifyUrl + outTradeNo;
        Map<String, Object> pay = AlipayService.pay(outTradeNo, subject, totalAmount, undiscountableAmount, body, notifyUrl);
        String qrCode = pay.get("qrCode").toString();
        if (StringUtils.isNotBlank(qrCode)) {
            //创建支付成功
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("allAmount", totalAmount);
            map.put("payUrl", qrCode);
            return AjaxResult.success(map);
        } else {
            return AjaxResult.fail(pay.get("msg").toString());
        }
    }

}
