package cn.cloud9.service;

import cn.cloud9.domain.CareHistory;
import cn.cloud9.domain.CareOrder;
import cn.cloud9.domain.form.CareOrderForm;
import cn.cloud9.domain.CareOrderItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月29日 下午 08:52
 */
public interface CareService extends IService<CareHistory> {

    /**
     * 根据患者ID查询历史病历
     * @param patientId
     * @return
     */
    List<CareHistory> queryCareHistoryByPatientId(String patientId);

    /**
     * 保存或更新病例信息
     * @param careHistoryDto
     * @return
     */
    CareHistory saveOrUpdateCareHistory(CareHistory careHistoryDto);

    /**
     * 根据挂号单位ID查询对应的病历信息
     * @param regId
     * @return
     */
    CareHistory queryCareHistoryByRegId(String regId);

    /**
     * 根据病历信息查询处方信息
     * @param chId
     * @return
     */
    List<CareOrder> queryCareOrdersByChId(String chId);

    /**
     * 根据处方ID查询处方详情信息
     * @param coId
     * @return
     */
    List<CareOrderItem> queryCareOrderItemsByCoId(String coId, String status);


    /**
     * 根据病例ID查询病历信息
     * @param chId
     * @return
     */
    CareHistory queryCareHistoryByChId(String chId);

    /**
     * 保存处方及详情信息
     * @param careOrderFormDto
     * @return
     */
    int saveCareOrderItem(CareOrderForm careOrderFormDto);

    /**
     * 根据处方详情ID查询处方详情数据
     * @param itemId
     * @return
     */
    CareOrderItem queryCareOrderItemByItemId(String itemId);
    /**
     * 根据详情ID删除详情信息
     * @param itemId
     * @return
     */
    int deleteCareOrderItemByItemId(String itemId);

    /**
     * 完成就诊
     * @param regId
     * @return
     */
    int visitComplete(String regId);

    /**
     * 发药
     * @param itemIds
     */
    String doMedicine(List<String> itemIds);

    /**
     * 根据条件查询所有项目
     * @param coType 项目类型 0 药品  1检查
     * @param status 支付状态
     * @return
     */
    List<CareOrderItem> queryCareOrderItemsByStatus(String coType, String status);

    /**
     * 根据ID查询处方详情
     * @param coId
     * @return
     */
    CareOrder queryCareOrderByCoId(String coId);
}
