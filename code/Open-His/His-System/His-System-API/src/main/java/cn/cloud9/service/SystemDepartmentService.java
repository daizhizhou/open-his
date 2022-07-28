package cn.cloud9.service;

import cn.cloud9.domain.SystemDepartment;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SystemDepartmentService extends IService<SystemDepartment> {

    /**
     * 分页查询
     * @param deptDto
     * @return
     */
    DataGridViewVO listPage(SystemDepartment deptDto);

    /**
     * 查询所有有效部门
     * @return
     */
    List<SystemDepartment> list();

    /**
     * 根据ID查询一个
     * @param deptId
     * @return
     */
    SystemDepartment getOne(Long deptId);

    /**
     * 添加一个部门
     * @param deptDto
     * @return
     */
    int addDept(SystemDepartment deptDto);

    /**
     * 修改部门
     * @param deptDto
     * @return
     */
    int updateDept(SystemDepartment deptDto);

    /**
     * 根据IDS删除部门
     * @param deptIds
     * @return
     */
    int deleteDeptByIds(Long[] deptIds);

    /**
     * 根据部门ID查询部门信息
     * @param deptIds
     * @return
     */
    List<SystemDepartment> listDeptByDeptIds(List<Long> deptIds);

    /**
     * 根据部门ID更新部门挂号编号
     * @param deptId
     * @param regNumber
     */
    void updateDeptRegNumber(Long deptId, Integer regNumber);
}
