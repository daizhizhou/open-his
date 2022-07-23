package cn.cloud9.vo;

import cn.cloud9.domain.SystemUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月23日 下午 05:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ActiveUser implements Serializable {
    private static final long serialVersionUID = -926237470242303316L;
    private SystemUser systemUser;
    private List<String> roles = Collections.EMPTY_LIST; //角色
    private List<String> permissions = Collections.EMPTY_LIST;  //权限
}
