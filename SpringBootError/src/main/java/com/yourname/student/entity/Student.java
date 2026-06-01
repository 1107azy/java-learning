package com.yourname.student.entity;

import lombok.Data;
import javax.validation.constraints.*;

@Data
public class Student {
    private Long id;

    @NotBlank(message = "姓名不能为空")
    @Size(min = 1, max = 20, message = "姓名长度需在1-20个字符之间")
    private String name;

    @Min(value = 0, message = "年龄不能小于0")
    @Max(value = 150, message = "年龄不能大于150")
    private Integer age;

    @Min(value = 0, message = "分数不能小于0")
    @Max(value = 100, message = "分数不能大于100")
    private Integer score;
}
