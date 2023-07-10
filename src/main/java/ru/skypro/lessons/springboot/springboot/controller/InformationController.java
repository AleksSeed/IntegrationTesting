package ru.skypro.lessons.springboot.springboot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appInfo")
@RequiredArgsConstructor
public class InformationController {

    @Value("${app.env}")
    private String env;

    @GetMapping()
    public String getEmployee() {
        return env;
    }
}
