package cn.cloud9.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 07:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Drug extends BaseEntity {
    private static final long serialVersionUID = -4313835989275348572L;
    /**
     * 药品id
     */
    private String medicinesId;

    /**
     * 药品名
     */
    private String medicinesName;

    /**
     * 订单数量
     */
    private BigDecimal num;

    /**
     * 单价
     */
    private BigDecimal price;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 创建时间
     */
    private Date createTime;
}
