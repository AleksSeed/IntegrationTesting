package ru.skypro.lessons.springboot.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    private String position;
    private Long numberOfEmployees;
    private Integer maxSalary;
    private Integer minSalary;
    private Double avgSalary;
}
