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

@ApiModel(value="挂号(项)费用表")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_registered_item")
public class SystemRegistItem extends BaseDTO {
    private static final long serialVersionUID = 5907447487124820268L;
    /**
     * 挂号项ID
     */
    @TableId(value = "reg_item_id", type = IdType.INPUT)
    @ApiModelProperty(value="挂号项ID")
    private Long regItemId;

    /**
     * 金额
     */
    @NotNull(message = "金额不能为空")
    @TableField(value = "reg_item_fee")
    @ApiModelProperty(value="金额")
    private BigDecimal regItemFee;

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

    /**
     * 状态（0正常 1停用）
     */
    @NotBlank(message = "状态不能为空")
    @TableField(value = "`status`")
    @ApiModelProperty(value="状态（0正常 1停用）")
    private String status;

    /**
     * 挂号项目名称
     */
    @NotBlank(message = "挂号项目名称不能为空")
    @TableField(value = "reg_item_name")
    @ApiModelProperty(value="挂号项目名称")
    private String regItemName;

    public static final String COL_REG_ITEM_ID = "reg_item_id";

    public static final String COL_REG_ITEM_FEE = "reg_item_fee";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_UPDATE_BY = "update_by";

    public static final String COL_STATUS = "status";

    public static final String COL_REG_ITEM_NAME = "reg_item_name";
}