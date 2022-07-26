package cn.cloud9.service;

import cn.cloud9.domain.Provider;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProviderService extends IService<Provider>{
    /**
     * 分页查询
     *
     * @param providerDto
     * @return
     */
    DataGridViewVO listProviderPage(Provider providerDto);

    /**
     * 根据ID查询
     *
     * @param providerId
     * @return
     */
    Provider getOne(Long providerId);

    /**
     * 添加
     *
     * @param providerDto
     * @return
     */
    int addProvider(Provider providerDto);

    /**
     * 修改
     *
     * @param providerDto
     * @return
     */
    int updateProvider(Provider providerDto);

    /**
     * 根据ID删除
     *
     * @param providerIds
     * @return
     */
    int deleteProviderByIds(Long[] providerIds);

    /**
     * 查询所有可用供应商
     */
    List<Provider> selectAllProvider();

}
