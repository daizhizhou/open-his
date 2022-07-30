package cn.cloud9.alipay;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月30日 上午 10:45
 */
public class PayService {

    private final static Log LOG = LogFactory.getLog("trade_precreate");
    private static AlipayTradeService alipayTradeService;

    static {
        Configs.init("zfbinfo.properties");
        alipayTradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    // 商户操作员编号，添加此参数可以为商户操作员做销售统计
    private final static String operator = "test_operator_id";

    // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
    private final static String store_id = "test_store_id";

    // 支付超时，定义为120分钟
    private final static String time_out_express = "120m";

    public static Map<String, Object> pay(
            // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
            // 需保证商户系统端不能重复，建议通过数据库sequence生成，
            String outTradeNo,

            // (必填) 订单标题，粗略描述用户的支付目的。如“喜士多（浦东店）消费”
            String subject,

            // (必填) 订单总金额，单位为元，不能超过1亿元
            // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
            String totalAmount,

            // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
            // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
            String undiscountableAmount,

            // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
            String body,

            String url
    ) {
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject)
                .setTotalAmount(totalAmount)
                .setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount)
                .setBody(body)
                .setOperatorId(operator)
                .setStoreId(store_id)
                // .setSellerId(null)
                // .setExtendParams(extendParams)
                // .setGoodsDetailList(goodsDetailList);
                .setTimeoutExpress(time_out_express)
                .setNotifyUrl(url); //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置

        // 发起支付
        AlipayF2FPrecreateResult result = alipayTradeService.tradePrecreate(builder);

        final Map<String, Object> map = new HashMap<>();
        String message = null;
        Integer code = null;
        switch (result.getTradeStatus()) {
            case SUCCESS:
                LOG.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse res = result.getResponse();
                // 得到支付码url
                final String qrCode = res.getQrCode();

                // ZxingUtils.getQRCodeImge(qrCode, 256, filePath);
                map.put("qrCode", qrCode);
                message = "下单成功！";
                code = 200;
                break;

            case FAILED:
                LOG.error("支付宝预下单失败!!!");
                message = "下单失败！";
                code = 500;
                break;

            case UNKNOWN:
                LOG.error("系统异常，预下单状态未知!!!");
                message = "预下单状态未知！";
                code = 404;
                break;

            default:
                LOG.error("不支持的交易状态，交易返回异常!!!");
                message = "不支持的交易状态！";
                code = 404;
                break;
        }
        map.put("message", message);
        map.put("code", code);
        return map;
    }

}
