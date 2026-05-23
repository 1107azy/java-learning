package com.example.hellospringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 核心注解：标记这是 Spring Boot 启动类
@SpringBootApplication
public class HelloSpringbootApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloSpringbootApplication.class, args);
    }
}
