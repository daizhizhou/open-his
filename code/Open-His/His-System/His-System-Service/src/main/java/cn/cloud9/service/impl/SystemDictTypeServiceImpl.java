package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.SystemDictData;
import cn.cloud9.domain.SystemDictType;
import cn.cloud9.domain.SystemMenu;
import cn.cloud9.mapper.SystemDictDataMapper;
import cn.cloud9.mapper.SystemDictTypeMapper;
import cn.cloud9.service.SystemDictTypeService;
import cn.cloud9.utils.CheckUtil;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class SystemDictTypeServiceImpl
        extends ServiceImpl<SystemDictTypeMapper, SystemDictType>
        implements SystemDictTypeService{

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SystemDictDataMapper systemDictDataMapper;

    @Override
    public DataGridViewVO listPage(SystemDictType dictTypeDto) {
        final Page<SystemDictType> selectPage = this.baseMapper.selectPage(
            new Page<>(dictTypeDto.getPageNum(), dictTypeDto.getPageSize()),
            new LambdaQueryWrapper<SystemDictType>()
                .like(StringUtils.isNotBlank(dictTypeDto.getDictName()), SystemDictType::getDictName, dictTypeDto.getDictName())
                .like(StringUtils.isNotBlank(dictTypeDto.getDictType()), SystemDictType::getDictType, dictTypeDto.getDictType())
                .eq(StringUtils.isNotBlank(dictTypeDto.getStatus()), SystemDictType::getStatus, dictTypeDto.getStatus())
                .between(
                        !CheckUtil.isEmpty(dictTypeDto.getBeginTime()) && !CheckUtil.isEmpty(dictTypeDto.getEndTime()),
                        SystemDictType::getCreateTime,
                        dictTypeDto.getBeginTime(),
                        dictTypeDto.getEndTime()
                )
                .orderByAsc(SystemDictType::getDictId)
        );
        return new DataGridViewVO(selectPage.getTotal(), selectPage.getRecords());
    }

    /**
     * 只查可用的菜单
     * @return
     */
    @Override
    public List<SystemDictType> list() {
        return this.baseMapper.selectList( new LambdaQueryWrapper<SystemDictType>().eq(SystemDictType::getStatus, ApiConstant.STATUS_TRUE));
    }

    /**
     * 检查字典类型是否唯一？
     * @param dictId
     * @param dictType
     * @return
     */
    @Override
    public Boolean checkDictTypeUnique(Long dictId, String dictType) {
        dictId = CheckUtil.isEmpty(dictId) ? -1L : dictId;
        final SystemDictType sysDictType = this.baseMapper.selectOne( new LambdaQueryWrapper<SystemDictType>().eq(SystemDictType::getDictType, dictType));
        return !CheckUtil.isEmpty(sysDictType) && !dictId.equals(sysDictType.getDictId());
    }

    @Override
    public int insert(SystemDictType dictTypeDto) {
        SystemDictType dictType = new SystemDictType();
        BeanUtil.copyProperties(dictTypeDto, dictType);
        dictType.setCreateTime(DateUtil.date());
        dictType.setCreateBy(dictTypeDto.getSimpleUser().getUserName());
        return this.baseMapper.insert(dictType);
    }

    @Override
    public int update(SystemDictType dictTypeDto) {
        SystemDictType dictType = new SystemDictType();
        BeanUtil.copyProperties(dictTypeDto, dictType);
        dictType.setUpdateBy(dictTypeDto.getSimpleUser().getUserName());
        return this.baseMapper.updateById(dictType);
    }

    @Override
    public int deleteDictTypeByIds(Long[] dictIds) {
        List<Long> ids = Arrays.asList(dictIds);
        boolean isEmptyIds = CollectionUtil.isEmpty(ids);
        return isEmptyIds ? -1 : this.baseMapper.deleteBatchIds(ids);
    }

    @Override
    public SystemDictType selectDictTypeById(Long dictId) {
        return this.baseMapper.selectById(dictId);
    }

    @Override
    public void dictCacheAsync() {
        //查询所有可用的dictType
        final List<SystemDictType> dictTypes = this.baseMapper.selectList(
            new LambdaQueryWrapper<SystemDictType>().eq(SystemDictType::getStatus, ApiConstant.STATUS_TRUE));

        final ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        dictTypes.forEach(dictType -> {
            List<SystemDictData> dictData = systemDictDataMapper.selectList(
                new LambdaQueryWrapper<SystemDictData>()
                .eq(SystemDictData::getStatus, ApiConstant.STATUS_TRUE)
                .eq(SystemDictData::getDictType, dictType.getDictType())
            );
            valueOperations.set(ApiConstant.DICT_REDIS_PREFIX + dictType.getDictType(), JSON.toJSONString(dictData));
        });
    }
}
