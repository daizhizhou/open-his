package cn.cloud9.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 菜单树对象
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月23日 下午 02:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MenuTreeVO implements Serializable {
    private static final long serialVersionUID = -8606997395788042878L;

    private String id;
    private String serPath;
    private boolean show = true;

    public MenuTreeVO(String id, String serPath) {
        this.id = id;
        this.serPath = serPath;
    }
}
