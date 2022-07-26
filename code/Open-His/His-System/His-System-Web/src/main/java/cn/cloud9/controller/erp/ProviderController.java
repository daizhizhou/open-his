package cn.cloud9.controller.erp;

import cn.cloud9.aspect.annotation.SystemLog;
import cn.cloud9.aspect.enums.BusinessType;
import cn.cloud9.config.spring.HystrixSupport;
import cn.cloud9.domain.Provider;
import cn.cloud9.service.ProviderService;
import cn.cloud9.util.ShiroSecurityUtil;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
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
 * @date 2022年07月26日 下午 09:36
 */
@RestController
@RequestMapping("erp/provider")
public class ProviderController extends HystrixSupport {
    @Reference//使用dubbo的引用
    private ProviderService providerService;


    @Override
    public AjaxResult serviceUnavailableFallBack() {
        return AjaxResult.fail("服务器内部异常，请联系管理员");
    }

    /**
     * 分页查询
     */
    @GetMapping("listProviderForPage")
    @HystrixCommand
    public AjaxResult listProviderForPage(Provider providerDto) {
        DataGridViewVO gridView = this.providerService.listProviderPage(providerDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }

    /**
     * 添加
     */
    @PostMapping("addProvider")
    @HystrixCommand
    @SystemLog(title = "添加供应商", businessType = BusinessType.INSERT)
    public AjaxResult addProvider(@Validated Provider providerDto) {
        providerDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.providerService.addProvider(providerDto));
    }

    /**
     * 修改
     */
    @PutMapping("updateProvider")
    @HystrixCommand
    @SystemLog(title = "修改供应商", businessType = BusinessType.UPDATE)
    public AjaxResult updateProvider(@Validated Provider providerDto) {
        providerDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.providerService.updateProvider(providerDto));
    }


    /**
     * 根据ID查询一个供应商信息
     */
    @GetMapping("getProviderById/{providerId}")
    @HystrixCommand
    public AjaxResult getProviderById(@PathVariable @Validated @NotNull(message = "供应商ID不能为空") Long providerId) {
        return AjaxResult.success(this.providerService.getOne(providerId));
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteProviderByIds/{providerIds}")
    @HystrixCommand
    @SystemLog(title = "删除供应商", businessType = BusinessType.DELETE)
    public AjaxResult deleteProviderByIds(@PathVariable @Validated @NotEmpty(message = "要删除的ID不能为空") Long[] providerIds) {
        return AjaxResult.toAjax(this.providerService.deleteProviderByIds(providerIds));
    }

    /**
     * 查询所有可用的供应商
     */
    @HystrixCommand
    @GetMapping("selectAllProvider")
    public AjaxResult selectAllProvider() {
        return AjaxResult.success(this.providerService.selectAllProvider());
    }

}
