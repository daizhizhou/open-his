import cn.cloud9.alipay.PayService;

import java.util.Map;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月30日 上午 11:07
 */
public class TradeTest {
    public static void main(String[] args) {
        String outTradeNo = "O-T-N_2022_07_30_14_11_33";
        String subject = "Cloud9's医疗管理平台";
        String totalAmount = "12";
        String undiscountableAmount = "1";
        String body = "药费";
        String notifyUrl = "https://2c7544653v.oicp.vip/alipay-notify/callback/" + outTradeNo;
        final Map<String, Object> res = PayService.pay(outTradeNo, subject, totalAmount, null, body, notifyUrl);

        System.out.println(res);
    }
}
