package cn.cloud9.controller.erp;

import cn.cloud9.aspect.annotation.SystemLog;
import cn.cloud9.aspect.enums.BusinessType;
import cn.cloud9.config.spring.BaseController;
import cn.cloud9.config.spring.HystrixSupport;
import cn.cloud9.domain.Producter;
import cn.cloud9.service.ProducterService;
import cn.cloud9.util.ShiroSecurityUtil;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月25日 下午 09:49
 */

@RestController
@RequestMapping("erp/producter")
public class ProducterController extends HystrixSupport {

    @Reference
    private ProducterService producterService;

    @Override
    public AjaxResult serviceUnavailableFallBack() {
        return AjaxResult.fail("自定义失败信息");
    }

    /**
     * 分页查询
     */
    @HystrixCommand
    @GetMapping("listProducterForPage")
    public AjaxResult listProducterForPage(Producter producterDto) {
        DataGridViewVO gridView = this.producterService.listProducterPage(producterDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }

    /**
     * 添加
     */
    @HystrixCommand
    @PostMapping("addProducter")
    @SystemLog(title = "添加生产厂家", businessType = BusinessType.INSERT)
    public AjaxResult addProducter(@Validated Producter producterDto) {
        producterDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.producterService.addProducter(producterDto));
    }

    /**
     * 修改
     */
    @HystrixCommand
    @PutMapping("updateProducter")
    @SystemLog(title = "修改生产厂家", businessType = BusinessType.UPDATE)
    public AjaxResult updateProducter(@Validated Producter producterDto) {
        producterDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.producterService.updateProducter(producterDto));
    }


    /**
     * 根据ID查询一个生产厂家信息
     */
    @HystrixCommand
    @GetMapping("getProducterById/{producterId}")
    public AjaxResult getProducterById(@PathVariable @Validated @NotNull(message = "生产厂家ID不能为空") Long producterId) {
        return AjaxResult.success(this.producterService.getOne(producterId));
    }

    /**
     * 删除
     */
    @HystrixCommand
    @DeleteMapping("deleteProducterByIds/{producterIds}")
    @SystemLog(title = "删除生产厂家", businessType = BusinessType.DELETE)
    public AjaxResult deleteProducterByIds(@PathVariable @Validated @NotEmpty(message = "要删除的ID不能为空") Long[] producterIds) {
        return AjaxResult.toAjax(this.producterService.deleteProducterByIds(producterIds));
    }

    /**
     * 查询所有可用的生产厂家
     */
    @HystrixCommand
    @GetMapping("selectAllProducter")
    public AjaxResult selectAllProducter() {
        return AjaxResult.success(this.producterService.selectAllProducter());
    }

}
