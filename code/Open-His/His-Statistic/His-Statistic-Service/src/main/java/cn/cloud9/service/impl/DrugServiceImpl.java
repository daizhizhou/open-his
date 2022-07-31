package cn.cloud9.service.impl;

import cn.cloud9.domain.Drug;
import cn.cloud9.domain.DrugQueryDto;
import cn.cloud9.domain.DrugStat;
import cn.cloud9.mapper.DrugMapper;
import cn.cloud9.service.DrugService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 07:40
 */
@Service
@Component
public class DrugServiceImpl implements DrugService {
    @Resource
    private DrugMapper drugMapper;

    @Override
    public List<Drug> queryDrug(DrugQueryDto drugQueryDto) {
        return this.drugMapper.queryDrug(drugQueryDto);
    }

    @Override
    public List<DrugStat> queryDrugStat(DrugQueryDto drugQueryDto) {
        return this.drugMapper.queryDrugStat(drugQueryDto);
    }
}
