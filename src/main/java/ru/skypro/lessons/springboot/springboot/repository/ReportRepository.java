package ru.skypro.lessons.springboot.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.lessons.springboot.springboot.pojo.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {
}