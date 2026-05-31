package com.example.controller;

import com.example.entity.Student;
import com.example.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    // 构造器注入
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // 新增学生
    @PostMapping
    public ResponseEntity<Long> addStudent(@RequestBody Student student) {
        Long id = studentService.addStudent(student);
        return ResponseEntity.ok(id);
    }

    // 根据ID查询学生
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // 更新学生
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        boolean success = studentService.updateStudent(id, student);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    // 删除学生
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        boolean success = studentService.deleteStudent(id);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
