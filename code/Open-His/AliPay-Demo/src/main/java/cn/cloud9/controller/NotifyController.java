package cn.cloud9.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
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

    /**
     * O-T-N_2022_07_30_01_52_30
     * {bb=10,20,
     * gmt_create=2022-07-30 13:53:22,
     * charset=utf-8,
     * seller_email=xrxgyt4991@sandbox.com,
     * subject=Cloud9's医疗管理平台,
     * sign=KJ8UgT1uHSOcdOyeRO3hTQktJgu7TXNVPrX+NRryngLojanCc6IFTav+IOQKHabhZ8XhmRc9XIrEDvzJvoI+Z6C3U18n3yUzyBpEgfV9POc0pxwAF1VsY1X9ofX1D45vjMdFHusbopBtCaEuIfjfilx73Tu3QbqVApsOe5pil3ADpp7yyM7iiwv3O5nyC/4x5f0I768dwQ+51VOJc5vPjJi5cJV3vsU7NN2LStfAyQHLM6kn1Zy64AutwT1BG8W9up6y8ZhvIObm+Y/LcssPMYRmBt3v26T1+nxnTLSkqyH+RU4/3THn2685afen+FVR1sizk/CMFkZ4p4GoV6vu6Q==,
     * buyer_id=2088622958236601,
     * body=药费,
     * invoice_amount=12.00,
     * notify_id=2022073000222135337036600522576526,
     * fund_bill_list=[{"amount":"12.00","fundChannel":"ALIPAYACCOUNT"}],
     * notify_type=trade_status_sync,
     * sss=100,
     * trade_status=TRADE_SUCCESS,
     * receipt_amount=12.00,
     * buyer_pay_amount=12.00,
     * app_id=2021000119652452,
     * sign_type=RSA2,
     * seller_id=2088621958210752,
     * gmt_payment=2022-07-30 13:53:36,
     * notify_time=2022-07-30 13:53:37,
     * version=1.0,
     * out_trade_no=O-T-N_2022_07_30_01_52_30,
     * total_amount=12.00,
     * trade_no=2022073022001436600501943899,
     * auth_app_id=2021000119652452,
     * buyer_logon_id=xth***@sandbox.com,
     * point_amount=0.00
     * }
     *
     * @param orderId
     * @param request
     */
    @PostMapping("/callback/{orderId}")
    public void callBack(@PathVariable("orderId") String orderId, HttpServletRequest request) {
        System.out.println(orderId);
        final Map<String, String> parameterMap = getParameterMap(request);
        System.out.println(parameterMap);

        // 校验请求是否来自支付宝？
        try {
            /*
            O-T-N_2022_07_30_14_11_33
            {gmt_create=2022-07-30 14:11:45, charset=utf-8, seller_email=xrxgyt4991@sandbox.com, subject=Cloud9's医疗管理平台, sign=j5iB1/ZN843hMZBIiqxv4Pp4B2IZsiNjtP8lW+zV0x2sIZjgLeFWsbFH6pKiIVBUhp9h/gP6asrt8nOg+c6eZDwNDe2aunQM8yygCUGI4J/7M7sdMeeYWVYNb3aFt5brZGK+mNBw+knPKFAae4TSHbgzius59/6SRyw5lVMtwvn8vjOHf2PxnA4edAxToTRaH1jr05eE0d9CB0TOX9wp9eXnLKDSNvSfE1UZRkQPK6/+r2/zyWcqq2vVix9FIpndboq45+d+E8X6w9n0e28tod3LT5EQLKn7mdYisCjeIOKZyYStsZOvlLwoyg8JgE3NNmZEg0VZEVw8N+WAD691EA==, buyer_id=2088622958236601, body=药费, invoice_amount=12.00, notify_id=2022073000222141207036600522574647, fund_bill_list=[{"amount":"12.00","fundChannel":"ALIPAYACCOUNT"}], notify_type=trade_status_sync, trade_status=TRADE_SUCCESS, receipt_amount=12.00, buyer_pay_amount=12.00, app_id=2021000119652452, sign_type=RSA2, seller_id=2088621958210752, gmt_payment=2022-07-30 14:12:06, notify_time=2022-07-30 14:12:07, version=1.0, out_trade_no=O-T-N_2022_07_30_14_11_33, total_amount=12.00, trade_no=2022073022001436600501943900, auth_app_id=2021000119652452, buyer_logon_id=xth***@sandbox.com, point_amount=0.00}
            true
             */
            final boolean rsa2 = AlipaySignature.rsaCheckV1(
                    parameterMap,
                    // 支付宝公钥
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqOQW5KIQhB/4YrcqgFjdJDlh+ntb4TmuDepNdsCKKrIPIcc+A82czF51Q4QzJhK16dwL9zQHAV6DOD5SKJsAgDZL5qInEY/ihy7nKIWFYlH6TPRp4yNump0Js2i7TYeKLd24SAl6lA1TfZVu+zKjMV8vygZc/5b4flLjmVuz/dZWwD1PqQ+5dybKevu02Kx57fhdMaXTkAV4AObMCL6qknmVJupYmQLvHs6nTOKSelHeZdRB+/uyET3he94C5+kV9Vxa/oo9p5lMNisdQrptGiBbzPA0QECXw12f0mwbyI4VzY1F9hyQZHmjFF370TsdIljKwHosxdBBX7K4HkFedQIDAQAB",
                    "utf-8",
                    "RSA2"
            );
            System.out.println(rsa2);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }


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
}
