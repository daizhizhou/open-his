package cn.cloud9.domain;

import cn.cloud9.dto.BaseDTO;
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
 * @date 2022年07月31日 下午 08:13
 */
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class Workload extends BaseDTO {
    private static final long serialVersionUID = 7972797436664465486L;
    private String regId;
    private String userId;
    private String doctorName;
    private BigDecimal regAmount;
    private String patientName;
    private Date visitDate;
}
