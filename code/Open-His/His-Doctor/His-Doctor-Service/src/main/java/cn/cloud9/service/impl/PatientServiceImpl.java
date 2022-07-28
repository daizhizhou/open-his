package cn.cloud9.service.impl;

import cn.cloud9.domain.Patient;
import cn.cloud9.domain.PatientFile;
import cn.cloud9.mapper.PatientFileMapper;
import cn.cloud9.mapper.PatientMapper;
import cn.cloud9.service.PatientService;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Service
public class PatientServiceImpl
        extends ServiceImpl<PatientMapper, Patient>
        implements PatientService {

    @Resource
    private PatientFileMapper patientFileMapper;

    @Override
    public DataGridViewVO listPatientForPage(Patient patientDto) {
        Page<Patient> page = new Page<>(patientDto.getPageNum(), patientDto.getPageSize());
        QueryWrapper<Patient> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(patientDto.getName()), Patient.COL_NAME, patientDto.getName());
        qw.like(StringUtils.isNotBlank(patientDto.getIdCard()), Patient.COL_ID_CARD, patientDto.getIdCard());
        qw.like(StringUtils.isNotBlank(patientDto.getPhone()), Patient.COL_PHONE, patientDto.getPhone());
        this.baseMapper.selectPage(page, qw);
        return new DataGridViewVO(page.getTotal(), page.getRecords());
    }

    @Override
    public Patient getPatientById(String patientId) {
        return this.baseMapper.selectById(patientId);
    }

    @Override
    public PatientFile getPatientFileById(String patientId) {
        if (null == patientId) return null;
        QueryWrapper<PatientFile> qw = new QueryWrapper<>();
        qw.eq(PatientFile.COL_PATIENT_ID, patientId);
        return this.patientFileMapper.selectOne(qw);
    }
}
