package cn.cloud9.config.shiro;

import cn.cloud9.contants.ApiConstant;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author OnCloud9
 * @description token生成，
 * @project Open-His
 * @date 2022年07月23日 下午 05:10
 */
@Configuration
public class TokenWebSessionManager extends DefaultWebSessionManager {

    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        // 从头里面得到请求TOKEN 如果不存在就生成一个
        String header = WebUtils.toHttp(request).getHeader(ApiConstant.TOKEN);
        if (StringUtils.hasText(header)) return header;
        return UUID.randomUUID().toString() ;
    }
}
