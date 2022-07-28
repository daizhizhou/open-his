package cn.cloud9.dto;

import cn.cloud9.domain.SimpleUser;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月23日 下午 02:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class
BaseDTO implements Serializable {
    private static final long serialVersionUID = -5168171386253550854L;

    /**
     * 页码 默认1
     */
    @TableField(exist = false)
    private Integer pageNum = 1;

    /**
     * 每页显示条数 默认10
     */
    @TableField(exist = false)
    private Integer pageSize = 10;

    /**
     * 当前操作对象
     */
    @TableField(exist = false)
    private SimpleUser simpleUser;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date beginTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date endTime;
}
