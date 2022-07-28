package cn.cloud9.service;

import cn.cloud9.domain.Scheduling;
import cn.cloud9.domain.SchedulingForm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SchedulingService extends IService<Scheduling> {

    /**
     * 查询排班的数据
     *
     * @param schedulingDto
     * @return
     */
    List<Scheduling> queryScheduling(Scheduling schedulingDto);

    /**
     * 保存排班的数据
     *
     * @param schedulingFormDto
     * @return
     */
    int saveScheduling(SchedulingForm schedulingFormDto);

    /**
     * 查询有排班的部门信息
     * @param deptId
     * @param schedulingDay
     * @param schedulingType
     * @param subsectionType
     * @return
     */
    List<Long> queryHasSchedulingDeptIds(Long deptId, String schedulingDay, String schedulingType, String subsectionType);
}
