import cn.cloud9.alipay.PayService;
import org.junit.Test;

import java.util.Map;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月30日 上午 11:07
 */

public class TradeTest {


    @Test
    public void tradePay2() {
        String outTradeNo = "OD123124124123412312312";
        String subject = "Cloud9's医疗管理系统支付平台";
        String totalAmount = "100";
        String unDiscountableAmount = null;
        String body = "买药用的";
        String notifyUrl = "https://2c7544653v.oicp.vip/alipay-notify/callback/" + outTradeNo;
        Map<String, Object> pay = PayService.pay(outTradeNo, subject, totalAmount, unDiscountableAmount, body, notifyUrl);
        System.out.println(pay);
    }


    @Test
    public void tradeRefund2() {
        String outTradeNo = "OD123124124123412312312";
        String tradeNo = "2022073022001436600501944072";
        String refundAmount = "30";
        String refundReason = "不想要了";
        PayService.payBack(outTradeNo, tradeNo, refundAmount, refundReason, "BK12345679");
    }
}
