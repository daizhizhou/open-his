package cn.cloud9.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 08:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class WorkloadStat extends BaseEntity {
    private static final long serialVersionUID = 3711076007265578884L;
    private String userId;
    private String doctorName;
    private BigDecimal totalAmount;
    private Long count;
}
