package com.liyz.dubbo.service.member;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 注释:启动类
 *
 * @author liyangzhen
 * @version 1.0.0
 * @date 2020/7/3 11:07
 */
@MapperScan(basePackages = {"com.liyz.dubbo.service.member.dao"})
@EnableDubbo(scanBasePackages = {"com.liyz.dubbo.service.member.provider"})
@SpringBootApplication(scanBasePackages = {"com.liyz.dubbo.common", "com.liyz.dubbo.service.member"})
public class MemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }
}
