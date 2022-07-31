package cn.cloud9.mapper;

import cn.cloud9.domain.Check;
import cn.cloud9.domain.CheckQueryDto;
import cn.cloud9.domain.CheckStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckMapper {
    /**
     * 查询检查项列表
     *
     * @param checkQueryDto
     * @return
     */
    List<Check> queryCheck(@Param("check") CheckQueryDto checkQueryDto);

    /**
     * 查询检查项统计列表
     *
     * @param checkQueryDto
     * @return
     */
    List<CheckStat> queryCheckStat(@Param("check") CheckQueryDto checkQueryDto);
}
