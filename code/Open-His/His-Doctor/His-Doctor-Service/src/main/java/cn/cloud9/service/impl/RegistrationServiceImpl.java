package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.Registration;
import cn.cloud9.mapper.RegistrationMapper;
import cn.cloud9.service.RegistrationService;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Component
@Service
public class RegistrationServiceImpl
        extends ServiceImpl<RegistrationMapper, Registration>
        implements RegistrationService {

    @Override
    public void addRegistration(Registration registrationDto) {
        registrationDto.setRegStatus(ApiConstant.REG_STATUS_0);
        registrationDto.setCreateBy(registrationDto.getSimpleUser().getUserName());
        registrationDto.setCreateTime(DateUtil.date());
        registrationDto.setUpdateTime(DateUtil.date());
        this.baseMapper.insert(registrationDto);
    }

    @Override
    public DataGridViewVO queryRegistrationForPage(Registration registrationDto) {
        Page<Registration> page=new Page<>(registrationDto.getPageNum(),registrationDto.getPageSize());
        QueryWrapper<Registration> qw=new QueryWrapper<>();
        qw.eq(registrationDto.getDeptId()!=null,Registration.COL_DEPT_ID,registrationDto.getDeptId());
        qw.eq(StringUtils.isNotBlank(registrationDto.getSchedulingType()),Registration.COL_SCHEDULING_TYPE,registrationDto.getSchedulingType());
        qw.eq(StringUtils.isNotBlank(registrationDto.getSubsectionType()),Registration.COL_SUBSECTION_TYPE,registrationDto.getSubsectionType());
        qw.eq(StringUtils.isNotBlank(registrationDto.getRegStatus()), Registration.COL_REG_STATUS,registrationDto.getRegStatus());
        qw.eq(StringUtils.isNotBlank(registrationDto.getVisitDate()),Registration.COL_VISIT_DATE,registrationDto.getVisitDate());
        qw.ge(registrationDto.getBeginTime()!=null,Registration.COL_VISIT_DATE,DateUtil.format(registrationDto.getBeginTime(),"yyyy-MM-dd"));
        qw.le(registrationDto.getEndTime()!=null,Registration.COL_VISIT_DATE,DateUtil.format(registrationDto.getEndTime(),"yyyy-MM-dd"));
        qw.orderByDesc(Registration.COL_CREATE_TIME);
        this.baseMapper.selectPage(page,qw);
        return new DataGridViewVO(page.getTotal(),page.getRecords());
    }

    @Override
    public int updateRegistrationById(Registration registration) {
        return this.baseMapper.updateById(registration);
    }

    @Override
    public Registration queryRegistrationByRegId(String registrationId) {
        return this.baseMapper.selectById(registrationId);
    }
}
