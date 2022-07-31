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
 * @date 2022年07月31日 下午 07:48
 */
@ApiModel(value="com-bjsxt-dto-CheckQueryDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CheckQueryDto extends BaseDTO {
    private static final long serialVersionUID = -4072697540919393122L;
    private Long checkItemId;
    private String patientName;
    private String queryDate;
}
