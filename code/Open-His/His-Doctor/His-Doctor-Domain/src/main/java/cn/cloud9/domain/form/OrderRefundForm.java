package cn.cloud9.domain.form;

import cn.cloud9.domain.OrderRefund;
import cn.cloud9.domain.OrderRefundItem;
import cn.cloud9.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 上午 09:11
 */
@ApiModel(value="cn-cloud9-dto-OrderBackfeeFormDto")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderRefundForm extends BaseDTO {
    private static final long serialVersionUID = -5208813684035945939L;
    //主订单
    private OrderRefund orderBackfeeDto;

    //订单详情
    @NotEmpty(message = "退费订单详情不能为空")
    private List<OrderRefundItem> orderBackfeeItemDtoList;
}
