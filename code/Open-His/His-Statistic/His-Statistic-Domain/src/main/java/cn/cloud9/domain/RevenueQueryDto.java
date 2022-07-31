package cn.cloud9.domain;

import cn.cloud9.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 07:23
 */
@ApiModel(value = "cn-cloud9-dto-RevenueQueryDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RevenueQueryDto extends BaseDTO {
    private static final long serialVersionUID = 4066596653639693259L;
    private String queryDate;
}
