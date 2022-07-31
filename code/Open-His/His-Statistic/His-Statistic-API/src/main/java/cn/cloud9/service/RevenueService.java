package cn.cloud9.service;

import cn.cloud9.domain.RevenueQueryDto;

import java.util.Map;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 07:27
 */
public interface RevenueService {
    /**
     * 查询收支统计的数据
     * @param revenueQueryDto
     * @return
     */
    Map<String, Object> queryAllRevenueData(RevenueQueryDto revenueQueryDto);
}
