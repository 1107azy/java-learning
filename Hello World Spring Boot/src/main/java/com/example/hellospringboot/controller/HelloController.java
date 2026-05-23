package com.example.hellospringboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

// 核心注解：标记这是 REST 接口控制器
@RestController
public class HelloController {

    // GET 请求，路径 /hello，返回固定字符串
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot!";
    }

    // GET 请求，路径 /hello/{name}，路径参数 name，返回拼接后的字符串
    @GetMapping("/hello/{name}")
    public String helloWithName(@PathVariable String name) {
        return "Hello, " + name + "!";
    }
}