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

@ApiModel(value="stock_purchase")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "stock_purchase")
public class Purchase extends BaseDTO {
    private static final long serialVersionUID = 5903706289069916201L;
    /**
     * 制单号ID 全局ID雪花算法
     */
    @NotBlank(message = "制单号ID不能为空")
    @TableId(value = "purchase_id", type = IdType.INPUT)
    @ApiModelProperty(value="制单号ID 全局ID雪花算法")
    private String purchaseId;

    /**
     * 供应商ID
     */
    @NotBlank(message = "供应商ID不能为空")
    @TableField(value = "provider_id")
    @ApiModelProperty(value="供应商ID")
    private String providerId;

    /**
     * 采购批发总额
     */
    @NotNull(message = "采购批发总额不能为空")
    @TableField(value = "purchase_trade_total_amount")
    @ApiModelProperty(value="采购批发总额")
    private BigDecimal purchaseTradeTotalAmount;

    /**
     * 单据状态； 1未提交2待审核 3审核通过 4审核失败 5作废 6入库成功 字典表 his_order_status
     */
    @TableField(value = "`status`")
    @ApiModelProperty(value="单据状态； 1未提交2待审核 3审核通过 4审核失败 5作废 6入库成功 字典表 his_order_status")
    private String status;

    /**
     * 申请人ID
     */
    @TableField(value = "apply_user_id")
    @ApiModelProperty(value="申请人ID")
    private Long applyUserId;

    /**
     * 申请人名称
     */
    @TableField(value = "apply_user_name")
    @ApiModelProperty(value="申请人名称")
    private String applyUserName;

    /**
     * 入库操作人
     */
    @TableField(value = "storage_opt_user")
    @ApiModelProperty(value="入库操作人")
    private String storageOptUser;

    /**
     * 入库操作时间
     */
    @TableField(value = "storage_opt_time")
    @ApiModelProperty(value="入库操作时间")
    private Date storageOptTime;

    /**
     * 审核信息
     */
    @TableField(value = "audit_msg")
    @ApiModelProperty(value="审核信息")
    private String auditMsg;

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

    public static final String COL_PURCHASE_ID = "purchase_id";

    public static final String COL_PROVIDER_ID = "provider_id";

    public static final String COL_PURCHASE_TRADE_TOTAL_AMOUNT = "purchase_trade_total_amount";

    public static final String COL_STATUS = "status";

    public static final String COL_APPLY_USER_ID = "apply_user_id";

    public static final String COL_APPLY_USER_NAME = "apply_user_name";

    public static final String COL_STORAGE_OPT_USER = "storage_opt_user";

    public static final String COL_STORAGE_OPT_TIME = "storage_opt_time";

    public static final String COL_AUDIT_MSG = "audit_msg";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_UPDATE_BY = "update_by";
}