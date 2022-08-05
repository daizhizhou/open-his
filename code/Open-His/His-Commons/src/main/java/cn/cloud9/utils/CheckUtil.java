package cn.cloud9.utils;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月23日 下午 05:19
 */
public class CheckUtil {
    public static boolean isEmpty(Object val) {
        return null == val;
    }

    public static boolean isEmptyArray(Object[] val) {
        return val == null || val.length == 0;
    }

    public static boolean isEmptyString(String path) {
        if (isEmpty(path)) return false;
        path = path.trim();
        return "".equals(path)
                || "null".equals(path)
                || "undefined".equals(path);
    }
}
