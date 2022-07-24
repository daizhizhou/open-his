package cn.cloud9.service;

import cn.cloud9.domain.SystemDictData;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SystemDictDataService extends IService<SystemDictData>{

    /**
     * 分页查询字典数据类型
     *
     * @param dictDataDto
     * @return
     */
    DataGridViewVO listPage(SystemDictData dictDataDto);


    /**
     * 插入新的字典类型
     *
     * @param dictDataDto
     * @return
     */
    int insert(SystemDictData dictDataDto);

    /**
     * 修改的字典类型
     *
     * @param dictDataDto
     * @return
     */
    int update(SystemDictData dictDataDto);

    /**
     * 根据ID删除字典类型
     *
     * @param dictCodeIds
     * @return
     */
    int deleteDictDataByIds(Long[] dictCodeIds);

    /**
     * 根据字典类型查询字典数据
     * @param dictType
     * @return
     */
    List<SystemDictData> selectDictDataByDictType(String dictType);

    /**
     * 根据ID查询一个字典类型
     *
     * @param dictCode
     * @return
     */
    SystemDictData selectDictDataById(Long dictCode);
}
