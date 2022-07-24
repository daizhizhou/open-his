package cn.cloud9.controller.system;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.SystemLoginInfo;
import cn.cloud9.service.SystemLoginInfoService;
import cn.cloud9.vo.AjaxResult;
import cn.cloud9.vo.DataGridViewVO;
import cn.hutool.core.date.DateUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月24日 下午 12:23
 */
@Log4j2
@RestController
@RequestMapping("system/loginInfo")
public class LoginInfoController {
    @Resource
    private SystemLoginInfoService systemLoginInfoService;

    /**
     * 分页查询
     */
    @GetMapping("listForPage")
    public AjaxResult listForPage(SystemLoginInfo loginInfoDto){
        DataGridViewVO gridView = systemLoginInfoService.listForPage(loginInfoDto);
        return AjaxResult.success("查询成功",gridView.getData(),gridView.getTotal());
    }

    /**
     * 删除
     */
    @DeleteMapping("deleteLoginInfoByIds/{infoIds}")
    public AjaxResult deleteLoginInfoByIds(@PathVariable Long[] infoIds){
        return AjaxResult.toAjax(this.systemLoginInfoService.deleteLoginInfoByIds(infoIds));
    }
    /**
     * 清空删除
     */
    @DeleteMapping("clearLoginInfo")
    public AjaxResult clearLoginInfo(){
        return AjaxResult.toAjax(this.systemLoginInfoService.clearLoginInfo());
    }

}
