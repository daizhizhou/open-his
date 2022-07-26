package cn.cloud9.controller.erp;

import cn.cloud9.aspect.annotation.SystemLog;
import cn.cloud9.aspect.enums.BusinessType;
import cn.cloud9.config.spring.HystrixSupport;
import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.Purchase;
import cn.cloud9.domain.PurchaseForm;
import cn.cloud9.domain.PurchaseItem;
import cn.cloud9.service.PurchaseService;
import cn.cloud9.util.ShiroSecurityUtil;
import cn.cloud9.utils.IdGeneratorSnowflakeUtil;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 生成单据号
     */
    @GetMapping("generatePurchaseId")
    public AjaxResult generatePurchaseId() {
        return AjaxResult.success(IdGeneratorSnowflakeUtil.generatorIdWithProfix(ApiConstant.ID_PREFIX_CG));
    }

    /**
     * 暂存采购单数据和详情
     */
    @PostMapping("addPurchase")
    @SystemLog(title = "采购单管理--暂存采购单位和详情数据", businessType = BusinessType.INSERT)
    public AjaxResult addPurchase(@RequestBody PurchaseForm purchaseFormDto) {
        //判断当前采购单的状态
        if (!checkPurchase(purchaseFormDto)) {
            return AjaxResult.fail("当前单据状态不是【未提交】或【审核失败】状态，不能进行修改");
        }
        purchaseFormDto.getPurchase().setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.purchaseService.addPurchaseAndItem(purchaseFormDto));

    }

    /**
     * 保存并提交审核采购单数据和详情
     */
    @PostMapping("addPurchaseToAudit")
    @SystemLog(title = "采购单管理--添加并提交审核采购单位和详情数据", businessType = BusinessType.INSERT)
    public AjaxResult addPurchaseToAudit(@RequestBody PurchaseForm purchaseFormDto) {
        //判断当前采购单的状态
        if (!checkPurchase(purchaseFormDto)) {
            return AjaxResult.fail("当前单据状态不是【未提交】或【审核失败】状态，不能进行修改");
        }
        purchaseFormDto.getPurchase().setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.purchaseService.addPurchaseAndItemToAudit(purchaseFormDto));

    }

    /**
     * 公共验证采购单的方法
     */
    private boolean checkPurchase(PurchaseForm purchaseFormDto) {
        String purchaseId = purchaseFormDto.getPurchase().getPurchaseId();
        Purchase purchase = this.purchaseService.getPurchaseById(purchaseId);
        //判断ID在数据库里面是否存在，如果存在，说明是再次暂存
        if (null == purchase) {
            return true;
        }
        if (purchase.getStatus().equals(ApiConstant.STOCK_PURCHASE_STATUS_1)
                || purchase.getStatus().equals(ApiConstant.STOCK_PURCHASE_STATUS_4)) {
            return true;
        }
        return false;
    }

    /**
     * 根据采购单号查询采购单信息和详情信息
     */
    @GetMapping("queryPurchaseAndItemByPurchaseId/{purchaseId}")
    public AjaxResult queryPurchaseAndItemByPurchaseId(@PathVariable String purchaseId) {
        Purchase purchase = this.purchaseService.getPurchaseById(purchaseId);
        if (null == purchase) {
            return AjaxResult.fail("单据号【" + purchaseId + "】不存在");
        } else {
            //查询详情
            List<PurchaseItem> items = this.purchaseService.getPurchaseItemById(purchaseId);
            Map<String, Object> res = new HashMap<>();
            res.put("purchase", purchase);
            res.put("items", items);
            return AjaxResult.success(res);
        }
    }

    /**
     * 入库
     */
    @PostMapping("doInventory/{purchaseId}")
    @SystemLog(title = "采购单管理--入库", businessType = BusinessType.UPDATE)
    public AjaxResult doInventory(@PathVariable String purchaseId) {
        Purchase purchase = this.purchaseService.getPurchaseById(purchaseId);
        if (purchase.getStatus().equals(ApiConstant.STOCK_PURCHASE_STATUS_3)) {
            //进行入库
            return AjaxResult.toAjax(this.purchaseService.doInventory(purchaseId, ShiroSecurityUtil.getCurrentSimpleUser()));
        } else if (purchase.getStatus().equals(ApiConstant.STOCK_PURCHASE_STATUS_6)) {
            return AjaxResult.fail("采购单【" + purchaseId + "】已入库，不能重复入库");
        } else {
            return AjaxResult.fail("采购单【" + purchaseId + "】没有审核通过，不能入库");
        }
    }
}
