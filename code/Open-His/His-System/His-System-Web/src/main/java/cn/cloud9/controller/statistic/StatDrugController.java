package cn.cloud9.controller.statistic;

import cn.cloud9.config.spring.HystrixSupport;
import cn.cloud9.domain.Drug;
import cn.cloud9.domain.DrugQueryDto;
import cn.cloud9.domain.DrugStat;
import cn.cloud9.service.DrugService;
import cn.cloud9.vo.AjaxResult;
import cn.hutool.core.date.DateUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author OnCloud9
 * @description 药品销售统计
 * @project Open-His
 * @date 2022年07月31日 下午 07:35
 */
@RestController
@RequestMapping("statistics/drug")
public class StatDrugController extends HystrixSupport {

    @Reference
    private DrugService drugService;

    /**
     * 查询发药统计列表
     */
    @GetMapping("queryDrug")
    public AjaxResult queryDrug(DrugQueryDto drugQueryDto) {
        if (drugQueryDto.getBeginTime() == null) {
            drugQueryDto.setQueryDate(DateUtil.format(DateUtil.date(), "yyyy-MM-dd"));
        }
        List<Drug> drugList = this.drugService.queryDrug(drugQueryDto);
        return AjaxResult.success(drugList);
    }


    /**
     * 查询发药数量统计列表
     */
    @GetMapping("queryDrugStat")
    public AjaxResult queryDrugStat(DrugQueryDto drugQueryDto) {
        if (drugQueryDto.getBeginTime() == null) {
            drugQueryDto.setQueryDate(DateUtil.format(DateUtil.date(), "yyyy-MM-dd"));
        }
        List<DrugStat> drugList = this.drugService.queryDrugStat(drugQueryDto);
        return AjaxResult.success(drugList);
    }
}
