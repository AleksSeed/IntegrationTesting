package ru.skypro.lessons.springboot.springboot.service;

import org.springframework.data.domain.PageRequest;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeInfo;

import java.util.List;

public interface EmployeeService {

    void addEmployee(EmployeeDTO employeeDTO);

    void addEmployees(EmployeeDTO[] employeeDTO);

    void putEmployee(Integer id, EmployeeDTO employeeDTO);

    EmployeeDTO getEmployee(Integer id);

    void deleteEmployee(Integer id);

    List<EmployeeDTO> getEmployeesWithHighestSalary();

    List<EmployeeDTO> getEmployeesOnPosition(Integer position);

    EmployeeInfo getEmployeeInfo(Integer id);

    List<EmployeeDTO> getEmployeeWithSalaryHigherThan(int salary);

    List<EmployeeDTO> getEmployeePage(PageRequest pageRequest);
}
