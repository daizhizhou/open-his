package cn.cloud9.controller.statistic;

import cn.cloud9.config.spring.HystrixSupport;
import cn.cloud9.domain.Workload;
import cn.cloud9.domain.WorkloadQueryDto;
import cn.cloud9.domain.WorkloadStat;
import cn.cloud9.service.WorkloadService;
import cn.cloud9.vo.AjaxResult;
import cn.hutool.core.date.DateUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 08:18
 */
@RestController
@RequestMapping("statistics/workload")
public class StatWorkloadController extends HystrixSupport {
    @Reference
    private WorkloadService workloadService;

    /**
     * 医生工作量统计列表
     */
    @GetMapping("queryWorkload")
    public AjaxResult queryWorkload(WorkloadQueryDto workloadQueryDto) {
        if (workloadQueryDto.getBeginTime() == null) {
            workloadQueryDto.setQueryDate(DateUtil.format(DateUtil.date(), "yyyy-MM-dd"));
        }
        List<Workload> workloadList = this.workloadService.queryWorkload(workloadQueryDto);
        return AjaxResult.success(workloadList);
    }


    /**
     * 总体工作量统计列表
     */
    @GetMapping("queryWorkloadStat")
    public AjaxResult queryWorkloadStat(WorkloadQueryDto workloadQueryDto) {
        if (workloadQueryDto.getBeginTime() == null) {
            workloadQueryDto.setQueryDate(DateUtil.format(DateUtil.date(), "yyyy-MM-dd"));
        }
        List<WorkloadStat> workloadList = this.workloadService.queryWorkloadStat(workloadQueryDto);
        return AjaxResult.success(workloadList);
    }
}
