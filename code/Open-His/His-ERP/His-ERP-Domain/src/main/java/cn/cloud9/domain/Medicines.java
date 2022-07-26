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
 * 药品信息表
 */
@ApiModel(value = "药品信息表")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "stock_medicines")
public class Medicines extends BaseDTO {
    private static final long serialVersionUID = -4258299363717364355L;
    @TableId(value = "medicines_id", type = IdType.INPUT)
    @ApiModelProperty(value = "")
    private Long medicinesId;

    /**
     * 药品编号
     */
    @NotBlank(message = "药品编号不能为空")
    @TableField(value = "medicines_number")
    @ApiModelProperty(value = "药品编号")
    private String medicinesNumber;

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
     * 处方价格
     */
    @NotNull(message = "处方价格不能为空")
    @TableField(value = "prescription_price")
    @ApiModelProperty(value = "处方价格")
    private BigDecimal prescriptionPrice;

    /**
     * 单位（g/条）
     */
    @TableField(value = "unit")
    @ApiModelProperty(value = "单位（g/条）")
    private String unit;

    /**
     * 换算量
     */
    @TableField(value = "`conversion`")
    @ApiModelProperty(value = "换算量")
    private Integer conversion;

    /**
     * 关键字
     */
    @NotBlank(message = "关键字不能为空")
    @TableField(value = "keywords")
    @ApiModelProperty(value = "关键字")
    private String keywords;

    /**
     * 生产厂家ID
     */
    @NotBlank(message = "生产厂家不能为空")
    @TableField(value = "producter_id")
    @ApiModelProperty(value = "生产厂家ID")
    private String producterId;

    /**
     * 药品状态0正常0停用 sys_dict_data表 sys_normal_disable
     */
    @NotBlank(message = "药品状态0不能为空")
    @TableField(value = "`status`")
    @ApiModelProperty(value = "药品状态0正常0停用 sys_dict_data表 sys_normal_disable")
    private String status;

    /**
     * 库存量
     */
    @NotNull(message = "库存量不能为空")
    @TableField(value = "medicines_stock_num")
    @ApiModelProperty(value = "库存量")
    private Long medicinesStockNum;

    /**
     * 预警值
     */
    @NotNull(message = "库存量不能为空")
    @TableField(value = "medicines_stock_danger_num")
    @ApiModelProperty(value = "预警值")
    private Long medicinesStockDangerNum;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value = "创建者")
    private String createBy;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    public static final String COL_MEDICINES_ID = "medicines_id";

    public static final String COL_MEDICINES_NUMBER = "medicines_number";

    public static final String COL_MEDICINES_NAME = "medicines_name";

    public static final String COL_MEDICINES_TYPE = "medicines_type";

    public static final String COL_PRESCRIPTION_TYPE = "prescription_type";

    public static final String COL_PRESCRIPTION_PRICE = "prescription_price";

    public static final String COL_UNIT = "unit";

    public static final String COL_CONVERSION = "conversion";

    public static final String COL_KEYWORDS = "keywords";

    public static final String COL_PRODUCTER_ID = "producter_id";

    public static final String COL_STATUS = "status";

    public static final String COL_MEDICINES_STOCK_NUM = "medicines_stock_num";

    public static final String COL_MEDICINES_STOCK_DANGER_NUM = "medicines_stock_danger_num";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_UPDATE_BY = "update_by";
}