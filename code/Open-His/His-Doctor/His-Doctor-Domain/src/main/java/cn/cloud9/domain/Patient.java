package cn.cloud9.domain;

import cn.cloud9.dto.BaseDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
    * 患者信息表
    */
@ApiModel(value="患者信息表")
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "his_patient")
public class Patient extends BaseDTO {
    private static final long serialVersionUID = 9107304182400570167L;
    /**
     * 主键
     */
    @TableId(value = "patient_id", type = IdType.INPUT)
    @ApiModelProperty(value="主键")
    private String patientId;

    /**
     * 患者姓名
     */
    @NotBlank(message = "患者姓名不能为空")
    @TableField(value = "`name`")
    @ApiModelProperty(value="患者姓名")
    private String name;

    /**
     * 患者电话
     */
    @NotBlank(message = "患者电话不能为空")
    @TableField(value = "phone")
    @ApiModelProperty(value="患者电话")
    private String phone;

    /**
     * 患者性别0男1女 2未知字典表 sys_user_sex
     */
    @NotBlank(message = "患者性别不能为空")
    @TableField(value = "sex")
    @ApiModelProperty(value="患者性别0男1女 2未知字典表 sys_user_sex")
    private String sex;

    /**
     * 出生年月
     */
    @NotBlank(message = "患者出生年月不能为空")
    @TableField(value = "birthday")
    @ApiModelProperty(value="出生年月")
    private String birthday;

    /**
     * 身份证号[认证ID]
     */
    @NotBlank(message = "身份证号不能为空")
    @TableField(value = "id_card")
    @ApiModelProperty(value="身份证号[认证ID]")
    private String idCard;

    /**
     * 地址信息
     */
    @TableField(value = "address")
    @ApiModelProperty(value="地址信息")
    private String address;

    /**
     * 过敏信息
     */
    @TableField(value = "allergy_info")
    @ApiModelProperty(value="过敏信息")
    private String allergyInfo;

    /**
     * 是否完善信息，0未完善1已完善 字典表 his_patient_msg_final
     */
    @TableField(value = "is_final")
    @ApiModelProperty(value="是否完善信息，0未完善1已完善 字典表 his_patient_msg_final")
    private String isFinal;

    /**
     * 登录密码
     */
    @JsonIgnore
    @TableField(value = "`password`")
    @ApiModelProperty(value="登录密码")
    private String password;

    /**
     * 微信openid
     */
    @JsonIgnore
    @TableField(value = "openid")
    @ApiModelProperty(value="微信openid")
    private String openid;

    /**
     * 最后登录ip
     */
    @TableField(value = "last_login_ip")
    @ApiModelProperty(value="最后登录ip")
    private String lastLoginIp;

    /**
     * 最后登录时间
     */
    @TableField(value = "last_login_time")
    @ApiModelProperty(value="最后登录时间")
    private Date lastLoginTime;

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

    public static final String COL_PATIENT_ID = "patient_id";

    public static final String COL_NAME = "name";

    public static final String COL_PHONE = "phone";

    public static final String COL_SEX = "sex";

    public static final String COL_BIRTHDAY = "birthday";

    public static final String COL_ID_CARD = "id_card";

    public static final String COL_ADDRESS = "address";

    public static final String COL_ALLERGY_INFO = "allergy_info";

    public static final String COL_IS_FINAL = "is_final";

    public static final String COL_PASSWORD = "password";

    public static final String COL_OPENID = "openid";

    public static final String COL_LAST_LOGIN_IP = "last_login_ip";

    public static final String COL_LAST_LOGIN_TIME = "last_login_time";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";
}