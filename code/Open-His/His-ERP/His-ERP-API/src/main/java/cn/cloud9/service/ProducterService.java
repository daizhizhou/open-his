package cn.cloud9.service;

import cn.cloud9.domain.Producter;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ProducterService extends IService<Producter> {
    /**
     * 分页查询
     *
     * @param producterDto
     * @return
     */
    DataGridViewVO listProducterPage(Producter producterDto);

    /**
     * 根据ID查询
     *
     * @param producterId
     * @return
     */
    Producter getOne(Long producterId);

    /**
     * 添加
     *
     * @param producterDto
     * @return
     */
    int addProducter(Producter producterDto);

    /**
     * 修改
     *
     * @param producterDto
     * @return
     */
    int updateProducter(Producter producterDto);

    /**
     * 根据ID删除
     *
     * @param producterIds
     * @return
     */
    int deleteProducterByIds(Long[] producterIds);

    /**
     * 查询所有可用的生产厂家
     */
    List<Producter> selectAllProducter();

}

