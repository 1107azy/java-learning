package com.example.mapper;

import com.example.entity.Student;
import org.apache.ibatis.annotations.*;

@Mapper
public interface StudentMapper {

    @Insert("INSERT INTO students(name, age, score) VALUES(#{name}, #{age}, #{score})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Student student);

    @Select("SELECT * FROM students WHERE id = #{id}")
    Student selectById(Long id);

    @Update("UPDATE students SET name=#{name}, age=#{age}, score=#{score} WHERE id=#{id}")
    int updateById(Student student);

    @Delete("DELETE FROM students WHERE id=#{id}")
    int deleteById(Long id);
}
