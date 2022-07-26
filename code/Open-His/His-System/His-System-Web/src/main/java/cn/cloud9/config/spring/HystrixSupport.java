package cn.cloud9.config.spring;

import cn.cloud9.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;

@DefaultProperties(defaultFallback = HystrixSupport.FALL_BACK_METHOD)
public abstract class HystrixSupport extends BaseController {
    public static final String FALL_BACK_METHOD = "serviceUnavailableFallBack";
    /**
     * dubbo服务提供者出现服务不可用或者异常，将调用该方法返回给此dubbo消费者
     * @return
     */
    protected AjaxResult serviceUnavailableFallBack() {
        return AjaxResult.toAjax(-1);
    }
}
