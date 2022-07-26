package cn.cloud9.service;

import cn.cloud9.domain.InventoryLog;
import cn.cloud9.mapper.InventoryLogMapper;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface InventoryLogService extends IService<InventoryLog> {
    /**
     * 分页查询
     *
     * @param inventoryLogDto
     * @return
     */
    DataGridViewVO listInventoryLogPage(InventoryLog inventoryLogDto);
}
