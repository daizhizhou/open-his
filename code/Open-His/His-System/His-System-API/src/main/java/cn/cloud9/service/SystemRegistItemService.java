package cn.cloud9.service;

import cn.cloud9.domain.SystemRegistItem;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SystemRegistItemService extends IService<SystemRegistItem>{
    /**
     * 分页查询
     *
     * @param registeredItemDto
     * @return
     */
    DataGridViewVO listRegisteredItemPage(SystemRegistItem registeredItemDto);

    /**
     * 根据ID查询
     *
     * @param registeredItemId
     * @return
     */
    SystemRegistItem getOne(Long registeredItemId);

    /**
     * 添加
     *
     * @param registeredItemDto
     * @return
     */
    int addRegisteredItem(SystemRegistItem registeredItemDto);

    /**
     * 修改
     *
     * @param registeredItemDto
     * @return
     */
    int updateRegisteredItem(SystemRegistItem registeredItemDto);

    /**
     * 根据ID删除
     *
     * @param registeredItemIds
     * @return
     */
    int deleteRegisteredItemByIds(Long[] registeredItemIds);

    /**
     * 查询所有可用的挂号项目
     * @return
     */
    List<SystemRegistItem> queryAllRegisteredItems();

}
