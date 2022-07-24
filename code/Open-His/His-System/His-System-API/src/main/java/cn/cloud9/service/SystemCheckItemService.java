package cn.cloud9.service;

import cn.cloud9.domain.SystemCheckItem;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SystemCheckItemService extends IService<SystemCheckItem>{
    /**
     * 分页查询
     *
     * @param checkItemDto
     * @return
     */
    DataGridViewVO listCheckItemPage(SystemCheckItem checkItemDto);

    /**
     * 根据ID查询
     *
     * @param checkItemId
     * @return
     */
    SystemCheckItem getOne(Long checkItemId);

    /**
     * 添加
     *
     * @param checkItemDto
     * @return
     */
    int addCheckItem(SystemCheckItem checkItemDto);

    /**
     * 修改
     *
     * @param checkItemDto
     * @return
     */
    int updateCheckItem(SystemCheckItem checkItemDto);

    /**
     * 根据ID删除
     *
     * @param checkItemIds
     * @return
     */
    int deleteCheckItemByIds(Long[] checkItemIds);

    /**
     * 查询所有可用的检查项目
     * @return
     */
    List<SystemCheckItem> queryAllCheckItems();

}
