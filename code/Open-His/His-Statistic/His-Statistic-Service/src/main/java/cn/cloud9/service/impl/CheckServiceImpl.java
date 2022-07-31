package cn.cloud9.service.impl;

import cn.cloud9.domain.Check;
import cn.cloud9.domain.CheckQueryDto;
import cn.cloud9.domain.CheckStat;
import cn.cloud9.mapper.CheckMapper;
import cn.cloud9.service.CheckService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 07:49
 */
@Service
@Component
public class CheckServiceImpl implements CheckService {

    @Resource
    private CheckMapper checkMapper;

    @Override
    public List<Check> queryCheck(CheckQueryDto checkQueryDto) {
        return this.checkMapper.queryCheck(checkQueryDto);
    }

    @Override
    public List<CheckStat> queryCheckStat(CheckQueryDto checkQueryDto) {
        return this.checkMapper.queryCheckStat(checkQueryDto);
    }
}
