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
    * 检查费用表
    */
@ApiModel(value="检查费用表")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_check_item")
public class SystemCheckItem extends BaseDTO {
    private static final long serialVersionUID = 3628322117461711478L;
    /**
     * 项目费用ID
     */
    @TableId(value = "check_item_id", type = IdType.INPUT)
    @ApiModelProperty(value="项目费用ID")
    private Long checkItemId;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    @TableField(value = "check_item_name")
    @ApiModelProperty(value="项目名称")
    private String checkItemName;

    /**
     * 关键字【查询用】
     */
    @NotBlank(message = "关键字不能为空")
    @TableField(value = "keywords")
    @ApiModelProperty(value="关键字【查询用】")
    private String keywords;

    /**
     * 项目单价
     */
    @NotNull(message = "项目单价不能为空")
    @TableField(value = "unit_price")
    @ApiModelProperty(value="项目单价")
    private BigDecimal unitPrice;

    /**
     * 项目成本
     */
    @NotNull(message = "项目成本不能为空")
    @TableField(value = "cost")
    @ApiModelProperty(value="项目成本")
    private BigDecimal cost;

    /**
     * 单位
     */
    @NotBlank(message = "项目单位不能为空")
    @TableField(value = "unit")
    @ApiModelProperty(value="单位")
    private String unit;

    /**
     * 项目类别IDsxt_sys_dict_type
     */
    @NotBlank(message = "项目类别不能为空")
    @TableField(value = "type_id")
    @ApiModelProperty(value="项目类别IDsxt_sys_dict_type")
    private String typeId;

    /**
     * 状态0正常1停用 sxt_sys_dict_type
     */
    @NotBlank(message = "项目状态不能为空")
    @TableField(value = "`status`")
    @ApiModelProperty(value="状态0正常1停用 sxt_sys_dict_type")
    private String status;

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

    public static final String COL_CHECK_ITEM_ID = "check_item_id";

    public static final String COL_CHECK_ITEM_NAME = "check_item_name";

    public static final String COL_KEYWORDS = "keywords";

    public static final String COL_UNIT_PRICE = "unit_price";

    public static final String COL_COST = "cost";

    public static final String COL_UNIT = "unit";

    public static final String COL_TYPE_ID = "type_id";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_UPDATE_BY = "update_by";
}