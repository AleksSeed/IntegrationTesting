package ru.skypro.lessons.springboot.springboot.repository;

import org.springframework.data.repository.CrudRepository;
import ru.skypro.lessons.springboot.springboot.pojo.Report;

public interface ReportRepository extends CrudRepository<Report, Integer> {
}