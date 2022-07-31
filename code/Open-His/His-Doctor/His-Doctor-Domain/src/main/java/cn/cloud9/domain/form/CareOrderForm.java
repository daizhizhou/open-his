package cn.cloud9.domain.form;

import cn.cloud9.domain.BaseEntity;
import cn.cloud9.domain.CareOrder;
import cn.cloud9.domain.CareOrderItem;
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
 * @date 2022年07月29日 下午 09:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="com-bjsxt-dto-CareOrderFormDto")
@AllArgsConstructor
@NoArgsConstructor
public class CareOrderForm extends BaseEntity {
    private static final long serialVersionUID = 6339256899483739902L;
    //处方
    private CareOrder careOrder;
    //处方详情
    @NotEmpty(message = "处方详情不能为空")
    private List<CareOrderItem> careOrderItems;

}
