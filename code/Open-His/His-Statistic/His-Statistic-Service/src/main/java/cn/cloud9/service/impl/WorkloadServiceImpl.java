package cn.cloud9.service.impl;

import cn.cloud9.domain.Workload;
import cn.cloud9.domain.WorkloadQueryDto;
import cn.cloud9.domain.WorkloadStat;
import cn.cloud9.mapper.WorkloadMapper;
import cn.cloud9.service.WorkloadService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 08:16
 */
@Service
@Component
public class WorkloadServiceImpl implements WorkloadService {

    @Resource
    private WorkloadMapper workloadMapper;

    @Override
    public List<Workload> queryWorkload(WorkloadQueryDto workloadQueryDto) {
        return this.workloadMapper.queryWorkload(workloadQueryDto);
    }

    @Override
    public List<WorkloadStat> queryWorkloadStat(WorkloadQueryDto workloadQueryDto) {
        return this.workloadMapper.queryWorkloadStat(workloadQueryDto);
    }
}
