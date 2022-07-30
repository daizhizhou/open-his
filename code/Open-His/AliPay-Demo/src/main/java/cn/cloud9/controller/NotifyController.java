package cn.cloud9.controller;

import cn.cloud9.alipay.AlipayConfig;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月30日 下午 12:31
 */
@RestController
@RequestMapping("/alipay-notify")
public class NotifyController {
    private final static Log LOG = LogFactory.getLog("trade_precreate");

    /**
     * 参数获取
     *
     * @param request
     * @return
     */
    private Map<String, String> getParameterMap(HttpServletRequest request) {
        final HashMap<String, String> map = new HashMap<>();
        final Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            final String name = parameterNames.nextElement();
            final String[] parameterValues = request.getParameterValues(name);
            if (1 == parameterValues.length) map.put(name, parameterValues[0]);
            else if (1 < parameterValues.length) map.put(name, StringUtils.join(parameterValues, ","));
        }
        return map;
    }


    /**
     * 接口必须是一个POST和GET都支持的请求
     * @param orderId
     * @param request
     */
    @RequestMapping("callback/{orderId}")
    public void callback(@PathVariable String orderId, HttpServletRequest request) {
        Map<String, String> parameterMap = this.getParameterMap(request);
        String trade_status = parameterMap.get("trade_status");
        if ("TRADE_SUCCESS".equals(trade_status)) {
            try {
                //验证是否为支付宝发来的信息
                boolean singVerified = AlipaySignature.rsaCheckV1(parameterMap, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
                System.out.println(singVerified);//只有支付宝调用我们系统的接口才能去更新系统订单状态
                LOG.info("验证签名结果{}:" + singVerified);
                String refund_fee = parameterMap.get("refund_fee");
                if (StringUtils.isNotBlank(refund_fee)) {
                    //说明是退费
                    System.out.println("退费成功：退费的子订单ID为：" + parameterMap.get("out_biz_no"));
                    //更新订单状态
                } else {
                    //说明支付
                    System.out.println("收费成功，平台ID" + parameterMap.get("trade_no"));
                }
                System.out.println(JSON.toJSON(parameterMap));
            } catch (AlipayApiException e) {
                e.printStackTrace();
                LOG.error(orderId + "验证签名出现异常");
            }
        } else if ("WAIT_BUYER_PAY".equals(trade_status)) {
            System.out.println("二维码已扫描，等待支付");
        }
    }
}
