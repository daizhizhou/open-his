package cn.cloud9.config.spring.aspect;

import cn.cloud9.aspect.annotation.SystemLog;
import cn.cloud9.aspect.enums.BusinessStatus;
import cn.cloud9.domain.SystemOperateLog;
import cn.cloud9.domain.SystemUser;
import cn.cloud9.service.SystemOperateLogService;
import cn.cloud9.util.ServletUtil;
import cn.cloud9.util.ShiroSecurityUtil;
import cn.cloud9.utils.AddressUtil;
import cn.cloud9.utils.CheckUtil;
import cn.cloud9.utils.IpUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月24日 上午 11:20
 */
@Aspect
@Component
@Log4j2
public class OperateLogAspect {

    @Resource
    private SystemOperateLogService systemOperateLogService;

    // 配置织入点
    @Pointcut("@annotation(cn.cloud9.aspect.annotation.SystemLog)")
    public void logPointCut() {

    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获得注解
            SystemLog controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) return;

            // 获取当前的用户
            SystemUser loginUser = ShiroSecurityUtil.getCurrentUser();

            // *========数据库日志=========*//
            SystemOperateLog operLog = new SystemOperateLog();
            operLog.setStatus(String.valueOf(BusinessStatus.SUCCESS.ordinal()));
            // 请求的地址
            String ip = IpUtil.getIpAddr(ServletUtil.getRequest());
            operLog.setOperIp(ip);
            String address = AddressUtil.getRealAddressByIP(ip);
            operLog.setOperLocation(address);
            // 返回参数
            operLog.setJsonResult(JSON.toJSONString(jsonResult));
            operLog.setOperUrl(ServletUtil.getRequest().getRequestURI());
            operLog.setOperName(CheckUtil.isEmpty(loginUser) ? "" : loginUser.getUserName());

            if (!CheckUtil.isEmpty(e)) {
                operLog.setStatus(String.valueOf(BusinessStatus.FAIL.ordinal()));
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operLog.setRequestMethod(ServletUtil.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operLog);
            //设置操作时间
            operLog.setOperTime(new Date());
            // 保存数据库
            systemOperateLogService.insertOperateLog(operLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param operLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, SystemLog log, SystemOperateLog operLog) throws Exception {
        // 设置action动作
        operLog.setBusinessType(String.valueOf(log.businessType().ordinal()));
        // 设置标题
        operLog.setTitle(log.title());
        // 设置操作人类别
        operLog.setOperatorType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operLog);
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, SystemOperateLog operLog) throws Exception {
        String requestMethod = operLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            operLog.setOperParam(StringUtils.substring(params, 0, 2000));
        } else {
            Map<?, ?> paramsMap = (Map<?, ?>) ServletUtil.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            operLog.setOperParam(StringUtils.substring(paramsMap.toString(), 0, 2000));
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private SystemLog getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return CheckUtil.isEmpty(method) ? null : method.getAnnotation(SystemLog.class);
    }

    /**
     * 参数拼装
     *
     * String params = "";
     * if (paramsArray != null && paramsArray.length > 0) {
     *     for (int i = 0; i < paramsArray.length; i++) {
     *         if (!isFilterObject(paramsArray[i])) {
     *             Object jsonObj = JSON.toJSON(paramsArray[i]);
     *             params += jsonObj.toString() + " ";
     *         }
     *     }
     * }
     * return params.trim();
     */
    private String argsArrayToString(Object[] paramsArray) {
        if (CheckUtil.isEmptyArray(paramsArray)) return "";
        final List<Object> paramList = Arrays.asList(paramsArray);
        final String collect = paramList.stream()
            .filter(param -> !isFilterObject(param))
            .map(JSON::toJSONString)
            .collect(Collectors.joining(" "));
        return collect.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile
            || o instanceof HttpServletRequest
            || o instanceof HttpServletResponse;
    }

}
