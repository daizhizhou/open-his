package cn.cloud9;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月21日 下午 11:21
 */
@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = {"cn.cloud9.mapper"})
public class HisSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(HisSystemApplication.class, args);
    }
}
