package cn.cloud9.controller.system;

import cn.cloud9.config.spring.BaseController;
import cn.cloud9.service.UploadService;
import cn.cloud9.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 03:22
 */
@RestController
@RequestMapping("system/upload")
public class UploadController extends BaseController {
    @Resource
    private UploadService uploadService;

    /**
     * 文件图片 ---检查结果
     */
    @PostMapping("doUploadImage")
    public AjaxResult uploadFile(MultipartFile mf){
        Map<String,Object> map=new HashMap<>();
        if(null!=mf){
            map.put("name",mf.getOriginalFilename());
            String path = this.uploadService.uploadImage(mf);
            map.put("url",path);
            System.out.println(map);
            return  AjaxResult.success(map);
        }else{
            return AjaxResult.error("上传文件失败");
        }
    }

}
