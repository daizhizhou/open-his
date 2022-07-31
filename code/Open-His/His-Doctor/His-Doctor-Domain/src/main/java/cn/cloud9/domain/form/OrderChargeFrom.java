package cn.cloud9.domain.form;

import cn.cloud9.domain.OrderCharge;
import cn.cloud9.domain.OrderChargeItem;
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
 * @date 2022年07月31日 上午 08:35
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="cn-cloud9-dto-OrderChargeFrom")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderChargeFrom extends BaseDTO {
    private static final long serialVersionUID = -259697179861614470L;

    //主订单
    private OrderCharge orderChargeDto;

    //订单详情
    @NotEmpty(message = "订单详情不能为空")
    private List<OrderChargeItem> orderChargeItemDtoList;
}
