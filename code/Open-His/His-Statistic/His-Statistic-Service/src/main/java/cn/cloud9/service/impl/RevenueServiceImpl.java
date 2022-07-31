package cn.cloud9.service.impl;

import cn.cloud9.contants.ApiConstant;
import cn.cloud9.domain.Income;
import cn.cloud9.domain.Refund;
import cn.cloud9.domain.RevenueQueryDto;
import cn.cloud9.mapper.RevenueMapper;
import cn.cloud9.service.RevenueService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月31日 下午 07:28
 */
@Component
@Service
public class RevenueServiceImpl implements RevenueService {
    @Resource
    private RevenueMapper revenueMapper;

    @Override
    public Map<String, Object> queryAllRevenueData(RevenueQueryDto revenueQueryDto) {
        //最后的返回对象
        Map<String, Object> map = new HashMap<>();

        //查询收入的
        List<Income> incomes = this.revenueMapper.queryIncome(revenueQueryDto);
        //查询退费的
        List<Refund> refunds = this.revenueMapper.queryRefund(revenueQueryDto);

        //声明需要的数据对象
        Double totalRevenue = 0.00; //合计收入
        Double toll = 0.00;//总收入
        Double refund = 0.00;//总退费
        Double cashIncome = 0.00;//现金支付
        Double alipayIncome = 0.00;//支付宝支付
        Double cashRefund = 0.00;//现金退费
        Double alipayRefund = 0.00;//支付宝退费
        Integer incomeChanelCash = 0;//现金收取次数
        Integer incomeChanelAlipay = 0;//支付宝收取的次数
        for (Income income : incomes) {
            toll += income.getOrderAmount();
            if (income.getPayType().equals(ApiConstant.PAY_TYPE_0)) {//现金
                cashIncome += income.getOrderAmount();
                incomeChanelCash++;
            } else if (income.getPayType().equals(ApiConstant.PAY_TYPE_1)) {//支付宝
                alipayIncome += income.getOrderAmount();
                incomeChanelAlipay++;
            }
        }
        for (Refund refund1 : refunds) {
            refund += refund1.getBackAmount();
            if (refund1.getBackType().equals(ApiConstant.PAY_TYPE_0)) {//现金退费
                cashRefund += refund1.getBackAmount();
            } else if (refund1.getBackType().equals(ApiConstant.PAY_TYPE_1)) {//支付宝退费
                alipayRefund += refund1.getBackAmount();
            }
        }
        //计算合计收入=总收入-总退费
        totalRevenue = toll - refund;
        Map<String, Object> revenueObj = new HashMap<>();
        revenueObj.put("totalRevenue", totalRevenue);
        Map<String, Object> overview = new HashMap<>();
        overview.put("toll", toll);
        overview.put("refund", refund);

        Map<String, Object> channel = new HashMap<>();
        channel.put("cashIncome", cashIncome);
        channel.put("alipayIncome", alipayIncome);
        channel.put("cashRefund", cashRefund);
        channel.put("alipayRefund", alipayRefund);

        revenueObj.put("overview", overview);
        revenueObj.put("channel", channel);
        map.put("revenueObj", revenueObj);

        /*******收支概况***************/
        Map<String, Object> revenueOverview = new HashMap<>();
        revenueOverview.put("title", "收支概况");
        List<Map<String, Object>> revenueOverviewData = new ArrayList<>();
        Map<String, Object> revenueOverviewData1 = new HashMap<>();
        revenueOverviewData1.put("name", "收费金额");
        revenueOverviewData1.put("value", toll);
        Map<String, Object> revenueOverviewData2 = new HashMap<>();
        revenueOverviewData2.put("name", "退费金额");
        revenueOverviewData2.put("value", refund);
        revenueOverviewData.add(revenueOverviewData1);
        revenueOverviewData.add(revenueOverviewData2);
        revenueOverview.put("data", revenueOverviewData);
        //放到返回的map里面
        map.put("revenueOverview", revenueOverview);

        /*******收入渠道***************/
        Map<String, Object> incomeChanel = new HashMap<>();
        incomeChanel.put("title", "收入渠道");
        List<Map<String, Object>> incomeChanelData = new ArrayList<>();
        Map<String, Object> incomeChanelData1 = new HashMap<>();
        incomeChanelData1.put("name", "现金笔数");
        incomeChanelData1.put("value", incomeChanelCash);
        Map<String, Object> incomeChanelData2 = new HashMap<>();
        incomeChanelData2.put("name", "支付宝笔数");
        incomeChanelData2.put("value", incomeChanelAlipay);
        incomeChanelData.add(incomeChanelData1);
        incomeChanelData.add(incomeChanelData2);
        incomeChanel.put("data", incomeChanelData);
        //放到返回的map里面
        map.put("incomeChanel", incomeChanel);

        /*******退款金额和渠道***************/
        Map<String, Object> refundMap = new HashMap<>();
        refundMap.put("title", "退款");
        List<Map<String, Object>> refundMapData = new ArrayList<>();
        Map<String, Object> refundMapData1 = new HashMap<>();
        refundMapData1.put("name", "现金退款");
        refundMapData1.put("value", cashRefund);
        Map<String, Object> refundMapData2 = new HashMap<>();
        refundMapData2.put("name", "支付宝退款");
        refundMapData2.put("value", alipayRefund);
        refundMapData.add(refundMapData1);
        refundMapData.add(refundMapData2);
        refundMap.put("data", refundMapData);
        //放到返回的map里面
        map.put("refund", refundMap);
        return map;
    }
}
