package com.yourname.student.service;

import com.yourname.student.entity.Student;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class StudentService {
    // 模拟数据库
    private final Map<Long, Student> studentMap = new HashMap<>();
    private Long idCounter = 1L;

    public void addStudent(Student student) {
        student.setId(idCounter++);
        studentMap.put(student.getId(), student);
    }

    public void updateStudent(Student student) {
        studentMap.put(student.getId(), student);
    }

    public Student getById(Long id) {
        return studentMap.get(id);
    }
}
