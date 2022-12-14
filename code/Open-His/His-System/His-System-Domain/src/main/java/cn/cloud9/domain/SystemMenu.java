package cn.cloud9.domain;

import cn.cloud9.dto.BaseDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
    * 菜单权限表
    */
@ApiModel(value="菜单权限表")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_menu")
public class SystemMenu extends BaseDTO {
    private static final long serialVersionUID = 2290391493413382241L;
    /**
     * 菜单ID
     */
    @TableId(value = "menu_id", type = IdType.INPUT)
    @ApiModelProperty(value="菜单ID")
    private Long menuId;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value="创建者")
    private String createBy;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value="备注")
    private String remark;

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    @NotNull(message = "菜单类型不能为空")
    @TableField(value = "menu_type")
    @ApiModelProperty(value="菜单类型（M目录 C菜单 F按钮）")
    private String menuType;

    /**
     * 菜单名称
     */
    @NotNull(message = "菜单名称不能为空")
    @TableField(value = "menu_name")
    @ApiModelProperty(value="菜单名称")
    private String menuName;

    /**
     * 父节点ID集合
     */
    @TableField(value = "parent_ids")
    @ApiModelProperty(value="父节点ID集合")
    private String parentIds;

    /**
     * 父菜单ID
     */
    @TableField(value = "parent_id")
    @ApiModelProperty(value="父菜单ID")
    private Long parentId;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    @ApiModelProperty(value="更新者")
    private String updateBy;

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
     * 权限标识
     */
    @TableField(value = "percode")
    @ApiModelProperty(value="权限标识")
    private String percode;

    /**
     * 路由地址
     */
    @TableField(value = "`path`")
    @ApiModelProperty(value="路由地址")
    private String path;

    /**
     * 菜单状态（0正常 1停用）
     */
    @NotNull(message = "菜单状态不能为空")
    @TableField(value = "`status`")
    @ApiModelProperty(value="菜单状态（0正常 1停用）")
    private String status;

    public static final String COL_MENU_ID = "menu_id";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_REMARK = "remark";

    public static final String COL_MENU_TYPE = "menu_type";

    public static final String COL_MENU_NAME = "menu_name";

    public static final String COL_PARENT_IDS = "parent_ids";

    public static final String COL_PARENT_ID = "parent_id";

    public static final String COL_UPDATE_BY = "update_by";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_PERCODE = "percode";

    public static final String COL_PATH = "path";

    public static final String COL_STATUS = "status";
}