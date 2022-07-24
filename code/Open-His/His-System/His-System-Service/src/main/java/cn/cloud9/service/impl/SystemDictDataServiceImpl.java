package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.SystemDictData;
import cn.cloud9.mapper.SystemDictDataMapper;
import cn.cloud9.service.SystemDictDataService;
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
public class SystemDictDataServiceImpl
    extends ServiceImpl<SystemDictDataMapper, SystemDictData>
    implements SystemDictDataService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public DataGridViewVO listPage(SystemDictData dictDataDto) {
        final Page<SystemDictData> page = this.baseMapper.selectPage(
            new Page<>(dictDataDto.getPageNum(), dictDataDto.getPageSize()),
            new LambdaQueryWrapper<SystemDictData>()
            .eq(StringUtils.isNotBlank(dictDataDto.getDictType()), SystemDictData::getDictType, dictDataDto.getDictType())
            .eq(StringUtils.isNotBlank(dictDataDto.getDictLabel()), SystemDictData::getDictLabel, dictDataDto.getDictLabel())
            .eq(StringUtils.isNotBlank(dictDataDto.getStatus()), SystemDictData::getStatus, dictDataDto.getStatus())
        );
        return new DataGridViewVO(page.getTotal(),page.getRecords());
    }

    @Override
    public int insert(SystemDictData dictDataDto) {
        SystemDictData dictData = new SystemDictData();
        BeanUtil.copyProperties(dictDataDto,dictData);
        //设置创建者，创建时间
        dictData.setCreateBy(dictDataDto.getSimpleUser().getUserName());
        dictData.setCreateTime(DateUtil.date());
        return this.baseMapper.insert(dictData);
    }

    @Override
    public int update(SystemDictData dictDataDto) {
        SystemDictData dictData = new SystemDictData();
        BeanUtil.copyProperties(dictDataDto , dictData);
        // 设置修改人, 更新时间
        dictData.setUpdateBy(dictDataDto.getSimpleUser().getUserName());
        dictData.setUpdateTime(DateUtil.date());
        return this.baseMapper.updateById(dictData);
    }

    @Override
    public int deleteDictDataByIds(Long[] dictCodeIds) {
        List<Long> ids= Arrays.asList(dictCodeIds);
        boolean isEmpty = CollectionUtil.isEmpty(ids);
        return isEmpty ? -1 : this.baseMapper.deleteBatchIds(ids);
    }

    /**
     * 从mysql直接查询，改为从redis获取
     * return this.baseMapper.selectList(
     *     new LambdaQueryWrapper<SystemDictData>()
     *     .eq(SystemDictData::getDictType, dictType)
     *     .eq(SystemDictData::getStatus, ApiConstant.STATUS_TRUE)
     * );
     * @param dictType
     * @return
     */
    @Override
    public List<SystemDictData> selectDictDataByDictType(String dictType) {
        String key = ApiConstant.DICT_REDIS_PREFIX + dictType;
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        String json = opsForValue.get(key);
        return JSON.parseArray(json, SystemDictData.class);
    }

    @Override
    public SystemDictData selectDictDataById(Long dictCode) {
        return this.baseMapper.selectById(dictCode);
    }
}
