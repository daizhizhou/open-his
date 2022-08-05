package cn.cloud9.controller.system;

import cn.cloud9.config.spring.BaseController;
import cn.cloud9.service.UploadService;
import cn.cloud9.utils.CheckUtil;
import cn.cloud9.vo.AjaxResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    /**
     * system/upload/doUploadImage2
     * 上传图片
     * @param multipartFile
     * @return
     */
    @PostMapping(value = "doUploadImage2", consumes = "multipart/form-data")
    public AjaxResult uploadImage(@RequestParam("image") MultipartFile multipartFile) {
        if (CheckUtil.isEmpty(multipartFile)) return AjaxResult.error("未收到上传文件！");
        return AjaxResult.success(new ConcurrentHashMap<String, Object>() {
            private static final long serialVersionUID = 846411610700615463L;
            {
                this.put("name", multipartFile.getOriginalFilename());
                String path = uploadService.uploadImage(multipartFile);
                this.put("url",path);
            }
        });
    }

    /**
     * system/upload/deleteImage
     * @param path
     * @return
     */
    @PostMapping("deleteImage")
    public AjaxResult deleteImage(@RequestParam("path") String path) {
        if (CheckUtil.isEmptyString(path)) return AjaxResult.error("请提供文件路径！");
        uploadService.deleteFile(path);
        return  AjaxResult.success("删除成功");
    }

}
