package cn.cloud9.service;

import cn.cloud9.domain.CheckResult;
import cn.cloud9.domain.form.CheckResultForm;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;
public interface CheckResultService extends IService<CheckResult>{


    /**
     * 保存检查项目信息
     *
     * @param checkResult
     * @return
     */
    int saveCheckResult(CheckResult checkResult);

    /**
     * 根据条件查询所有检查中的和检查完成了的项目
     * @param checkResultDto
     * @return
     */
    DataGridViewVO queryAllCheckResultForPage(CheckResult checkResultDto);

    /**
     * 完成检查
     * @param checkResultFormDto
     * @return
     */
    int completeCheckResult(CheckResultForm checkResultFormDto);
}
