package cn.cloud9.service.impl;

import cn.cloud9.utils.CheckUtil;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cloud9.mapper.SystemOperateLogMapper;
import cn.cloud9.domain.SystemOperateLog;
import cn.cloud9.service.SystemOperateLogService;
@Service
public class SystemOperateLogServiceImpl extends ServiceImpl<SystemOperateLogMapper, SystemOperateLog> implements SystemOperateLogService{

    @Override
    public void insertOperateLog(SystemOperateLog operateLog) {
        this.baseMapper.insert(operateLog);
    }

    @Override
    public DataGridViewVO listForPage(SystemOperateLog operateLog) {
        final Page<SystemOperateLog> page = this.baseMapper.selectPage(
            new Page<>(operateLog.getPageNum(), operateLog.getPageSize()),
            new LambdaQueryWrapper<SystemOperateLog>()
                .like(StringUtils.isNotBlank(operateLog.getOperName()), SystemOperateLog::getOperName, operateLog.getOperName())
                .like(StringUtils.isNotBlank(operateLog.getTitle()), SystemOperateLog::getTitle, operateLog.getTitle())
                .eq(StringUtils.isNotBlank(operateLog.getBusinessType()), SystemOperateLog::getBusinessType, operateLog.getBusinessType())
                .eq(StringUtils.isNotBlank(operateLog.getStatus()), SystemOperateLog::getStatus, operateLog.getStatus())
                .between(
                    !CheckUtil.isEmpty(operateLog.getBeginTime()) && !CheckUtil.isEmpty(operateLog.getEndTime()),
                    SystemOperateLog::getOperTime,
                    operateLog.getBeginTime(),
                    operateLog.getEndTime()
                )
                .orderByDesc(SystemOperateLog::getOperTime)
        );
        return new DataGridViewVO(page.getTotal(), page.getRecords());
    }

    @Override
    public int deleteOperateLogByIds(Long[] infoIds) {
        return CheckUtil.isEmptyArray(infoIds) ?
            0 : this.baseMapper.deleteBatchIds(Arrays.asList(infoIds));
    }

    @Override
    public int clearAllOperateLog() {
        return this.baseMapper.delete(null);
    }
}
