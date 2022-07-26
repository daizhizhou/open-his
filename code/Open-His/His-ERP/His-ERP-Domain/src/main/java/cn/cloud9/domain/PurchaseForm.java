package cn.cloud9.domain;

import cn.cloud9.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月26日 下午 11:16
 */
@ApiModel(value = "com-bjsxt-dto-PurchaseFromDto")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseForm extends BaseDTO {
    private static final long serialVersionUID = -411288153152785927L;
    private Purchase purchase;
    private List<PurchaseItem> purchaseItemList;
}
