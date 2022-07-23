package cn.cloud9.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author OnCloud9
 * @description 表格数据传输对象
 * @project Open-His
 * @date 2022年07月23日 下午 09:59
 */
@ApiModel(value="cn-cloud9-vo-DataGridView")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataGridViewVO implements Serializable {
    private static final long serialVersionUID = -4708191325133087170L;
    private Long total;
    private List<?> data;
}
