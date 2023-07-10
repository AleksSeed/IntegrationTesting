package ru.skypro.lessons.springboot.springboot.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skypro.lessons.springboot.springboot.pojo.Employee;

import java.util.Optional;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class EmployeeDTO {
    private Integer id;
    private String name;
    private Integer salary;
    private Integer position;

    public static EmployeeDTO fromEmployee(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSalary(employee.getSalary());
        if (Optional.ofNullable(employee.getPosition()).isPresent()) {
            employeeDTO.setPosition(employee.getPosition().getId());
        }
        return employeeDTO;
    }

    public Employee toEmployee() {
        Employee employee = new Employee();
        employee.setId(this.getId());
        employee.setName(this.getName());
        employee.setSalary(this.getSalary());
        return employee;
    }
}