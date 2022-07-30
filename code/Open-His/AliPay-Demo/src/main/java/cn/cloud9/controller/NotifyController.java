package cn.cloud9.controller;

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
     *  {bb=10,20,
     *  gmt_create=2022-07-30 13:53:22,
     *  charset=utf-8,
     *  seller_email=xrxgyt4991@sandbox.com,
     *  subject=Cloud9's医疗管理平台,
     *  sign=KJ8UgT1uHSOcdOyeRO3hTQktJgu7TXNVPrX+NRryngLojanCc6IFTav+IOQKHabhZ8XhmRc9XIrEDvzJvoI+Z6C3U18n3yUzyBpEgfV9POc0pxwAF1VsY1X9ofX1D45vjMdFHusbopBtCaEuIfjfilx73Tu3QbqVApsOe5pil3ADpp7yyM7iiwv3O5nyC/4x5f0I768dwQ+51VOJc5vPjJi5cJV3vsU7NN2LStfAyQHLM6kn1Zy64AutwT1BG8W9up6y8ZhvIObm+Y/LcssPMYRmBt3v26T1+nxnTLSkqyH+RU4/3THn2685afen+FVR1sizk/CMFkZ4p4GoV6vu6Q==,
     *  buyer_id=2088622958236601,
     *  body=药费,
     *  invoice_amount=12.00,
     *  notify_id=2022073000222135337036600522576526,
     *  fund_bill_list=[{"amount":"12.00","fundChannel":"ALIPAYACCOUNT"}],
     *  notify_type=trade_status_sync,
     *  sss=100,
     *  trade_status=TRADE_SUCCESS,
     *  receipt_amount=12.00,
     *  buyer_pay_amount=12.00,
     *  app_id=2021000119652452,
     *  sign_type=RSA2,
     *  seller_id=2088621958210752,
     *  gmt_payment=2022-07-30 13:53:36,
     *  notify_time=2022-07-30 13:53:37,
     *  version=1.0,
     *  out_trade_no=O-T-N_2022_07_30_01_52_30,
     *  total_amount=12.00,
     *  trade_no=2022073022001436600501943899,
     *  auth_app_id=2021000119652452,
     *  buyer_logon_id=xth***@sandbox.com,
     *  point_amount=0.00
     * }
     * @param orderId
     * @param request
     */
    @PostMapping("/callback/{orderId}")
    public void callBack(@PathVariable("orderId") String orderId, HttpServletRequest request) {
        System.out.println(orderId);
        System.out.println(getParameterMap(request));
    }


    /**
     * 参数获取
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
