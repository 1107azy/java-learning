package com.example.service;

import com.example.entity.Student;

public interface StudentService {
    Long addStudent(Student student);
    Student getStudentById(Long id);
    boolean updateStudent(Long id, Student student);
    boolean deleteStudent(Long id);
}
