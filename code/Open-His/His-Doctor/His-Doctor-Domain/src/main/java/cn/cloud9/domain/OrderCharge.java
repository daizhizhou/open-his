package cn.cloud9.domain;

import cn.cloud9.dto.BaseDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
    * 收费表
    */
@ApiModel(value="收费表")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "his_order_charge")
public class OrderCharge extends BaseDTO {
    private static final long serialVersionUID = -8154846508883794405L;
    /**
     * 收费ID
     */
    @TableId(value = "order_id", type = IdType.INPUT)
    @ApiModelProperty(value="收费ID")
    private String orderId;

    /**
     * 总费用
     */
    @NotNull(message = "总费用不能为空")
    @TableField(value = "order_amount")
    @ApiModelProperty(value="总费用")
    private BigDecimal orderAmount;

    /**
     * 病历ID
     */
    @NotBlank(message = "病历ID不能为空")
    @TableField(value = "ch_id")
    @ApiModelProperty(value="病历ID")
    private String chId;

    /**
     * 挂号单
     */
    @NotBlank(message = "挂号单不能为空")
    @TableField(value = "reg_id")
    @ApiModelProperty(value="挂号单")
    private String regId;

    /**
     * 患者名称
     */
    @NotBlank(message = "患者名称不能为空")
    @TableField(value = "patient_name")
    @ApiModelProperty(value="患者名称")
    private String patientName;

    /**
     * 订单状态0未支付  1 支付成功  2支付超时 3支付失败 字典表his_order_charge_status
     */
    @TableField(value = "order_status")
    @ApiModelProperty(value="订单状态0未支付  1 支付成功  2支付超时 3支付失败 字典表his_order_charge_status")
    private String orderStatus;

    /**
     * 支付ID
     */
    @TableField(value = "pay_platform_id")
    @ApiModelProperty(value="支付ID")
    private String payPlatformId;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time")
    @ApiModelProperty(value="支付时间")
    private Date payTime;

    /**
     * 支付类型0现金1支付宝  字典表	his_pay_type_status
     */
    @TableField(value = "pay_type")
    @ApiModelProperty(value="支付类型0现金1支付宝  字典表	his_pay_type_status")
    private String payType;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value="更新时间")
    private Date updateTime;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value="创建者")
    private String createBy;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    @ApiModelProperty(value="更新者")
    private String updateBy;

    public static final String COL_ORDER_ID = "order_id";

    public static final String COL_ORDER_AMOUNT = "order_amount";

    public static final String COL_CH_ID = "ch_id";

    public static final String COL_REG_ID = "reg_id";

    public static final String COL_PATIENT_NAME = "patient_name";

    public static final String COL_ORDER_STATUS = "order_status";

    public static final String COL_PAY_PLATFORM_ID = "pay_platform_id";

    public static final String COL_PAY_TIME = "pay_time";

    public static final String COL_PAY_TYPE = "pay_type";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_UPDATE_BY = "update_by";
}