package ru.skypro.lessons.springboot.springboot.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ReportService {
    void putReport(MultipartFile file);

    int putMainReport();

    Resource getJson(int id);
}
