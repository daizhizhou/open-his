package cn.cloud9.service;

import cn.cloud9.domain.Patient;
import cn.cloud9.domain.PatientFile;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;
public interface PatientService extends IService<Patient>{

    /**
     * 分页查询
     * @param patientDto
     * @return
     */
    DataGridViewVO listPatientForPage(Patient patientDto);

    /**
     * 根据患者ID查询患者信息
     * @param patientId
     * @return
     */
    Patient getPatientById(String patientId);

    /**
     * 根据患者ID查询患者档案
     * @param patientId
     * @return
     */
    PatientFile getPatientFileById(String patientId);
}
