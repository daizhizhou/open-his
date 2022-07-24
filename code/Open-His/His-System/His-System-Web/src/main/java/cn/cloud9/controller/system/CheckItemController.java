package cn.cloud9.controller.system;

import cn.cloud9.aspect.annotation.SystemLog;
import cn.cloud9.aspect.enums.BusinessType;
import cn.cloud9.domain.SystemCheckItem;
import cn.cloud9.service.SystemCheckItemService;
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
 * @date 2022年07月24日 下午 08:47
 */
@RestController
@RequestMapping("system/checkItem")
public class CheckItemController {
    @Resource
    private SystemCheckItemService systemCheckItemService;

    /**
     * 分页查询
     */
    @GetMapping("listCheckItemForPage")
    public AjaxResult listCheckItemForPage(SystemCheckItem checkItemDto) {
        DataGridViewVO gridView = this.systemCheckItemService.listCheckItemPage(checkItemDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }

    /**
     * 添加
     */
    @PostMapping("addCheckItem")
    @SystemLog(title = "添加检查项目", businessType = BusinessType.INSERT)
    public AjaxResult addCheckItem(@Validated SystemCheckItem checkItemDto) {
        checkItemDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemCheckItemService.addCheckItem(checkItemDto));
    }

    /**
     * 修改
     */
    @PutMapping("updateCheckItem")
    @SystemLog(title = "修改检查项目", businessType = BusinessType.UPDATE)
    public AjaxResult updateCheckItem(@Validated SystemCheckItem checkItemDto) {
        checkItemDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemCheckItemService.updateCheckItem(checkItemDto));
    }


    /**
     * 根据ID查询一个检查项目信息
     */
    @GetMapping("getCheckItemById/{checkItemId}")
    public AjaxResult getCheckItemById(@PathVariable @Validated @NotNull(message = "检查项目ID不能为空") Long checkItemId) {
        return AjaxResult.success(this.systemCheckItemService.getOne(checkItemId));
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteCheckItemByIds/{checkItemIds}")
    @SystemLog(title = "删除检查项目", businessType = BusinessType.DELETE)
    public AjaxResult deleteCheckItemByIds(@PathVariable @Validated @NotEmpty(message = "要删除的ID不能为空") Long[] checkItemIds) {
        return AjaxResult.toAjax(this.systemCheckItemService.deleteCheckItemByIds(checkItemIds));
    }

    /**
     * 查询所有可用的检查项目
     */
    @GetMapping("selectAllCheckItem")
    public AjaxResult selectAllCheckItem() {
        List<SystemCheckItem> checkItems = this.systemCheckItemService.queryAllCheckItems();
        return AjaxResult.success(checkItems);
    }

}
