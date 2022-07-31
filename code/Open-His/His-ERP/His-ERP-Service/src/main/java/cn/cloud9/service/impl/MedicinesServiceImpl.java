package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.Medicines;
import cn.cloud9.mapper.MedicinesMapper;
import cn.cloud9.service.MedicinesService;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@Service(methods = {@Method(name = "addMedicines", retries = 0)})
public class MedicinesServiceImpl extends ServiceImpl<MedicinesMapper, Medicines> implements MedicinesService {

    @Override
    public DataGridViewVO listMedicinesPage(Medicines medicinesDto) {
        Page<Medicines> page = new Page<>(medicinesDto.getPageNum(), medicinesDto.getPageSize());
        QueryWrapper<Medicines> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(medicinesDto.getMedicinesName()), Medicines.COL_MEDICINES_NAME, medicinesDto.getMedicinesName());
        qw.like(StringUtils.isNotBlank(medicinesDto.getKeywords()), Medicines.COL_KEYWORDS, medicinesDto.getKeywords());
        qw.eq(StringUtils.isNotBlank(medicinesDto.getMedicinesType()), Medicines.COL_MEDICINES_TYPE, medicinesDto.getMedicinesType());
        qw.eq(StringUtils.isNotBlank(medicinesDto.getProducterId()), Medicines.COL_PRODUCTER_ID, medicinesDto.getProducterId());
        qw.eq(StringUtils.isNotBlank(medicinesDto.getPrescriptionType()), Medicines.COL_PRESCRIPTION_TYPE, medicinesDto.getPrescriptionType());
        qw.eq(StringUtils.isNotBlank(medicinesDto.getStatus()), Medicines.COL_STATUS, medicinesDto.getStatus());
        this.baseMapper.selectPage(page, qw);
        return new DataGridViewVO(page.getTotal(), page.getRecords());
    }

    @Override
    public Medicines getOne(Long medicinesId) {
        return this.baseMapper.selectById(medicinesId);
    }

    @Override
    public int addMedicines(Medicines medicinesDto) {
        medicinesDto.setCreateTime(DateUtil.date());
        medicinesDto.setCreateBy(medicinesDto.getSimpleUser().getUserName());
        medicinesDto.setUpdateTime(DateUtil.date());
        medicinesDto.setUpdateBy(medicinesDto.getSimpleUser().getUserName());
        return this.baseMapper.insert(medicinesDto);
    }

    @Override
    public int updateMedicines(Medicines medicinesDto) {
        medicinesDto.setUpdateTime(DateUtil.date());
        medicinesDto.setUpdateBy(medicinesDto.getSimpleUser().getUserName());
        return this.baseMapper.updateById(medicinesDto);
    }

    @Override
    public int deleteMedicinesByIds(Long[] medicinesIds) {
        List<Long> ids = Arrays.asList(medicinesIds);
        if (ids.size() > 0) {
            return this.baseMapper.deleteBatchIds(ids);
        }
        return 0;
    }

    @Override
    public List<Medicines> selectAllMedicines() {
        QueryWrapper<Medicines> qw = new QueryWrapper<>();
        qw.eq(Medicines.COL_STATUS, ApiConstant.STATUS_TRUE);
        return this.baseMapper.selectList(qw);
    }

    @Override
    public int updateMedicinesStorage(Long medicinesId, Long medicinesStockNum) {
        Medicines medicines = new Medicines();
        medicines.setMedicinesId(medicinesId);
        medicines.setMedicinesStockNum(medicinesStockNum);
        return this.baseMapper.updateById(medicines);
    }

    @Override
    @Transactional
    public int deductionMedicinesStorage(Long medicinesId, Long num) {
        return this.baseMapper.deductionMedicinesStorage(medicinesId,num);
    }
}
