package cn.cloud9.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月23日 下午 02:45
 */
public class IdGeneratorSnowflakeUtil {
    private static final Logger log = LoggerFactory.getLogger(IdGeneratorSnowflakeUtil.class);
    private static long workId = 0;
    private static long datacenterId = 1;
    private static Snowflake snowflake;

    static {
        try {
            workId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            log.info("当前机器的工作ID为:" + workId);
            snowflake=IdUtil.createSnowflake(workId, datacenterId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("当前机器的workId获取失败", e);
            workId = NetUtil.getLocalhostStr().hashCode();
        }
    }

    /**
     * 生成ID
     */
    public static synchronized Long snowflakeId() {
        return snowflake.nextId();
    }

    /**
     * 根据前缀前成ID
     */
    public static String generatorIdWithProfix(String profix) {
        return profix + snowflakeId();
    }
}
