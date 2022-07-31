package cn.cloud9.controller.doctor;

import cn.cloud9.config.spring.HystrixSupport;
import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.CareHistory;
import cn.cloud9.domain.CareOrder;
import cn.cloud9.domain.CareOrderItem;
import cn.cloud9.domain.CheckResult;
import cn.cloud9.domain.form.CheckResultForm;
import cn.cloud9.service.CareService;
import cn.cloud9.service.CheckResultService;
import cn.cloud9.util.ShiroSecurityUtil;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.date.DateUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 03:08
 */
@RestController
@RequestMapping("doctor/check")
public class CheckResultController extends HystrixSupport {
    @Reference
    private CareService careService;

    @Reference
    private CheckResultService checkResultService;

    /**
     * 根据挂号ID查询未支付的处方信息及详情
     */
    @PostMapping("queryNeedCheckItem")
    @HystrixCommand
    public AjaxResult getNoChargeCareHistoryByRegId(@RequestBody CheckResult checkResultDto) {
        //声明返回的对象
        List<CareOrderItem> resCareOrderItems = new ArrayList<>();
        if (StringUtils.isNotBlank(checkResultDto.getRegId())) {
            //查询时带入挂号单
            //根据挂号单ID查询病例信息
            CareHistory careHistory = this.careService.queryCareHistoryByRegId(checkResultDto.getRegId());
            if (null == careHistory) {
                return AjaxResult.success(resCareOrderItems);
            }
            //根据病例ID查询所有的处方信息
            List<CareOrder> careOrders = this.careService.queryCareOrdersByChId(careHistory.getChId());
            for (CareOrder careOrder : careOrders) {
                if (careOrder.getCoType().equals(ApiConstant.CO_TYPE_CHECK)) {//只取检查处方
                    List<CareOrderItem> careOrderItems = this.careService.queryCareOrderItemsByCoId(careOrder.getCoId(), ApiConstant.ORDER_DETAILS_STATUS_1);
                    //过滤查询条件
                    for (CareOrderItem careOrderItem : careOrderItems) {
                        if (checkResultDto.getCheckItemIds().contains(Integer.valueOf(careOrderItem.getItemRefId()))) {
                            resCareOrderItems.add(careOrderItem);
                        }
                    }
                }
            }
            return AjaxResult.success(resCareOrderItems);
        } else {
            //查询所有已支付检查的项目
            List<CareOrderItem> careOrderItems = this.careService.queryCareOrderItemsByStatus(ApiConstant.CO_TYPE_CHECK, ApiConstant.ORDER_DETAILS_STATUS_1);
            //过滤查询条件
            for (CareOrderItem careOrderItem : careOrderItems) {
                if (checkResultDto.getCheckItemIds().contains(Integer.valueOf(careOrderItem.getItemRefId()))) {
                    resCareOrderItems.add(careOrderItem);
                }
            }
            return AjaxResult.success(resCareOrderItems);
        }
    }

    /**
     * 开始检查
     */
    @PostMapping("startCheck/{itemId}")
    @HystrixCommand
    public AjaxResult startCheck(@PathVariable String itemId) {
        CareOrderItem careOrderItem = this.careService.queryCareOrderItemByItemId(itemId);
        if (careOrderItem == null) {
            return AjaxResult.fail("【" + itemId + "】的检查单号的数据不存在，请核对后再查询");
        }
        if (!careOrderItem.getStatus().equals(ApiConstant.ORDER_DETAILS_STATUS_1)) {
            return AjaxResult.fail("【" + itemId + "】的检查单号的没有支付，请支付后再来检查");
        }
        if (!careOrderItem.getItemType().equals(ApiConstant.CO_TYPE_CHECK)) {
            return AjaxResult.fail("【" + itemId + "】的单号不是检查项目，请核对后再查询");
        }
        CareOrder careOrder = this.careService.queryCareOrderByCoId(careOrderItem.getCoId());
        CareHistory careHistory = this.careService.queryCareHistoryByChId(careOrder.getChId());
        CheckResult checkResult = new CheckResult();
        checkResult.setItemId(itemId);
        checkResult.setCheckItemId(Integer.valueOf(careOrderItem.getItemRefId()));
        checkResult.setCheckItemName(careOrderItem.getItemName());
        checkResult.setPatientId(careOrder.getPatientId());
        checkResult.setPatientName(careOrder.getPatientName());
        checkResult.setPrice(careOrderItem.getPrice());
        checkResult.setRegId(careHistory.getRegId());
        checkResult.setResultStatus(ApiConstant.RESULT_STATUS_0);//检查中
        checkResult.setCreateTime(DateUtil.date());
        checkResult.setCreateBy(ShiroSecurityUtil.getCurrentUserName());
        return AjaxResult.toAjax(checkResultService.saveCheckResult(checkResult));
    }

    /**
     * 分页查询所有检查中的项目
     */
    @PostMapping("queryAllCheckingResultForPage")
//    @HystrixCommand
    public AjaxResult queryAllCheckingResultForPage(@RequestBody CheckResult checkResultDto) {
        checkResultDto.setResultStatus(ApiConstant.RESULT_STATUS_0);//检查中的
        DataGridViewVO dataGridView = this.checkResultService.queryAllCheckResultForPage(checkResultDto);
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());
    }

    /**
     * 上传检查结果并完成检查
     */
    @PostMapping("completeCheckResult")
//    @HystrixCommand
    public AjaxResult completeCheckResult(@RequestBody @Validated CheckResultForm checkResultFormDto) {
        checkResultFormDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.checkResultService.completeCheckResult(checkResultFormDto));
    }

    /**
     * 查询所有检查中的和检查完成了的项目
     */
    @PostMapping("queryAllCheckResultForPage")
//    @HystrixCommand
    public AjaxResult queryAllCheckResultForPage(@RequestBody CheckResult checkResultDto) {
        DataGridViewVO dataGridView = this.checkResultService.queryAllCheckResultForPage(checkResultDto);
        return AjaxResult.success("查询成功", dataGridView.getData(), dataGridView.getTotal());
    }

    /**
     * 根据检查单号查询要检查的项目详情
     */
    @GetMapping("queryCheckItemByItemId/{itemId}")
    @HystrixCommand
    public AjaxResult queryCheckItemByItemId(@PathVariable String itemId) {
        CareOrderItem careOrderItem = this.careService.queryCareOrderItemByItemId(itemId);
        if (careOrderItem == null) {
            return AjaxResult.fail("【" + itemId + "】的检查单号的数据不存在，请核对后再查询");
        }
        if (!careOrderItem.getStatus().equals(ApiConstant.ORDER_DETAILS_STATUS_1)) {
            return AjaxResult.fail("【" + itemId + "】的检查单号的没有支付，请支付后再来检查");
        }
        if (!careOrderItem.getItemType().equals(ApiConstant.CO_TYPE_CHECK)) {
            return AjaxResult.fail("【" + itemId + "】的单号不是检查项目，请核对后再查询");
        }
        CareOrder careOrder = this.careService.queryCareOrderByCoId(careOrderItem.getCoId());
        CareHistory careHistory = this.careService.queryCareHistoryByChId(careOrder.getChId());
        Map<String, Object> res = new HashMap<>();
        res.put("item", careOrderItem);
        res.put("careOrder", careOrder);
        res.put("careHistory", careHistory);
        return AjaxResult.success(res);
    }
}
