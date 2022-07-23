package cn.cloud9.config.spring;

import cn.cloud9.vo.AjaxResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月23日 下午 06:03
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 当系统出现MethodArgumentNotValidException这个异常时，会调用下面的方法
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public AjaxResult jsonErrorHandler(MethodArgumentNotValidException e) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();

        allErrors.forEach(allError -> list.add(new HashMap<String, Object>() {
            private static final long serialVersionUID = 2317044682730439888L;
            {
                this.put("defaultMessage", allError.getDefaultMessage());
                this.put("objectName", allError.getObjectName());
                //注意，这里面拿到具体的某一个属性
                FieldError fieldError = (FieldError) allError;
                this.put("field", fieldError.getField());
            }
        }));
        return AjaxResult.fail("后端数据校验异常", list);
    }
}
