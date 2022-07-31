package cn.cloud9.domain;

import cn.cloud9.dto.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 07:47
 */
@Data
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
@NoArgsConstructor
public class Check extends BaseDTO {
    /**
     * 检查项目ID
     */
    private Long checkItemId;

    /**
     * 检查项目名称
     */
    private String checkItemName;

    /**
     * 项目价格
     */
    private BigDecimal price;

    /**
     * 患者ID
     */
    private String patientId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 是否录入检查结果0 否  1是
     */
    private String resultStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建者
     */
    private String createBy;
    private static final long serialVersionUID = -2372009932035210633L;
}
