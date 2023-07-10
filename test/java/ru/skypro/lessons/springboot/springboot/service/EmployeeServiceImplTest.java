package ru.skypro.lessons.springboot.springboot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeInfo;
import ru.skypro.lessons.springboot.springboot.pojo.Employee;
import ru.skypro.lessons.springboot.springboot.pojo.Position;
import ru.skypro.lessons.springboot.springboot.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @ParameterizedTest
    @MethodSource("addEmployees_ProvideParamsForTests")
    void addEmployees_ArraysOfEmployee_NoReturn(EmployeeDTO[] employeeDTO) {
        employeeService.addEmployees(employeeDTO);
        verify(employeeRepository, times(1)).saveAll(any());
    }

    public static Stream<Arguments> addEmployees_ProvideParamsForTests() {
        return Stream.of(

                Arguments.of(
                        (Object) new EmployeeDTO[]{
                                EmployeeDTO.fromEmployee(
                                        new Employee()
                                                .setId(123)
                                                .setName("Aleks")
                                                .setSalary(200)
                                                .setPosition(new Position().setId(1).setName("User1"))),
                                EmployeeDTO.fromEmployee(
                                        new Employee()
                                                .setId(123)
                                                .setName("Aleks2")
                                                .setSalary(200)
                                                .setPosition(new Position().setId(2).setName("User2")))
                        }),
                Arguments.of(
                        (Object) new EmployeeDTO[]{
                                EmployeeDTO.fromEmployee(
                                        new Employee()
                                                .setId(123)
                                                .setName("Aleks")
                                                .setSalary(200)
                                                .setPosition(new Position().setId(1).setName("User1")))
                        }),
                Arguments.of((Object) new EmployeeDTO[]{})
        );
    }

    @Test
    void addEmployee_EmployeeDTO_NoReturn() {
        EmployeeDTO employee = EmployeeDTO.fromEmployee(
                new Employee()
                        .setId(123)
                        .setName("Aleks")
                        .setSalary(200)
                        .setPosition(new Position().setId(2).setName("User1"))
        );
        employeeService.addEmployee(employee);
        verify(employeeRepository, times(1)).save(any());
    }

    @Test
    void putEmployee_IdAndEmployeeDTO_NoReturn() {

        when(employeeRepository.existsById(anyInt()))
                .thenReturn(true);

        Integer inputInteger = 1;
        EmployeeDTO inputEmployee = EmployeeDTO.fromEmployee(
                new Employee()
                        .setId(123)
                        .setName("Aleks")
                        .setSalary(200)
                        .setPosition(new Position().setId(2).setName("User1"))
        );
        employeeService.putEmployee(inputInteger, inputEmployee);
        verify(employeeRepository, times(1)).save(any());
    }

    @Test
    void getEmployee_EmployeeId_ShouldReturnEmployeeDTO() {
        Integer input = 2;
        EmployeeDTO expectedDTO =
                EmployeeDTO.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Aleks1")
                        .setSalary(200)
                        .setPosition(new Position().setId(2).setName("User1")));

        Optional<Employee> expectedEntity =
                Optional.ofNullable(new Employee()
                        .setId(123)
                        .setName("Aleks1")
                        .setSalary(200)
                        .setPosition(new Position().setId(2).setName("User1"))
                );
        when(employeeRepository.findById(input))
                .thenReturn(expectedEntity);


        EmployeeDTO actual = employeeService.getEmployee(input);
        assertEquals(expectedDTO, actual);
        verify(employeeRepository, times(2)).findById(any());
    }

    @Test
    void deleteEmployee_ById_NoReturn() {
        Integer input = 1;
        employeeService.deleteEmployee(input);
        verify(employeeRepository, times(1)).deleteById(any());
    }

    @Test
    void getEmployeesWithHighestSalary_ValidPosition_ShouldReturnEmployeeDTOList() {
        List<EmployeeDTO> expected = List.of(
                EmployeeDTO.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Aleks1")
                        .setSalary(202)
                        .setPosition(new Position().setId(2).setName("User1"))),
                EmployeeDTO.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Aleks2")
                        .setSalary(202)
                        .setPosition(new Position().setId(2).setName("User2")))
        );

        List<Employee> expectedRepositoryOut = List.of(
                new Employee()
                        .setId(123)
                        .setName("Aleks1")
                        .setSalary(202)
                        .setPosition(new Position().setId(2).setName("User1")),
                new Employee()
                        .setId(123)
                        .setName("Aleks2")
                        .setSalary(202)
                        .setPosition(new Position().setId(2).setName("User2"))
        );
        Integer input = 2;
        when(employeeRepository.getEmployeesWithHighestSalary())
                .thenReturn(expectedRepositoryOut);

        List<EmployeeDTO> actual = employeeService.getEmployeesWithHighestSalary();
        assertEquals(expected, actual);
        verify(employeeRepository, times(1)).getEmployeesWithHighestSalary();
    }

    @Test
    void getEmployeesOnPosition_ValidPosition_ShouldReturnEmployeeDTOList() {
        List<EmployeeDTO> expectedMethodOut = List.of(
                EmployeeDTO.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Aleks1")
                        .setSalary(200)
                        .setPosition(new Position().setId(2).setName("User1"))),
                EmployeeDTO.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Aleks2")
                        .setSalary(200)
                        .setPosition(new Position().setId(2).setName("User2")))
        );

        List<Employee> expectedRepositoryOut = List.of(
                new Employee()
                        .setId(123)
                        .setName("Aleks1")
                        .setSalary(200)
                        .setPosition(new Position().setId(2).setName("User1")),
                new Employee()
                        .setId(123)
                        .setName("Aleks2")
                        .setSalary(200)
                        .setPosition(new Position().setId(2).setName("User2")),
                new Employee()
                        .setId(1234)
                        .setName("Aleks3")
                        .setSalary(201)
                        .setPosition(new Position().setId(3).setName("User3"))
        );
        Integer input = 2;
        when(employeeRepository.findAll())
                .thenReturn(expectedRepositoryOut);

        List<EmployeeDTO> actual = employeeService.getEmployeesOnPosition(input);
        assertEquals(expectedMethodOut, actual);
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getEmployeeInfo_ValidPosition_ShouldReturnEmployeeInfo() {
        Integer input = 3;
        EmployeeInfo expected = new EmployeeInfo()
                .setName("Salary")
                .setSalary(380000)
                .setPositionName("DevUser1");

        when(employeeRepository.getEmployeeInfo(input))
                .thenReturn(expected);

        EmployeeInfo actual = employeeService.getEmployeeInfo(input);
        assertEquals(expected, actual);
        verify(employeeRepository, times(1)).getEmployeeInfo(any());
    }

    @Test
    void getEmployeePage_ValidPosition_ShouldReturnEmployeeInfo() {
        int page = 0;
        int size = 2;
        PageRequest input = PageRequest.of(page, size);
        List<EmployeeDTO> expected = List.of(
                EmployeeDTO.fromEmployee(new Employee()
                        .setId(12)
                        .setName("Aleks1")
                        .setSalary(400)
                        .setPosition(new Position().setId(1).setName("User1"))),
                EmployeeDTO.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Aleks2")
                        .setSalary(500)
                        .setPosition(new Position().setId(2).setName("User2")))
        );
        List<Employee> expectedRepositoryOut = List.of(
                new Employee()
                        .setId(12)
                        .setName("Aleks1")
                        .setSalary(400)
                        .setPosition(new Position().setId(1).setName("User1")),
                new Employee()
                        .setId(123)
                        .setName("Aleks2")
                        .setSalary(500)
                        .setPosition(new Position().setId(2).setName("User2"))
        );
        Page<Employee> pageContent = new PageImpl<>(expectedRepositoryOut);
        when(employeeRepository.findAll(input))
                .thenReturn(pageContent);

        List<EmployeeDTO> actual = employeeService.getEmployeePage(input);
        assertEquals(expected, actual);
        verify(employeeRepository, times(1)).findAll(input);
    }

    //=======================

    @Test
    void getEmployeeWithSalaryHigherThan_ValidPosition_ShouldReturnEmployeeDTOList() {
        List<EmployeeDTO> expected = List.of(
                EmployeeDTO.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Aleks1")
                        .setSalary(402)
                        .setPosition(new Position().setId(2).setName("User1"))),
                EmployeeDTO.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Aleks2")
                        .setSalary(402)
                        .setPosition(new Position().setId(2).setName("User2")))
        );
        List<Employee> expectedRepositoryOut = List.of(
                new Employee()
                        .setId(123)
                        .setName("Aleks1")
                        .setSalary(402)
                        .setPosition(new Position().setId(2).setName("User1")),
                new Employee()
                        .setId(123)
                        .setName("Aleks2")
                        .setSalary(402)
                        .setPosition(new Position().setId(2).setName("User2"))
        );
        Integer input = 5;
        when(employeeRepository.getEmployeeSalaryHigherThan(input))
                .thenReturn(expectedRepositoryOut);

        List<EmployeeDTO> actual = employeeService.getEmployeeWithSalaryHigherThan(input);
        assertEquals(expected, actual);
        verify(employeeRepository, times(1)).getEmployeeSalaryHigherThan(input);
    }

}