package cn.cloud9.service;

import cn.cloud9.domain.OrderCharge;
import cn.cloud9.domain.form.OrderChargeFrom;
import cn.cloud9.domain.OrderChargeItem;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface OrderChargeService extends IService<OrderCharge>{

    /**
     * 保存收费订单及详情
     * @param orderChargeFromDto
     */
    void saveOrderAndItems(OrderChargeFrom orderChargeFromDto);

    /**
     * 支付成功之后更新订单状态
     * @param orderId
     * @param payPlatformId 平台交易ID 如果是现金，则为空
     */
    void paySuccess(String orderId, String payPlatformId, String payType);

    /**
     * 根据订单ID查询订单信息
     * @param orderId
     * @return
     */
    OrderCharge queryOrderChargeByOrderId(String orderId);

    /**
     * 分页查询所有收费单
     * @param orderChargeDto
     * @return
     */
    DataGridViewVO queryAllOrderChargeForPage(OrderCharge orderChargeDto);

    /**
     * 据收费单的ID查询收费详情信息
     * @param orderId
     * @return
     */
    List<OrderChargeItem> queryOrderChargeItemByOrderId(String orderId);

    /**
     * 根据详情ID查询收费订单详情
     * @param itemId
     * @return
     */
    OrderChargeItem queryOrderChargeItemByItemId(String itemId);
}
