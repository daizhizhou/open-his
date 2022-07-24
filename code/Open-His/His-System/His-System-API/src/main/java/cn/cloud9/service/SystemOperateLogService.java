package cn.cloud9.service;

import cn.cloud9.domain.SystemOperateLog;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SystemOperateLogService extends IService<SystemOperateLog>{

    /**
     * 插入操作日志
     * @param operateLog
     */
    void insertOperateLog(SystemOperateLog operateLog);

    /**
     * 分页查询操作日志
     * @param operateLog
     * @return
     */
    DataGridViewVO listForPage(SystemOperateLog operateLog);

    /**
     * 根据ID删除操作日志
     * @param infoIds
     * @return
     */
    int deleteOperateLogByIds(Long[] infoIds);

    /**
     * 清空操作日志
     * @return
     */
    int clearAllOperateLog();
}
