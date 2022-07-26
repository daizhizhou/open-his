package cn.cloud9.controller.erp;

import cn.cloud9.config.spring.HystrixSupport;
import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.Purchase;
import cn.cloud9.domain.PurchaseItem;
import cn.cloud9.service.PurchaseService;
import cn.cloud9.util.ShiroSecurityUtil;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月26日 下午 09:53
 */
@RestController
@RequestMapping("erp/purchase")
public class PurchaseController extends HystrixSupport {
    @Reference//使用dubbo的引用
    private PurchaseService purchaseService;

    /**
     * 分页查询
     */
    @GetMapping("listPurchaseForPage")
    @HystrixCommand
    public AjaxResult listPurchaseForPage(Purchase purchaseDto) {
        DataGridViewVO gridView = this.purchaseService.listPurchasePage(purchaseDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }


    /**
     * 分页查询待审核的单据
     */
    @GetMapping("listPurchasePendingForPage")
    @HystrixCommand
    public AjaxResult listPurchasePendingForPage(Purchase purchaseDto) {
        purchaseDto.setStatus(ApiConstant.STOCK_PURCHASE_STATUS_2);
        DataGridViewVO gridView = this.purchaseService.listPurchasePage(purchaseDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }


    /**
     * 提交审核
     * 什么条件下可以提交审核
     */
    @PostMapping("doAudit/{purchaseId}")
    @HystrixCommand
    public AjaxResult doAudit(@PathVariable String purchaseId) {
        //根据purchaseId查询单据对象
        Purchase purchase = this.purchaseService.getPurchaseById(purchaseId);
        if (purchase.getStatus().equals(ApiConstant.STOCK_PURCHASE_STATUS_1) || purchase.getStatus().equals(ApiConstant.STOCK_PURCHASE_STATUS_4)) {
            int i = this.purchaseService.doAudit(purchaseId, ShiroSecurityUtil.getCurrentSimpleUser());
            return AjaxResult.toAjax(i);
        } else {
            return AjaxResult.fail("当前单据状态不是【未提交】或【审核失败】状态，不能提交审核");
        }
    }

    /**
     * 作废
     * 什么条件下可以提交作废
     */
    @PostMapping("doInvalid/{purchaseId}")
    @HystrixCommand
    public AjaxResult doInvalid(@PathVariable String purchaseId) {
        //根据purchaseId查询单据对象
        Purchase purchase = this.purchaseService.getPurchaseById(purchaseId);
        if (purchase.getStatus().equals(ApiConstant.STOCK_PURCHASE_STATUS_1) || purchase.getStatus().equals(ApiConstant.STOCK_PURCHASE_STATUS_4)) {
            int i = this.purchaseService.doInvalid(purchaseId);
            return AjaxResult.toAjax(i);
        } else {
            return AjaxResult.fail("当前单据状态不是【未提交】或【审核失败】状态，不能提交审核");
        }
    }

    /**
     * 审核通过
     * 什么条件下可以审核通过  状态必须是待审核单据
     */
    @PostMapping("auditPass/{purchaseId}")
    @HystrixCommand
    public AjaxResult auditPass(@PathVariable String purchaseId) {
        //根据purchaseId查询单据对象
        Purchase purchase = this.purchaseService.getPurchaseById(purchaseId);
        if (purchase.getStatus().equals(ApiConstant.STOCK_PURCHASE_STATUS_2)) {
            int i = this.purchaseService.auditPass(purchaseId);
            return AjaxResult.toAjax(i);
        } else {
            return AjaxResult.fail("当前单据状态不是【待审核】状态，不能审核");
        }
    }

    /**
     * 审核不通过
     * 什么条件下可以审核通过  状态必须是待审核单据
     */
    @PostMapping("auditNoPass/{purchaseId}/{auditMsg}")
    @HystrixCommand
    public AjaxResult auditNoPass(@PathVariable String purchaseId, @PathVariable String auditMsg) {
        //根据purchaseId查询单据对象
        Purchase purchase = this.purchaseService.getPurchaseById(purchaseId);
        if (purchase.getStatus().equals(ApiConstant.STOCK_PURCHASE_STATUS_2)) {
            int i = this.purchaseService.auditNoPass(purchaseId, auditMsg);
            return AjaxResult.toAjax(i);
        } else {
            return AjaxResult.fail("当前单据状态不是【待审核】状态，不能审核");
        }
    }


    /**
     * 根据ID查询一个采购信息详情
     */
    @GetMapping("getPurchaseItemById/{purchaseId}")
    @HystrixCommand
    public AjaxResult getPurchaseItemById(@PathVariable String purchaseId) {
        List<PurchaseItem> list = this.purchaseService.getPurchaseItemById(purchaseId);
        return AjaxResult.success(list);
    }
}
