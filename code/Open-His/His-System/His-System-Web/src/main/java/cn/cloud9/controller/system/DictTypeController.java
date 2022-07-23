package cn.cloud9.controller.system;

import cn.cloud9.domain.SystemDictType;
import cn.cloud9.service.SystemDictTypeService;
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
 * @date 2022年07月23日 下午 10:37
 */
@RestController
@RequestMapping("/system/dict/type")
public class DictTypeController {
    @Resource
    private SystemDictTypeService systemDictTypeService;

    /**
     * 分页查询
     */
    @GetMapping("/listForPage")
    public AjaxResult listForPage(SystemDictType systemDictType){
        DataGridViewVO gridView = this.systemDictTypeService.listPage(systemDictType);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }

    /**
     * 添加
     */
    @PostMapping("/addDictType")
    public AjaxResult addDictType(@Validated SystemDictType dictTypeDto) {
        if (systemDictTypeService.checkDictTypeUnique(dictTypeDto.getDictId(), dictTypeDto.getDictType())) {
            return AjaxResult.fail("新增字典【" + dictTypeDto.getDictName() + "】失败，字典类型已存在");
        }
        dictTypeDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemDictTypeService.insert(dictTypeDto));
    }

    /**
     * 修改
     */
    @PutMapping("/updateDictType")
    public AjaxResult updateDictType(@Validated SystemDictType dictTypeDto) {
        if (systemDictTypeService.checkDictTypeUnique(dictTypeDto.getDictId(), dictTypeDto.getDictType())) {
            return AjaxResult.fail("修改字典【" + dictTypeDto.getDictName() + "】失败，字典类型已存在");
        }
        dictTypeDto.setSimpleUser(ShiroSecurityUtil.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.systemDictTypeService.update(dictTypeDto));
    }


    /**
     * 根据ID查询一个字典信息
     */
    @GetMapping("/getOne/{dictId}")
    public AjaxResult getDictType(@PathVariable @Validated @NotNull(message = "字典ID不能为空") Long dictId) {
        return AjaxResult.success(this.systemDictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 删除
     */
    @DeleteMapping("/deleteDictTypeByIds/{dictIds}")
    public AjaxResult updateDictType(
        @PathVariable
        @Validated
        @NotEmpty(message = "要删除的ID不能为空")
        Long[] dictIds
    ) {
        return AjaxResult.toAjax(this.systemDictTypeService.deleteDictTypeByIds(dictIds));
    }

    /**
     * 查询所有可用的字典类型
     */
    @GetMapping("/selectAllDictType")
    public AjaxResult selectAllDictType(){
        return AjaxResult.success(this.systemDictTypeService.list());
    }

}
