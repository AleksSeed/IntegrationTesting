package ru.skypro.lessons.springboot.springboot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.exception.ReportNotFoundException;
import ru.skypro.lessons.springboot.springboot.pojo.Employee;
import ru.skypro.lessons.springboot.springboot.pojo.Report;
import ru.skypro.lessons.springboot.springboot.pojo.Position;
import ru.skypro.lessons.springboot.springboot.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.springboot.repository.ReportRepository;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepositoryMock;
    @Mock
    private ReportRepository reportRepositoryMock;
    @Mock
    private ObjectMapper objectMapperMock;
    @InjectMocks
    private ReportServiceImpl reportService;

    @Test
    void putReport_FileWithEmployeeDTOs_NoReturn() throws IOException {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getBytes()).thenReturn(new byte[10]);
        List<EmployeeDTO> expected = List.of(
                EmployeeDTO.fromEmployee(new Employee()
                        .setId(1)
                        .setName("Aleks1")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("User1"))),
                EmployeeDTO.fromEmployee(new Employee()
                        .setId(2)
                        .setName("Aleks2")
                        .setSalary(400)
                        .setPosition(new Position().setId(2).setName("User2")))
        );
        when(objectMapperMock.readValue(any(byte[].class), any(TypeReference.class)))
                .thenReturn(expected);
        reportService.putReport(file);
        verify(employeeRepositoryMock, times(2)).save(any(Employee.class));
    }

    @Test
    void putMainReport_NoReturn() throws JsonProcessingException {
        when(objectMapperMock.writeValueAsString(employeeRepositoryMock.putMainReport()))
                .thenReturn("test");
        reportService.putMainReport();
        verify(reportRepositoryMock, times(1)).save(any(Report.class));
    }

    @Test
    void getJson_ThrowReportNotFoundException() {
        Integer input = 10;
        when(reportRepositoryMock.findById(any()))
                .thenThrow(ReportNotFoundException.class);
        assertThrows(ReportNotFoundException.class, () -> reportRepositoryMock.findById(input));
    }
}
