package cn.cloud9.controller.erp;

import cn.cloud9.config.spring.HystrixSupport;
import cn.cloud9.domain.InventoryLog;
import cn.cloud9.service.InventoryLogService;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月26日 下午 11:57
 */
@RestController
@RequestMapping("erp/inventoryLog")
public class InventoryLogController extends HystrixSupport {
    @Reference//使用dubbo的引用
    private InventoryLogService inventoryLogService;

    /**
     * 分页查询
     */
    @GetMapping("listInventoryLogForPage")
    @HystrixCommand
    public AjaxResult listMedicinesForPage(InventoryLog inventoryLogDto) {
        DataGridViewVO gridView = this.inventoryLogService.listInventoryLogPage(inventoryLogDto);
        return AjaxResult.success("查询成功", gridView.getData(), gridView.getTotal());
    }

}
