package cn.cloud9.mapper;

import cn.cloud9.domain.Scheduling;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SchedulingMapper extends BaseMapper<Scheduling> {

    /**
     * @param deptId
     * @param schedulingDay
     * @param schedulingType
     * @param subsectionType
     * @return
     */
    List<Long> queryHasSchedulingDeptIds(
            @Param("deptId") Long deptId,
            @Param("schedulingDay") String schedulingDay,
            @Param("schedulingType") String schedulingType,
            @Param("subsectionType") String subsectionType
    );
}