package cn.cloud9.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import cn.cloud9.dto.BaseDTO;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 07:37
 */


@ApiModel(value = "com-bjsxt-dto-DrugQueryDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DrugQueryDto extends BaseDTO {
    private static final long serialVersionUID = -6632753270621731294L;
    private String drugName;
    private String queryDate;
}
