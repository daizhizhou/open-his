package cn.cloud9.utils;

import cn.hutool.core.date.DateUtil;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月29日 下午 08:51
 */
public class HisDateUtil {
    /**
     * 得到时段
     * 1上午
     * 2下午
     * 3晚上
     */
    public static String getCurrentTimeType() {
        int hour = DateUtil.hour(DateUtil.date(), true);
        if (hour >= 8 && hour < 12) {
            return "1";
        } else if (hour >= 12 && hour < 18) {
            return "2";
        } else {
            return "3";
        }
    }
}
