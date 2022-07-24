package cn.cloud9.controller.system;

import cn.cloud9.aspect.annotation.SystemLog;
import cn.cloud9.aspect.enums.BusinessType;
import cn.cloud9.domain.SystemDictData;
import cn.cloud9.service.SystemDictDataService;
import cn.cloud9.util.ShiroSecurityUtil;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月24日 上午 09:47
 */
@RestController
@RequestMapping("system/dict/data")
public class DictDataController {

    @Resource
    private SystemDictDataService systemDictDataService;

    /**
     * 分页查询
     */
    @GetMapping("listForPage")
    public AjaxResult listForPage(SystemDictData dictDataDto){
        DataGridViewVO gridView = this.systemDictDataService.listPage(dictDataDto);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }
    /**
     * 添加
     */
    @PostMapping("addDictData")
    @SystemLog(title = "新增字典数据", businessType = BusinessType.INSERT)
    public AjaxResult addDictData(@Validated SystemDictData dictDataDto) {
        dictDataDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemDictDataService.insert(dictDataDto));
    }

    /**
     * 修改
     */
    @PutMapping("updateDictData")
    @SystemLog(title = "更新字典数据",businessType = BusinessType.UPDATE)
    public AjaxResult updateDictData(@Validated SystemDictData dictDataDto) {
        dictDataDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemDictDataService.update(dictDataDto));
    }


    /**
     * 根据ID查询一个字典信息
     */
    @GetMapping("getOne/{dictCode}")
    public AjaxResult getDictData(@PathVariable @Validated @NotNull(message = "字典ID不能为空") Long dictCode) {
        return AjaxResult.success(this.systemDictDataService.selectDictDataById(dictCode));
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteDictDataByIds/{dictCodeIds}")
    @SystemLog(title = "删除字典数据",businessType = BusinessType.DELETE)
    public AjaxResult updateDictData(@PathVariable @Validated @NotEmpty(message = "要删除的ID不能为空") Long[] dictCodeIds) {
        return AjaxResult.toAjax(this.systemDictDataService.deleteDictDataByIds(dictCodeIds));
    }

    /**
     * 查询所有可用的字典类型
     */
    @GetMapping("getDataByType/{dictType}")
    public AjaxResult getDataByType(
        @PathVariable
        @Validated
        @NotEmpty(message = "字典类型不能为空")
        String dictType
    ) {
        return AjaxResult.success(this.systemDictDataService.selectDictDataByDictType(dictType));
    }
}
