package cn.cloud9.service;

import cn.cloud9.domain.SystemDictType;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SystemDictTypeService extends IService<SystemDictType>{
    /**
     * 分页查询字典类型
     * @param dictTypeDto
     * @return
     */
    DataGridViewVO listPage(SystemDictType dictTypeDto);
    /**
     * 查询所有字典类型
     * @return
     */
    List<SystemDictType> list();

    /**
     * 检查字典类型是否存在
     * @param dictType
     * @return
     */
    Boolean checkDictTypeUnique(Long dictId,String dictType);

    /**
     * 插入新的字典类型
     * @param dictTypeDto
     * @return
     */
    int insert(SystemDictType dictTypeDto);
    /**
     * 修改的字典类型
     * @param dictTypeDto
     * @return
     */
    int update(SystemDictType dictTypeDto);

    /**
     * 根据ID删除字典类型
     * @param dictIds
     * @return
     */
    int deleteDictTypeByIds(Long[] dictIds);

    /**
     * 根据ID查询一个字典类型
     * @param dictId
     * @return
     */
    SystemDictType selectDictTypeById(Long dictId);

}
