package cn.cloud9.service;

import cn.cloud9.domain.Drug;
import cn.cloud9.domain.DrugQueryDto;
import cn.cloud9.domain.DrugStat;

import java.util.List;

public interface DrugService {
    /**
     * 查询发药统计列表
     * @param drugQueryDto
     * @return
     */
    List<Drug> queryDrug(DrugQueryDto drugQueryDto);

    /**
     * 查询发药数量统计列表
     * @param drugQueryDto
     * @return
     */
    List<DrugStat> queryDrugStat(DrugQueryDto drugQueryDto);
}
