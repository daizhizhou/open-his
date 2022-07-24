package cn.cloud9.service;

import cn.cloud9.domain.SystemLoginInfo;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SystemLoginInfoService extends IService<SystemLoginInfo>{
    /**
     * 添加
     *
     * @param loginInfo
     * @return
     */
    int insertLoginInfo(SystemLoginInfo loginInfo);

    /**
     * 分页查询
     *
     * @param loginInfoDto
     * @return
     */
    DataGridViewVO listForPage(SystemLoginInfo loginInfoDto);

    /**
     * 删除登陆日志
     *
     * @param infoIds
     * @return
     */
    int deleteLoginInfoByIds(Long[] infoIds);

    /**
     * 清空登陆日志
     *
     * @return
     */
    int clearLoginInfo();

}
