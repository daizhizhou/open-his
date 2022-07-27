package cn.cloud9.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月27日 下午 08:43
 */
@ApiModel(value="cn-cloud9-dto-SchedulingFormDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingForm implements Serializable {
    private static final long serialVersionUID = 7062925374497677425L;

    private SimpleUser simpleUser;

    private String beginDate;

    private List<SchedulingData> data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SchedulingData implements  Serializable{
        private static final long serialVersionUID = -1900328064964987610L;
        private Long userId;
        private Long deptId;
        private String subsectionType; //上午 下午  晚上
        //星期的值班值
        private  List<String> schedulingTypeList;
    }
}
