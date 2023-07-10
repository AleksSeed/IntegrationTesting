package ru.skypro.lessons.springboot.springboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeInfo;
import ru.skypro.lessons.springboot.springboot.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public void addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.addEmployee(employeeDTO);
    }

    @PostMapping("/list")
    public void addEmployees(@RequestBody EmployeeDTO[] employeesDTO) {
        employeeService.addEmployees(employeesDTO);
    }

    @PutMapping("/{id}")
    public void putEmployee(@PathVariable Integer id, @RequestBody EmployeeDTO employeeDTO) {
        employeeService.putEmployee(id, employeeDTO);
    }

    @GetMapping("/{id}")
    public EmployeeDTO getEmployee(@PathVariable Integer id) {
        return employeeService.getEmployee(id);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
    }

    @GetMapping("/salaryHigherThan/{salary}")
    public List<EmployeeDTO> getEmployeeWithSalaryHigherThan(@PathVariable Integer salary) {
        return employeeService.getEmployeeWithSalaryHigherThan(salary);
    }

    @GetMapping("/withHighestSalary")
    public List<EmployeeDTO> getEmployeesWithHighestSalary() {
        return employeeService.getEmployeesWithHighestSalary();
    }

    @GetMapping
    public List<EmployeeDTO> getEmployeesOnPosition(@RequestParam(value = "position", required = false) Integer position) {
        return employeeService.getEmployeesOnPosition(position);
    }

    @GetMapping("/{id}/Info")
    public EmployeeInfo getEmployeeInfo(@PathVariable Integer id) {
        return employeeService.getEmployeeInfo(id);
    }

    @GetMapping("/page")
    public List<EmployeeDTO> getEmployeePage(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        return employeeService.getEmployeePage(PageRequest.of(page, size));
    }
}

