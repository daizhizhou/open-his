package cn.cloud9.mapper;

import cn.cloud9.domain.Workload;
import cn.cloud9.domain.WorkloadQueryDto;
import cn.cloud9.domain.WorkloadStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkloadMapper {
    /**
     * 医生工作量统计列表
     *
     * @param workloadQueryDto
     * @return
     */
    List<Workload> queryWorkload(@Param("workload") WorkloadQueryDto workloadQueryDto);

    /**
     * 总体工作量统计列表
     *
     * @param workloadQueryDto
     * @return
     */
    List<WorkloadStat> queryWorkloadStat(@Param("workload") WorkloadQueryDto workloadQueryDto);
}
