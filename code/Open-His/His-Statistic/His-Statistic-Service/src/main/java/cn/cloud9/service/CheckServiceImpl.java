package cn.cloud9.service;

import cn.cloud9.domain.Check;
import cn.cloud9.domain.CheckQueryDto;
import cn.cloud9.domain.CheckStat;
import cn.cloud9.mapper.CheckMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 07:49
 */
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
