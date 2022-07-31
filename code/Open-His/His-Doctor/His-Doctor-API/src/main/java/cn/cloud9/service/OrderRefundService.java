package cn.cloud9.service;

import cn.cloud9.domain.OrderRefund;
import cn.cloud9.domain.OrderRefundItem;
import cn.cloud9.domain.form.OrderRefundForm;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface OrderRefundService extends IService<OrderRefund>{

    /**
     * 保存退费单
     * @param orderBackfeeFormDto
     */
    void saveOrderAndItems(OrderRefundForm orderBackfeeFormDto);

    /**
     * 退费成功之后更改状态
     * @param backId
     * @param backPlatformId
     * @param payType0
     */
    void backSuccess(String backId, String backPlatformId, String payType0);
    /**
     * 分页查询所有退费单
     * @param orderBackfeeDto
     * @return
     */
    DataGridViewVO queryAllOrderBackfeeForPage(OrderRefund orderBackfeeDto);

    /**
     * 根据退费单的ID查询退费详情信息
     * @param backId
     * @return
     */
    List<OrderRefundItem> queryrderBackfeeItemByBackId(String backId);

}
