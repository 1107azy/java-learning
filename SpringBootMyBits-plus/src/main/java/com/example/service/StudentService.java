package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Student;

public interface StudentService extends IService<Student> {
    // 可以不写任何方法，直接用 IService 提供的通用 CRUD
}
