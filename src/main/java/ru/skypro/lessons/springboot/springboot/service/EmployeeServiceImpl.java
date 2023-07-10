package ru.skypro.lessons.springboot.springboot.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeInfo;
import ru.skypro.lessons.springboot.springboot.pojo.Employee;
import ru.skypro.lessons.springboot.springboot.pojo.Position;
import ru.skypro.lessons.springboot.springboot.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Override
    public void addEmployees(EmployeeDTO[] employeeDTO) {
        logger.info("Вызов метода addEmployees() с аргументом: employeeDTO={}", Arrays.stream(employeeDTO).toList());
        employeeRepository.saveAll(Arrays.stream(employeeDTO).map(EmployeeDTO::toEmployee).toList());
        logger.debug("addEmployees() выполнено");
    }

    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        logger.info("Вызов метода addEmployee() с аргументом: employeeDTO={}", employeeDTO);
        employeeRepository.save(employeeDTO.toEmployee());
        logger.debug("addEmployee() выполнено");
    }

    @Override
    public void putEmployee(Integer id, EmployeeDTO employeeDTO) {
        logger.info("Вызов метода putEmployee() с аргументом: id={}, employeeDTO={}", id, employeeDTO);
        if (employeeRepository.existsById(id)) {
            employeeDTO.setId(id);
            employeeRepository.save(employeeDTO.toEmployee());
            logger.debug("putEmployee() выполнено");
        }
    }

    @Override
    public EmployeeDTO getEmployee(Integer id) {
        logger.info("Вызов метода getEmployee() с аргументом: id={}", id);
        if (employeeRepository.findById(id).isPresent()) {
            EmployeeDTO employeeDTO = EmployeeDTO.fromEmployee(employeeRepository.findById(id).get());
            logger.debug("putEmployee() выполнено");
            return employeeDTO;
        }
        logger.error("Вызов метода getEmployee() с аргументом: id={}. There is no employee with the id.", id);
        throw new IndexOutOfBoundsException();
   }

    @Override
    public void deleteEmployee(Integer id) {
        logger.info("Вызов метода deleteEmployee() с аргументом: id={}", id);
        employeeRepository.deleteById(id);
        logger.debug("deleteEmployee() выполнено");
    }

    @Override
    public List<EmployeeDTO> getEmployeeWithSalaryHigherThan(int salary) {
        logger.info("Вызов метода getEmployeeWithSalaryHigherThan() с аргументом: salary={}", salary);
        List<EmployeeDTO> listEmployeeDTO = employeeRepository.getEmployeeSalaryHigherThan(salary).stream().map(EmployeeDTO::fromEmployee).toList();
        logger.debug("getEmployeeWithSalaryHigherThan() выполнено");
        return listEmployeeDTO;
    }

    @Override
    public List<EmployeeDTO> getEmployeesWithHighestSalary() {
        logger.info("Вызов метода getEmployeesWithHighestSalary() w/o аргументы");
        List<EmployeeDTO> listEmployeeDTO = employeeRepository.getEmployeesWithHighestSalary().stream().map(EmployeeDTO::fromEmployee).toList();
        logger.debug("getEmployeesWithHighestSalary() выполнено");
        return listEmployeeDTO;
    }

    @Override
    public List<EmployeeDTO> getEmployeesOnPosition(Integer position) {
        logger.info("Вызов метода getEmployeesOnPosition() с аргументом: position={}", position);
        List<EmployeeDTO> EmployeeDTOList = new ArrayList<>();
        if (Optional.ofNullable(position).isPresent()) {
            for (Employee employee : employeeRepository.findAll()) {
                Position EmployeePosition = (Position) employee.getPosition();
                if (Optional.ofNullable(EmployeePosition).isPresent()) {
                    if (EmployeePosition.getId().equals(position)) {
                        EmployeeDTOList.add(EmployeeDTO.fromEmployee(employee));
                        logger.debug("getEmployeesOnPosition() выполнено");
                    }
                }
            }
        } else {
            employeeRepository.findAll().forEach(employee -> EmployeeDTOList.add(EmployeeDTO.fromEmployee(employee)));
        }
        return EmployeeDTOList;
    }

    @Override
    public EmployeeInfo getEmployeeInfo(Integer id) {
        logger.info("Вызов метода getEmployeeFullInfo() с аргументом: id={}", id);
        EmployeeInfo employeeInfo = employeeRepository.getEmployeeInfo(id);
        logger.debug("getEmployeeFullInfo() выполнено");
        return employeeInfo;
    }

    @Override
    public List<EmployeeDTO> getEmployeePage(PageRequest pageRequest) {
        logger.info("Вызов метода getEmployeePage() с аргументом: pageRequest={}", pageRequest);
        List<EmployeeDTO> employeesList = employeeRepository.findAll(pageRequest).stream().map(EmployeeDTO::fromEmployee).toList();
        logger.debug("getEmployeePage() выполнено");
        return employeesList;
    }
}
