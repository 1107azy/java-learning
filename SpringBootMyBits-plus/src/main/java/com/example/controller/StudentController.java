package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Student;
import com.example.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // 新增学生
    @PostMapping
    public ResponseEntity<Long> addStudent(@RequestBody Student student) {
        studentService.save(student);
        return ResponseEntity.ok(student.getId());
    }

    // 根据ID查询
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.getById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // 更新学生
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        boolean success = studentService.updateById(student);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    // 删除学生
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        boolean success = studentService.removeById(id);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    // ========== 【这里就是第七部分：分页查询接口，加在类的末尾】 ==========
    @GetMapping
    public ResponseEntity<Page<Student>> listStudents(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Student> pageObj = new Page<>(page, size);
        Page<Student> pageResult = studentService.page(pageObj, new LambdaQueryWrapper<>());
        return ResponseEntity.ok(pageResult);
    }
    // =================================================================
}
