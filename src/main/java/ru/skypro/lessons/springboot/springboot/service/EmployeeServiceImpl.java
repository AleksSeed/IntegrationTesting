package ru.skypro.lessons.springboot.springboot.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeInfo;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeMapper;
import ru.skypro.lessons.springboot.springboot.pojo.Position;
import ru.skypro.lessons.springboot.springboot.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.springboot.repository.PositionRepository;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Override
    public void addEmployees(EmployeeDTO[] employeeDTO) {
        logger.info("Вызов метода addEmployees() с аргументом: employeeDTO={}", Arrays.stream(employeeDTO).toList());
        employeeRepository.saveAll(Arrays.stream(employeeDTO)
                .map(e -> {
                    Position position = positionRepository.getPositionByName(e.getPosition());
                    return EmployeeMapper.toEmployee(e, position);
                })
                .toList());
        logger.debug("addEmployees() выполнено");
    }

    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        logger.info("Вызов метода addEmployee() с аргументом: employeeDTO={}", employeeDTO);
        Position position = positionRepository.getPositionByName(employeeDTO.getPosition());
        employeeRepository.save(EmployeeMapper.toEmployee(employeeDTO, position));
        logger.debug("addEmployee() выполнено");
    }

    @Override
    public void putEmployee(Integer id, EmployeeDTO employeeDTO) {
        logger.info("Вызов метода putEmployee() с аргументом: id={}, employeeDTO={}", id, employeeDTO);
        if (employeeRepository.existsById(id)) {
            employeeDTO.setId(id);
            Position position = positionRepository.getPositionByName(employeeDTO.getPosition());
            employeeRepository.save(EmployeeMapper.toEmployee(employeeDTO, position));
            logger.debug("putEmployee() выполнено");
        }
    }

    @Override
    public EmployeeDTO getEmployee(Integer id) {
        logger.info("Вызов метода getEmployee() с аргументом: id={}", id);
        if (employeeRepository.findById(id).isPresent()) {
            EmployeeDTO employeeDTO = EmployeeMapper.fromEmployee(employeeRepository.findById(id).get());
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
        List<EmployeeDTO> listEmployeeDTO = employeeRepository.getEmployeeSalaryHigherThan(salary).stream().map(EmployeeMapper::fromEmployee).toList();
        logger.debug("getEmployeeWithSalaryHigherThan() выполнено");
        return listEmployeeDTO;
    }

    @Override
    public List<EmployeeDTO> getEmployeesWithHighestSalary() {
        logger.info("Вызов метода getEmployeesWithHighestSalary() w/o аргументы");
        List<EmployeeDTO> listEmployeeDTO = employeeRepository.getEmployeesWithHighestSalary().stream().map(EmployeeMapper::fromEmployee).toList();
        logger.debug("getEmployeesWithHighestSalary() выполнено");
        return listEmployeeDTO;
    }

    @Override
    public List<EmployeeDTO> getEmployeesOnPosition(String positionName) {
        logger.info("Вызов метода getEmployeesOnPosition() с аргументом: position={}", positionName);
        List<EmployeeDTO> EmployeeDTOList;
        Position position = positionRepository.getPositionByName(positionName);
        if (positionName == null || positionName.isBlank() || position == null) {
            EmployeeDTOList = employeeRepository.findAll().stream()
                    .map(EmployeeMapper::fromEmployee)
                    .toList();
        } else {
            EmployeeDTOList = employeeRepository.getEmployeesOnPosition(position.getId()).stream()
                    .map(EmployeeMapper::fromEmployee)
                    .toList();
        }
        logger.debug("getEmployeesOnPosition() выполнено");
        return EmployeeDTOList;
    }

    @Override
    public EmployeeInfo getEmployeeInfo(Integer id) {
        logger.info("Вызов метода getEmployeeInfo() с аргументом: id={}", id);
        EmployeeInfo employeeInfo = employeeRepository.getEmployeeInfo(id);
        logger.debug("getEmployeeInfo() выполнено");
        return employeeInfo;
    }

    @Override
    public List<EmployeeDTO> getEmployeePage(PageRequest pageRequest) {
        logger.info("Вызов метода getEmployeePage() с аргументом: pageRequest={}", pageRequest);
        List<EmployeeDTO> employeesList = employeeRepository.findAll(pageRequest).stream().map(EmployeeMapper::fromEmployee).toList();
        logger.debug("getEmployeePage() выполнено");
        return employeesList;
    }
}
