package cn.cloud9.service;

import cn.cloud9.domain.SystemNotice;
import cn.cloud9.vo.DataGridViewVO;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SystemNoticeService extends IService<SystemNotice>{

    /**
     * 分页查询
     * @param noticeDto
     * @return
     */
    DataGridViewVO listNoticePage(SystemNotice noticeDto);

    /**
     * 根据ID查询
     * @param noticeId
     * @return
     */
    SystemNotice getOne(Long noticeId);

    /**
     * 添加
     * @param noticeDto
     * @return
     */
    int addNotice(SystemNotice noticeDto);

    /**
     * 修改
     * @param noticeDto
     * @return
     */
    int updateNotice(SystemNotice noticeDto);

    /**
     * 根据ID删除
     * @param noticeIds
     * @return
     */
    int deleteNoticeByIds(Long[] noticeIds);

}
