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

import javax.validation.constraints.NotBlank;

/**
    * 通知公告表
    */
@ApiModel(value="通知公告表")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_notice")
public class SystemNotice extends BaseDTO {
    private static final long serialVersionUID = -3464326058027652598L;
    /**
     * 公告ID
     */
    @TableId(value = "notice_id", type = IdType.INPUT)
    @ApiModelProperty(value="公告ID")
    private Integer noticeId;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value="创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * 备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value="备注")
    private String remark;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @ApiModelProperty(value="更新时间")
    private Date updateTime;

    /**
     * 更新者
     */
    @TableField(value = "update_by")
    @ApiModelProperty(value="更新者")
    private String updateBy;

    /**
     * 公告状态（0正常 1关闭）
     */
    @NotBlank(message = "公告状态不能为空")
    @TableField(value = "`status`")
    @ApiModelProperty(value="公告状态（0正常 1关闭）")
    private String status;

    /**
     * 公告内容
     */
    @NotBlank(message = "公告内容不能为空")
    @TableField(value = "notice_content")
    @ApiModelProperty(value="公告内容")
    private String noticeContent;

    /**
     * 公告类型（1通知 2公告）
     */
    @NotBlank(message = "公告类型不能为空")
    @TableField(value = "notice_type")
    @ApiModelProperty(value="公告类型（1通知 2公告）")
    private String noticeType;

    /**
     * 公告标题
     */
    @NotBlank(message = "公告标题不能为空")
    @TableField(value = "notice_title")
    @ApiModelProperty(value="公告标题")
    private String noticeTitle;

    public static final String COL_NOTICE_ID = "notice_id";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_REMARK = "remark";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_UPDATE_BY = "update_by";

    public static final String COL_STATUS = "status";

    public static final String COL_NOTICE_CONTENT = "notice_content";

    public static final String COL_NOTICE_TYPE = "notice_type";

    public static final String COL_NOTICE_TITLE = "notice_title";
}