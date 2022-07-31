package cn.cloud9.domain;

import cn.cloud9.dto.BaseDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
    * 支付订单详情表
    */
@ApiModel(value="支付订单详情表")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "his_order_charge_item")
public class OrderChargeItem extends BaseDTO {
    private static final long serialVersionUID = 3274502633403881957L;
    /**
     * 详情ID和his_care_order_*表里面的ID一样
     */
    @NotBlank(message = "详情ID不能为空")
    @TableId(value = "item_id", type = IdType.INPUT)
    @ApiModelProperty(value="详情ID和his_care_order_*表里面的ID一样")
    private String itemId;

    /**
     * 处方ID【备用】
     */
    @NotBlank(message = "处方ID不能为空")
    @TableField(value = "co_id")
    @ApiModelProperty(value="处方ID【备用】")
    private String coId;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    @TableField(value = "item_name")
    @ApiModelProperty(value="项目名称")
    private String itemName;

    /**
     * 项目价格
     */
    @NotBlank(message = "项目价格不能为空")
    @TableField(value = "item_price")
    @ApiModelProperty(value="项目价格")
    private BigDecimal itemPrice;

    /**
     * 项目数量
     */
    @NotNull(message = "项目数量不能为空")
    @TableField(value = "item_num")
    @ApiModelProperty(value="项目数量")
    private Integer itemNum;

    /**
     * 小计
     */
    @NotNull(message = "项目总价不能为空")
    @TableField(value = "item_amount")
    @ApiModelProperty(value="小计")
    private Long itemAmount;

    /**
     * 订单IDhis_oder_charge主键
     */
    @TableField(value = "order_id")
    @ApiModelProperty(value="订单IDhis_oder_charge主键")
    private String orderId;

    /**
     * 项目类型0药品  还是1检查项
     */
    @NotBlank(message = "项目类型不能为空")
    @TableField(value = "item_type")
    @ApiModelProperty(value="项目类型0药品  还是1检查项")
    private String itemType;

    /**
     * 状态，0未支付，1已支付，2，已退费  3，已完成 字典表 his_order_details_status
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value="状态，0未支付，1已支付，2，已退费  3，已完成 字典表 his_order_details_status")
    private String status;

    public static final String COL_ITEM_ID = "item_id";

    public static final String COL_CO_ID = "co_id";

    public static final String COL_ITEM_NAME = "item_name";

    public static final String COL_ITEM_PRICE = "item_price";

    public static final String COL_ITEM_NUM = "item_num";

    public static final String COL_ITEM_AMOUNT = "item_amount";

    public static final String COL_ORDER_ID = "order_id";

    public static final String COL_ITEM_TYPE = "item_type";

    public static final String COL_STATUS = "status";
}