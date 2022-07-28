package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.SystemDepartment;
import cn.cloud9.mapper.SystemDepartmentMapper;
import cn.cloud9.service.SystemDepartmentService;
import cn.cloud9.utils.CheckUtil;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SystemDepartmentServiceImpl
        extends ServiceImpl<SystemDepartmentMapper, SystemDepartment>
        implements SystemDepartmentService {

    @Override
    public DataGridViewVO listPage(SystemDepartment deptDto) {
        final Page<SystemDepartment> page = this.lambdaQuery()
                .like(StringUtils.isNotBlank(deptDto.getDeptName()), SystemDepartment::getDeptName, deptDto.getDeptName())
                .eq(StringUtils.isNotBlank(deptDto.getStatus()), SystemDepartment::getStatus, deptDto.getStatus())
                .like(StringUtils.isNotBlank(deptDto.getDeptName()), SystemDepartment::getDeptName, deptDto.getDeptName())
                .between(
                        !CheckUtil.isEmpty(deptDto.getBeginTime()) && !CheckUtil.isEmpty(deptDto.getEndTime()),
                        SystemDepartment::getCreateTime,
                        deptDto.getBeginTime(),
                        deptDto.getEndTime()
                )
                .orderByAsc(SystemDepartment::getOrderNum)
                .page(new Page<SystemDepartment>(deptDto.getPageNum(), deptDto.getPageSize()));
        return new DataGridViewVO(page.getTotal(), page.getRecords());
    }

    @Override
    public List<SystemDepartment> list() {
        return this.lambdaQuery()
                .eq(SystemDepartment::getStatus, ApiConstant.STATUS_TRUE)
                .orderByAsc(SystemDepartment::getOrderNum)
                .list();
    }

    @Override
    public SystemDepartment getOne(Long deptId) {
        return this.lambdaQuery().eq(SystemDepartment::getDeptId, deptId).one();
    }

    @Override
    public int addDept(SystemDepartment deptDto) {
        deptDto.setCreateBy(deptDto.getSimpleUser().getUserName());
        deptDto.setCreateTime(DateUtil.date());
        return this.baseMapper.insert(deptDto);
    }

    @Override
    public int updateDept(SystemDepartment deptDto) {
        //设置创建者，。创建时间
        deptDto.setUpdateBy(deptDto.getSimpleUser().getUserName());
        deptDto.setUpdateTime(DateUtil.date());
        return this.baseMapper.updateById(deptDto);
    }

    @Override
    public int deleteDeptByIds(Long[] deptIds) {
        return CheckUtil.isEmptyArray(deptIds) ?
            0 : this.baseMapper.deleteBatchIds(Arrays.asList(deptIds));
    }

    @Override
    public List<SystemDepartment> listDeptByDeptIds(List<Long> deptIds) {
        QueryWrapper<SystemDepartment> qw = new QueryWrapper<>();
        qw.in(SystemDepartment.COL_DEPT_ID, deptIds);
        return this.baseMapper.selectList(qw);
    }

    @Override
    public void updateDeptRegNumber(Long deptId, Integer regNumber) {
        SystemDepartment dept = new SystemDepartment();
        dept.setDeptId(deptId);
        dept.setRegNumber(regNumber);
        this.baseMapper.updateById(dept);
    }
}
