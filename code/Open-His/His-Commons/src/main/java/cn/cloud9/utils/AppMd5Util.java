package cn.cloud9.utils;

import cn.hutool.core.lang.UUID;
import org.apache.shiro.crypto.hash.Md5Hash;
/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月24日 下午 05:28
 */
public class AppMd5Util {
    /**
     * 生成盐
     */
    public static String createSalt(){
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }

    /**
     * 生成加密字符串
     */
    public static String md5(String source,String salt,Integer hashIterations){
        return new Md5Hash(source,salt,hashIterations).toString();
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(md5("907654","9ED13A792A6B438D9AC3FC1C90F2F06A",2));
    }
}
