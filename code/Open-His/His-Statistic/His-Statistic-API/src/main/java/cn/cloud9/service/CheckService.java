package cn.cloud9.service;

import cn.cloud9.domain.Check;
import cn.cloud9.domain.CheckQueryDto;
import cn.cloud9.domain.CheckStat;

import java.util.List;

public interface CheckService {
    /**
     * 查询检查项列表
     * @param checkQueryDto
     * @return
     */
    List<Check> queryCheck(CheckQueryDto checkQueryDto);

    /**
     * 查询检查项统计列表
     * @param checkQueryDto
     * @return
     */
    List<CheckStat> queryCheckStat(CheckQueryDto checkQueryDto);
}
