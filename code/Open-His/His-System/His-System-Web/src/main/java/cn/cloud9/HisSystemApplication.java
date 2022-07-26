package cn.cloud9;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * @author OnCloud9
 * @description
 * @project Open-His
 * @date 2022年07月21日 下午 11:21
 */
@SpringBootApplication
@EnableHystrix // 启用豪猪熔断组件
@EnableCircuitBreaker // 启用断路保护件
@EnableDubbo
@MapperScan(basePackages = {"cn.cloud9.mapper"})
public class HisSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(HisSystemApplication.class, args);
    }
}
