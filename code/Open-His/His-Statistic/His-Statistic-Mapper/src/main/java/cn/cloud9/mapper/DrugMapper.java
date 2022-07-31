package cn.cloud9.mapper;

import cn.cloud9.domain.Drug;
import cn.cloud9.domain.DrugQueryDto;
import cn.cloud9.domain.DrugStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DrugMapper {
    /**
     * 药品统计列表
     *
     * @param drugQueryDto
     * @return
     */
    List<Drug> queryDrug(@Param("drug") DrugQueryDto drugQueryDto);

    /**
     * 药品数量统计列表
     *
     * @param drugQueryDto
     * @return
     */
    List<DrugStat> queryDrugStat(@Param("drug") DrugQueryDto drugQueryDto);
}
