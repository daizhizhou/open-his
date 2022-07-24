package cn.cloud9.controller.system;

import cn.cloud9.aspect.annotation.SystemLog;
import cn.cloud9.aspect.enums.BusinessType;
import cn.cloud9.domain.SystemRegistItem;
import cn.cloud9.service.SystemRegistItemService;
import cn.cloud9.util.ShiroSecurityUtil;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月24日 下午 09:05
 */
@RestController
@RequestMapping("system/registeredItem")
public class RegistItemController {
    @Resource
    private SystemRegistItemService systemRegistItemService;

    /**
     * 分页查询
     */
    @GetMapping("listRegisteredItemForPage")
    public AjaxResult listRegisteredItemForPage(SystemRegistItem registeredItemDto) {
        DataGridViewVO gridView = this.systemRegistItemService.listRegisteredItemPage(registeredItemDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }

    /**
     * 添加
     */
    @PostMapping("addRegisteredItem")
    @SystemLog(title = "添加挂号项目", businessType = BusinessType.INSERT)
    public AjaxResult addRegisteredItem(@Validated SystemRegistItem registeredItemDto) {
        registeredItemDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemRegistItemService.addRegisteredItem(registeredItemDto));
    }

    /**
     * 修改
     */
    @PutMapping("updateRegisteredItem")
    @SystemLog(title = "修改挂号项目", businessType = BusinessType.UPDATE)
    public AjaxResult updateRegisteredItem(@Validated SystemRegistItem registeredItemDto) {
        registeredItemDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemRegistItemService.updateRegisteredItem(registeredItemDto));
    }


    /**
     * 根据ID查询一个挂号项目信息
     */
    @GetMapping("getRegisteredItemById/{registeredItemId}")
    public AjaxResult getRegisteredItemById(@PathVariable @Validated @NotNull(message = "挂号项目ID不能为空") Long registeredItemId) {
        return AjaxResult.success(this.systemRegistItemService.getOne(registeredItemId));
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteRegisteredItemByIds/{registeredItemIds}")
    @SystemLog(title = "删除挂号项目", businessType = BusinessType.DELETE)
    public AjaxResult deleteRegisteredItemByIds(@PathVariable @Validated @NotEmpty(message = "要删除的ID不能为空") Long[] registeredItemIds) {
        return AjaxResult.toAjax(this.systemRegistItemService.deleteRegisteredItemByIds(registeredItemIds));
    }

    /**
     * 查询所有可用的挂号项目
     */
    @GetMapping("selectAllRegisteredItem")
    public AjaxResult selectAllRegisteredItem() {
        List<SystemRegistItem> checkItems = this.systemRegistItemService.queryAllRegisteredItems();
        return AjaxResult.success(checkItems);
    }
}
