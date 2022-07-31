package cn.cloud9.mapper;

import cn.cloud9.domain.Medicines;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface MedicinesMapper extends BaseMapper<Medicines> {

    /**
     * 扣减库存
     *
     * @param medicinesId
     * @param num
     * @return
     */
    int deductionMedicinesStorage(@Param("medicinesId") Long medicinesId, @Param("num") Long num);
}