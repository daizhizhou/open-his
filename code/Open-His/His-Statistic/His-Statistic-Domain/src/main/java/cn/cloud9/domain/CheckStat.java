package cn.cloud9.domain;

import cn.cloud9.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 07:47
 */
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class CheckStat extends BaseDTO {
    private static final long serialVersionUID = -1926298213702341182L;
    /**
     * 检查项目ID
     */
    private Long checkItemId;

    /**
     * 检查项目名称
     */
    private String checkItemName;

    /**
     * 项目总价
     */
    private BigDecimal totalAmount;

    /**
     * 检查次数
     */
    private Integer count;
}
