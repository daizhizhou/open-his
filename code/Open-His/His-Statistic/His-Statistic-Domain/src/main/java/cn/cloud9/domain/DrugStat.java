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
 * @date 2022年07月31日 下午 07:39
 */


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DrugStat extends BaseDTO {
    private static final long serialVersionUID = 8276837338977284683L;
    /**
     * 药品id
     */
    private String medicinesId;

    /**
     * 药品名
     */
    private String medicinesName;

    /**
     * 金额
     */
    private BigDecimal totalAmount;

    /**
     * 销售数量
     */
    private BigDecimal count;
}
