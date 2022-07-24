package cn.cloud9.controller.system;


import cn.cloud9.domain.SystemOperateLog;
import cn.cloud9.service.SystemOperateLogService;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月24日 上午 11:50
 */
@Log4j2
@RestController
@RequestMapping("system/operLog")
public class OperateLogController {

    @Resource
    private SystemOperateLogService systemOperateLogService;

    /**
     * 分页查询
     */
    @GetMapping("listForPage")
    public AjaxResult listForPage(SystemOperateLog operLogDto){
        DataGridViewVO gridView = systemOperateLogService.listForPage(operLogDto);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteOperLogByIds/{infoIds}")
    public AjaxResult deleteOperLogByIds(@PathVariable Long[] infoIds){

        return AjaxResult.toAjax(this.systemOperateLogService.deleteOperateLogByIds(infoIds));
    }
    /**
     * 清空删除
     */
    @DeleteMapping("clearAllOperLog")
    public AjaxResult clearAllOperLog(){
        return AjaxResult.toAjax(this.systemOperateLogService.clearAllOperateLog());
    }



}
