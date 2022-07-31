package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.CareOrderItem;
import cn.cloud9.domain.CheckResult;
import cn.cloud9.domain.OrderChargeItem;
import cn.cloud9.domain.form.CheckResultForm;
import cn.cloud9.mapper.CareOrderItemMapper;
import cn.cloud9.mapper.CheckResultMapper;
import cn.cloud9.mapper.OrderChargeItemMapper;
import cn.cloud9.service.CheckResultService;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Method;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Service(methods = {@Method(name = "saveCheckResult", retries = 1)})
public class CheckResultServiceImpl extends ServiceImpl<CheckResultMapper, CheckResult> implements CheckResultService {

    @Resource
    private CareOrderItemMapper careOrderItemMapper;

    @Resource
    private OrderChargeItemMapper orderChargeItemMapper;

    @Override
    public int saveCheckResult(CheckResult checkResult) {
        //保存检查项目
        int i = baseMapper.insert(checkResult);
        //更新收费详情的状态
        OrderChargeItem orderChargeItem = new OrderChargeItem();
        orderChargeItem.setItemId(checkResult.getItemId());
        orderChargeItem.setStatus(ApiConstant.ORDER_DETAILS_STATUS_3);//已完成
        this.orderChargeItemMapper.updateById(orderChargeItem);
        //更新处方详情的状态
        CareOrderItem careOrderItem = new CareOrderItem();
        careOrderItem.setItemId(checkResult.getItemId());
        careOrderItem.setStatus(ApiConstant.ORDER_DETAILS_STATUS_3);//已完成
        this.careOrderItemMapper.updateById(careOrderItem);
        return i;
    }

    @Override
    public DataGridViewVO queryAllCheckResultForPage(CheckResult checkResultDto) {
        Page<CheckResult> page = new Page<>(checkResultDto.getPageNum(), checkResultDto.getPageSize());
        QueryWrapper<CheckResult> qw = new QueryWrapper<>();
        qw.in(checkResultDto.getCheckItemIds().size() > 0, CheckResult.COL_CHECK_ITEM_ID, checkResultDto.getCheckItemIds());
        qw.like(StringUtils.isNotBlank(checkResultDto.getPatientName()), CheckResult.COL_PATIENT_NAME, checkResultDto.getPatientName());
        qw.like(StringUtils.isNotBlank(checkResultDto.getRegId()), CheckResult.COL_REG_ID, checkResultDto.getRegId());
        qw.eq(StringUtils.isNotBlank(checkResultDto.getResultStatus()), CheckResult.COL_RESULT_STATUS, checkResultDto.getResultStatus());
        this.baseMapper.selectPage(page, qw);
        return new DataGridViewVO(page.getTotal(), page.getRecords());
    }

    @Override
    public int completeCheckResult(CheckResultForm checkResultFormDto) {
        CheckResult checkResult = new CheckResult();
        BeanUtil.copyProperties(checkResultFormDto, checkResult);
        checkResult.setResultStatus(ApiConstant.RESULT_STATUS_1);//检查完成
        checkResult.setUpdateBy(checkResultFormDto.getSimpleUser().getUserName());
        return this.baseMapper.updateById(checkResult);
    }
}
