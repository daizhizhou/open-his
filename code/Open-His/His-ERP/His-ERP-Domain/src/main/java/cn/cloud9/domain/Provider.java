package cn.cloud9.domain;

import cn.cloud9.dto.BaseDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 供应商信息表
 */
@ApiModel(value = "供应商信息表")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "stock_provider")
public class Provider extends BaseDTO {
    private static final long serialVersionUID = 1758684954536853834L;
    /**
     * 供应商id
     */
    @TableId(value = "provider_id", type = IdType.INPUT)
    @ApiModelProperty(value = "供应商id")
    private Long providerId;

    /**
     * 供应商地址
     */
    @TableField(value = "provider_address")
    @ApiModelProperty(value = "供应商地址")
    private String providerAddress;

    /**
     * 供应商名称
     */
    @NotBlank(message = "供应商名称不能为空")
    @TableField(value = "provider_name")
    @ApiModelProperty(value = "供应商名称")
    private String providerName;

    /**
     * 联系人名称
     */
    @NotBlank(message = "联系人名称不能为空")
    @TableField(value = "contact_name")
    @ApiModelProperty(value = "联系人名称")
    private String contactName;

    /**
     * 联系人手机
     */
    @NotBlank(message = "联系人手机不能为空")
    @TableField(value = "contact_mobile")
    @ApiModelProperty(value = "联系人手机")
    private String contactMobile;

    /**
     * 联系人电话
     */
    @TableField(value = "contact_tel")
    @ApiModelProperty(value = "联系人电话")
    private String contactTel;

    /**
     * 银行账号
     */
    @NotBlank(message = "银行账号不能为空")
    @TableField(value = "bank_account")
    @ApiModelProperty(value = "银行账号")
    private String bankAccount;

    /**
     * 状态（0正常 1停用）sys_normal_disable
     */
    @NotBlank(message = "状态不能为空")
    @TableField(value = "`status`")
    @ApiModelProperty(value = "状态（0正常 1停用）sys_normal_disable")
    private String status;

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

    public static final String COL_PROVIDER_ID = "provider_id";

    public static final String COL_PROVIDER_ADDRESS = "provider_address";

    public static final String COL_PROVIDER_NAME = "provider_name";

    public static final String COL_CONTACT_NAME = "contact_name";

    public static final String COL_CONTACT_MOBILE = "contact_mobile";

    public static final String COL_CONTACT_TEL = "contact_tel";

    public static final String COL_BANK_ACCOUNT = "bank_account";

    public static final String COL_STATUS = "status";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_UPDATE_BY = "update_by";
}