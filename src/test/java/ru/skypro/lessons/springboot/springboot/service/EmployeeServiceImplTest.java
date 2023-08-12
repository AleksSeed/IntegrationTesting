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
import ru.skypro.lessons.springboot.springboot.dto.EmployeeMapper;
import ru.skypro.lessons.springboot.springboot.pojo.Employee;
import ru.skypro.lessons.springboot.springboot.pojo.Position;
import ru.skypro.lessons.springboot.springboot.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.springboot.repository.PositionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepositoryMock;
    @Mock
    private PositionRepository positionRepositoryMock;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @ParameterizedTest
    @MethodSource("addEmployees_ProvideParamsForTests")
    void addEmployees_ArraysOfEmployee_NoReturn(EmployeeDTO[] employeeDTO) {
        employeeService.addEmployees(employeeDTO);
        verify(employeeRepositoryMock, times(1)).saveAll(any());
    }

    public static Stream<Arguments> addEmployees_ProvideParamsForTests() {
        return Stream.of(

                Arguments.of(
                        (Object) new EmployeeDTO[]{
                                EmployeeMapper.fromEmployee(
                                        new Employee()
                                                .setId(123)
                                                .setName("Aleks1")
                                                .setSalary(400)
                                                .setPosition(new Position().setId(1).setName("User1"))),
                                EmployeeMapper.fromEmployee(
                                        new Employee()
                                                .setId(123)
                                                .setName("Aleks2")
                                                .setSalary(400)
                                                .setPosition(new Position().setId(2).setName("User2")))
                        }),
                Arguments.of(
                        (Object) new EmployeeDTO[]{
                                EmployeeMapper.fromEmployee(
                                        new Employee()
                                                .setId(123)
                                                .setName("Aleks1")
                                                .setSalary(400)
                                                .setPosition(new Position().setId(1).setName("User1")))
                        }),
                Arguments.of((Object) new EmployeeDTO[]{})
        );
    }

    @Test
    void addEmployee_EmployeeDTO_NoReturn() {
        EmployeeDTO employee = EmployeeMapper.fromEmployee(
                new Employee()
                        .setId(123)
                        .setName("Aleks1")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("User1"))
        );
        employeeService.addEmployee(employee);
        verify(employeeRepositoryMock, times(1)).save(any());
    }

    @Test
    void putEmployee_IdAndEmployeeDTO_NoReturn() {

        when(employeeRepositoryMock.existsById(anyInt()))
                .thenReturn(true);
        when(positionRepositoryMock.getPositionByName(any(String.class)))
                .thenReturn(new Position().setId(2).setName("User1"));

        Integer inputInteger = 1;
        EmployeeDTO inputEmployee = EmployeeMapper.fromEmployee(
                new Employee()
                        .setId(123)
                        .setName("Aleks1")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("User1"))
        );
        employeeService.putEmployee(inputInteger, inputEmployee);
        verify(employeeRepositoryMock, times(1)).save(any());
    }

    @Test
    void getEmployee_EmployeeId_ShouldReturnEmployeeDTO() {
        Integer input = 2;
        EmployeeDTO expectedDTO =
                EmployeeMapper.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Aleks1")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("User1")));

        Optional<Employee> expectedEntity =
                Optional.ofNullable(new Employee()
                        .setId(123)
                        .setName("Aleks1")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("User1"))
                );
        when(employeeRepositoryMock.findById(input))
                .thenReturn(expectedEntity);


        EmployeeDTO actual = employeeService.getEmployee(input);
        assertEquals(expectedDTO, actual);
        verify(employeeRepositoryMock, times(2)).findById(any());
    }

    @Test
    void delEmployee_ById_NoReturn() {
        Integer input = 1;
        employeeService.deleteEmployee(input);
        verify(employeeRepositoryMock, times(1)).deleteById(any());
    }

    @Test
    void getEmployeesWithHighestSalary_ValidPosition_ShouldReturnEmployeeDTOList() {
        List<EmployeeDTO> expected = List.of(
                EmployeeMapper.fromEmployee(new Employee()
                        .setId(123)
                        .setName("Aleks1")
                        .setSalary(402)
                        .setPosition(new Position().setId(2).setName("User1"))),
                EmployeeMapper.fromEmployee(new Employee()
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
        Integer input = 2;
        when(employeeRepositoryMock.getEmployeesWithHighestSalary())
                .thenReturn(expectedRepositoryOut);

        List<EmployeeDTO> actual = employeeService.getEmployeesWithHighestSalary();
        assertEquals(expected, actual);
        verify(employeeRepositoryMock, times(1)).getEmployeesWithHighestSalary();
    }

    @Test
    void getEmployeesOnPosition_ValidPosition_ShouldReturnEmployeeDTOList() {
        List<EmployeeDTO> expectedMethodOut = List.of(
                EmployeeMapper.fromEmployee(new Employee()
                        .setId(100)
                        .setName("Aleks1")
                        .setSalary(200)
                        .setPosition(new Position().setId(2).setName("User2"))),
                EmployeeMapper.fromEmployee(new Employee()
                        .setId(100)
                        .setName("Aleks2")
                        .setSalary(200)
                        .setPosition(new Position().setId(2).setName("User2")))
        );

        List<Employee> expectedRepositoryOut = List.of(
                new Employee()
                        .setId(100)
                        .setName("Aleks1")
                        .setSalary(200)
                        .setPosition(new Position().setId(2).setName("User2")),
                new Employee()
                        .setId(101)
                        .setName("Aleks2")
                        .setSalary(200)
                        .setPosition(new Position().setId(2).setName("User2")),
                new Employee()
                        .setId(102)
                        .setName("Aleks3")
                        .setSalary(201)
                        .setPosition(new Position().setId(3).setName("User3"))
        );

        String input = "User2";
        when(positionRepositoryMock.getPositionByName(input))
                .thenReturn(new Position().setId(2).setName("User2"));
        when(employeeRepositoryMock.getEmployeesOnPosition(anyInt()))
                .thenReturn(expectedMethodOut.stream().map(EmployeeMapper::toEmployee).toList());


        List<EmployeeDTO> actual = employeeService.getEmployeesOnPosition(input);
        assertEquals(expectedMethodOut, actual);
        verify(employeeRepositoryMock, times(1)).getEmployeesOnPosition(anyInt());
    }

    @Test
    void getEmployeeInfo_ValidPosition_ShouldReturnEmployeeFullInfo() {
        Integer input = 3;
        EmployeeInfo expected = new EmployeeInfo()
                .setName("Salary")
                .setSalary(410000)
                .setPositionName("Developer1");

        when(employeeRepositoryMock.getEmployeeInfo(input))
                .thenReturn(expected);

        EmployeeInfo actual = employeeService.getEmployeeInfo(input);
        assertEquals(expected, actual);
        verify(employeeRepositoryMock, times(1)).getEmployeeInfo(any());
    }

    @Test
    void getEmployeePage_ValidPosition_ShouldReturnEmployeeFullInfo() {
        int page = 0;
        int size = 2;
        PageRequest input = PageRequest.of(page, size);
        List<EmployeeDTO> expected = List.of(
                EmployeeMapper.fromEmployee(new Employee()
                        .setId(12)
                        .setName("Aleks1")
                        .setSalary(400)
                        .setPosition(new Position().setId(1).setName("User1"))),
                EmployeeMapper.fromEmployee(new Employee()
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
        when(employeeRepositoryMock.findAll(input))
                .thenReturn(pageContent);

        List<EmployeeDTO> actual = employeeService.getEmployeePage(input);
        assertEquals(expected, actual);
        verify(employeeRepositoryMock, times(1)).findAll(input);
    }
}