package cn.cloud9.mapper;

import cn.cloud9.domain.Income;
import cn.cloud9.domain.Refund;
import cn.cloud9.domain.RevenueQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RevenueMapper {
    /**
     * 查询收入的数据
     *
     * @param revenueQueryDto
     * @return
     */
    List<Income> queryIncome(@Param("revenue") RevenueQueryDto revenueQueryDto);

    /**
     * 查询退费的数据
     *
     * @param revenueQueryDto
     * @return
     */
    List<Refund> queryRefund(@Param("revenue") RevenueQueryDto revenueQueryDto);
}
