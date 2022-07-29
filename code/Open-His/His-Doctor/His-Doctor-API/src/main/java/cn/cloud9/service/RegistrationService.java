package cn.cloud9.service;

import cn.cloud9.domain.Registration;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;
public interface RegistrationService extends IService<Registration>{

    /**
     * 保存挂号单信息
     * @param registrationDto
     */
    void addRegistration(Registration registrationDto);

    /**
     * 分页加载挂号列表【默认当天的】
     * @param registrationDto
     * @return
     */
    DataGridViewVO queryRegistrationForPage(Registration registrationDto);

    /**
     * 更新挂号单信息
     * @param registration
     * @return
     */
    int updateRegistrationById(Registration registration);

    /**
     * 根据挂号ID查询挂号信息
     * @param registrationId
     * @return
     */
    Registration queryRegistrationByRegId(String registrationId);

    int updateRegistrationByRegId(Registration registration);

}
