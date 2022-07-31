package cn.cloud9.controller.statistic;

import cn.cloud9.config.spring.HystrixSupport;
import cn.cloud9.domain.Check;
import cn.cloud9.domain.CheckQueryDto;
import cn.cloud9.domain.CheckStat;
import cn.cloud9.service.CheckService;
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
 * @date 2022年07月31日 下午 07:52
 */
@RestController
@RequestMapping("statistics/check")
public class StatCheckController extends HystrixSupport {

    @Reference
    private CheckService checkService;

    /**
     * 查询检查项列表
     */
    @GetMapping("queryCheck")
    public AjaxResult queryCheck(CheckQueryDto checkQueryDto) {
        if (checkQueryDto.getBeginTime() == null) {
            checkQueryDto.setQueryDate(DateUtil.format(DateUtil.date(), "yyyy-MM-dd"));
        }
        List<Check> checkList = this.checkService.queryCheck(checkQueryDto);
        return AjaxResult.success(checkList);
    }


    /**
     * 查询检查项统计列表
     */
    @GetMapping("queryCheckStat")
    public AjaxResult queryCheckStat(CheckQueryDto checkQueryDto) {
        if (checkQueryDto.getBeginTime() == null) {
            checkQueryDto.setQueryDate(DateUtil.format(DateUtil.date(), "yyyy-MM-dd"));
        }
        List<CheckStat> checkList = this.checkService.queryCheckStat(checkQueryDto);
        return AjaxResult.success(checkList);
    }
}
