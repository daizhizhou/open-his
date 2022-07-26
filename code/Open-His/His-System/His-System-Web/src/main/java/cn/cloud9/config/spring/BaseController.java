package cn.cloud9.config.spring;

import cn.cloud9.vo.AjaxResult;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月26日 下午 09:01
 */

@Component
public abstract class BaseController {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;
}
