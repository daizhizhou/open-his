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

@ApiModel(value = "stock_purchase_item")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "stock_purchase_item")
public class PurchaseItem extends BaseDTO {
    private static final long serialVersionUID = 8465088614126500899L;
    /**
     * 详情ID
     */

    @TableId(value = "item_id", type = IdType.INPUT)
    @ApiModelProperty(value = "详情ID")
    private String itemId;

    /**
     * 生产厂家ID
     */
    @NotBlank(message = "生产厂家不能为空")
    @TableField(value = "producter_id")
    @ApiModelProperty(value = "生产厂家ID")
    private String producterId;

    /**
     * 采购单据ID
     */
    @NotBlank(message = "采购单据ID不能为空")
    @TableField(value = "purchase_id")
    @ApiModelProperty(value = "采购单据ID")
    private String purchaseId;

    /**
     * 采购数量
     */
    @NotNull(message = "采购数量不能为空")
    @TableField(value = "purchase_number")
    @ApiModelProperty(value = "采购数量")
    private Integer purchaseNumber;

    /**
     * 批发价
     */
    @NotNull(message = "批发价不能为空")
    @TableField(value = "trade_price")
    @ApiModelProperty(value = "批发价")
    private BigDecimal tradePrice;

    /**
     * 批发额
     */
    @NotNull(message = "批发额不能为空")
    @TableField(value = "trade_total_amount")
    @ApiModelProperty(value = "批发额")
    private BigDecimal tradeTotalAmount;

    /**
     * 药品生产批次号
     */
    @TableField(value = "batch_number")
    @ApiModelProperty(value = "药品生产批次号")
    private String batchNumber;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 药品ID
     */
    @NotBlank(message = "药品ID不能为空")
    @TableField(value = "medicines_id")
    @ApiModelProperty(value = "药品ID")
    private String medicinesId;

    /**
     * 药品名称
     */
    @NotBlank(message = "药品名称不能为空")
    @TableField(value = "medicines_name")
    @ApiModelProperty(value = "药品名称")
    private String medicinesName;

    /**
     * 药品分类 sys_dict_data表his_medicines_type
     */
    @NotBlank(message = "药品分类不能为空")
    @TableField(value = "medicines_type")
    @ApiModelProperty(value = "药品分类 sys_dict_data表his_medicines_type")
    private String medicinesType;

    /**
     * 处方类型 sys_dict_data表his_prescription_type
     */
    @NotBlank(message = "处方类型不能为空")
    @TableField(value = "prescription_type")
    @ApiModelProperty(value = "处方类型 sys_dict_data表his_prescription_type")
    private String prescriptionType;

    /**
     * 换算量
     */
    @NotNull(message = "换算量不能为空")
    @TableField(value = "`conversion`")
    @ApiModelProperty(value = "换算量")
    private Integer conversion;

    /**
     * 单位（g/条）
     */
    @NotBlank(message = "单位不能为空")
    @TableField(value = "unit")
    @ApiModelProperty(value = "单位（g/条）")
    private String unit;

    /**
     * 关键字
     */
    @NotBlank(message = "关键字不能为空")
    @TableField(value = "keywords")
    @ApiModelProperty(value = "关键字")
    private String keywords;

    public static final String COL_ITEM_ID = "item_id";

    public static final String COL_PRODUCTER_ID = "producter_id";

    public static final String COL_PURCHASE_ID = "purchase_id";

    public static final String COL_PURCHASE_NUMBER = "purchase_number";

    public static final String COL_TRADE_PRICE = "trade_price";

    public static final String COL_TRADE_TOTAL_AMOUNT = "trade_total_amount";

    public static final String COL_BATCH_NUMBER = "batch_number";

    public static final String COL_REMARK = "remark";

    public static final String COL_MEDICINES_ID = "medicines_id";

    public static final String COL_MEDICINES_NAME = "medicines_name";

    public static final String COL_MEDICINES_TYPE = "medicines_type";

    public static final String COL_PRESCRIPTION_TYPE = "prescription_type";

    public static final String COL_CONVERSION = "conversion";

    public static final String COL_UNIT = "unit";

    public static final String COL_KEYWORDS = "keywords";
}