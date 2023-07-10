package ru.skypro.lessons.springboot.springboot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class EmployeeInfo {
    private String name;
    private Integer salary;
    private String positionName;

    public EmployeeInfo(String name, Integer salary, String positionName) {
        this.name = name;
        this.salary = salary;
        this.positionName = positionName;
    }
}
