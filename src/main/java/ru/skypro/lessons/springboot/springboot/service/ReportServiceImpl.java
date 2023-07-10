package ru.skypro.lessons.springboot.springboot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.lessons.springboot.springboot.dto.EmployeeDTO;
import ru.skypro.lessons.springboot.springboot.pojo.Report;
import ru.skypro.lessons.springboot.springboot.exception.IncorrectEmployeeIdException;
import ru.skypro.lessons.springboot.springboot.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.springboot.repository.ReportRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);
    private final EmployeeRepository employeeRepository;
    private final ReportRepository reportRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void putReport(MultipartFile file) {
        logger.info("Вызов метода putReport() с аргументом: file");
        try {
            List<EmployeeDTO> employeeDTO = objectMapper.readValue(file.getBytes(), new TypeReference<>(){});
            employeeDTO.stream().map(EmployeeDTO::toEmployee).forEach(employeeRepository::save);
            logger.debug("putReport() выполнено");
        } catch (IOException e) {
            logger.error("Вызов метода putReport() с аргументом: file. Существует исключение.");
            e.printStackTrace();
        }
    }

    @Override
    public int putMainReport() {
        logger.info("Вызов метода putMainReport() w/o аргументы");
        String json = null;
        try {
            json = objectMapper.writeValueAsString(employeeRepository.putMainReport());
            logger.debug("putGeneralReport() выполнено");
        } catch (JsonProcessingException ex) {
            logger.error("Вызов метода putMainReport() w/o аргументы. Невозможно обработать json", ex);
            ex.printStackTrace();
        }
        Report report = new Report(json);
        reportRepository.save(report);
        return report.getId();
    }

    @SneakyThrows
    @Override
    public Resource getJson(int id) {
        logger.info("Вызов метода getJson() с аргументом: id={}", id);
        Optional<Report> file = reportRepository.findById(id);
        if (file.isPresent()) {
            String jsonText = reportRepository.findById(id).get().getText();
            logger.debug("getJson() выполнено");
            return new ByteArrayResource(jsonText.getBytes());
        }
        throw new IncorrectEmployeeIdException();
    }
}
