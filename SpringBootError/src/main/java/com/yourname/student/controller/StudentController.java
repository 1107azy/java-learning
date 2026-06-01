package com.yourname.student.controller;

import com.yourname.student.common.Result;
import com.yourname.student.exception.ResourceNotFoundException;
import com.yourname.student.entity.Student;
import com.yourname.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // 添加学生（带参数校验）
    @PostMapping
    public Result<Void> addStudent(@Valid @RequestBody Student student) {
        studentService.addStudent(student);
        return Result.success();
    }

    // 更新学生（带参数校验）
    @PutMapping
    public Result<Void> updateStudent(@Valid @RequestBody Student student) {
        studentService.updateStudent(student);
        return Result.success();
    }

    // 查询学生（抛出资源不存在异常）
    @GetMapping("/{id}")
    public Result<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.getById(id);
        if (student == null) {
            throw new ResourceNotFoundException("学生不存在");
        }
        return Result.success(student);
    }
}
