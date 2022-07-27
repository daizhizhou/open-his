package cn.cloud9.domain;

import cn.cloud9.dto.BaseDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 排班信息表
 */
@ApiModel(value = "排班信息表")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "his_scheduling")
public class Scheduling extends BaseDTO {
    private static final long serialVersionUID = -1290425825478350L;
    /**
     * 医生ID
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value = "医生ID")
    private Long userId;

    /**
     * 科室ID
     */
    @TableField(value = "dept_id")
    @ApiModelProperty(value = "科室ID")
    private Long deptId;

    /**
     * 值班日期
     */
    @TableField(value = "scheduling_day")
    @ApiModelProperty(value = "值班日期")
    private String schedulingDay;

    /**
     * 排班时段1上午  2下午 3晚上 字典表数据翻译
     */
    @TableField(value = "subsection_type")
    @ApiModelProperty(value = "排班时段1上午  2下午 3晚上 字典表数据翻译")
    private String subsectionType;

    /**
     * 排班类型1 门诊 2 急诊 字典表数据翻译
     */
    @TableField(value = "scheduling_type")
    @ApiModelProperty(value = "排班类型1 门诊 2 急诊 字典表数据翻译")
    private String schedulingType;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 创建者
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value = "创建者")
    private String createBy;

    public static final String COL_USER_ID = "user_id";

    public static final String COL_DEPT_ID = "dept_id";

    public static final String COL_SCHEDULING_DAY = "scheduling_day";

    public static final String COL_SUBSECTION_TYPE = "subsection_type";

    public static final String COL_SCHEDULING_TYPE = "scheduling_type";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_CREATE_BY = "create_by";

    //页面传过来的的上一周下一周
    @TableField(exist = false)
    private String queryDate;
    //开始时间
    @TableField(exist = false)
    private String beginDate;
    //结束时间
    @TableField(exist = false)
    private String endDate;

    /**
     * 星期的值班值
     */
    @TableField(exist = false)
    private Collection<String> schedulingTypeList;

    //星期值班的记录 key 是日期
    @JsonIgnore
    @TableField(exist = false)
    private Map<String, String> record;

    public Scheduling(Long userId, Long deptId, String subsectionType, Map<String, String> map) {
        this.userId = userId;
        this.subsectionType = subsectionType;
        this.record = map;
        this.deptId = deptId;
    }

    public Scheduling(
            Long userId,
            Long deptId,
            String schedulingDay,
            String subsectionType,
            String schedulingType,
            Date createTime,
            String createBy) {

        this.userId = userId;
        this.deptId = deptId;
        this.schedulingDay = schedulingDay;
        this.subsectionType = subsectionType;
        this.schedulingType = schedulingType;
        this.createTime = createTime;
        this.createBy = createBy;
    }
}