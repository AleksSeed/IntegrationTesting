package ru.skypro.lessons.springboot.springboot.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.lessons.springboot.springboot.pojo.Position;
import ru.skypro.lessons.springboot.springboot.repository.EmployeeRepository;
import ru.skypro.lessons.springboot.springboot.repository.PositionRepository;
import ru.skypro.lessons.springboot.springboot.service.EmployeeService;

import java.util.ArrayList;
import java.util.List;

import static Support.EmployeeSupportData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private ObjectMapper objectMapper;


//       Заполнение репозитория данными?
 /*   @BeforeAll
    public void initPositionTable(){
        List<Position> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(new Position(i,Integer.toString(i)));
        }
        positionRepository.saveAll(list);
    }*/

    @BeforeEach
    void cleanEmployeeTable() {
        employeeRepository.deleteAll();
    }

    private static final String EMPLOYEES_URL = "/employees";


    @Test
    void addEmployee_SingleRecord() throws Exception {
        int employeeId = 1;
        mockMvc.perform(post(EMPLOYEES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(returnJsonEmployeeDTO(employeeId).toString()))
                .andExpect(status().isOk());
        String expectedName = Integer.toString(employeeId);
        assertEquals(expectedName, returnEmployeeListFromDB(employeeRepository).stream().findFirst().orElseThrow().getName());
    }

    @Test
    void addEmployee_List() throws Exception {
        int arrayElement1 = 1;
        int arrayElement2 = 2;
        String jsonString = returnJsonEmployeeDTOList(arrayElement1, arrayElement2);
        mockMvc.perform(post(EMPLOYEES_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk());
        assertEquals(Integer.toString(arrayElement1), employeeRepository.findById(arrayElement1).orElseThrow().getName());
        assertEquals(Integer.toString(arrayElement2), employeeRepository.findById(arrayElement2).orElseThrow().getName());
    }

    @Test
    void putEmployee_SingleRecord() throws Exception {
        Integer employeeId = 1;
        String newEmployeeName = "3";
        mockMvc.perform(post(EMPLOYEES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(returnJsonEmployeeDTO(employeeId).toString()))
                .andExpect(status().isOk());
        employeeId = returnEmployeeListFromDB(employeeRepository).stream().findFirst().orElseThrow().getId();
        JSONObject jsonObject = returnJsonEmployeeDTO(employeeId);
        jsonObject.put("name", newEmployeeName);
        mockMvc.perform(put(EMPLOYEES_URL + "/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk());
        assertEquals(newEmployeeName, returnEmployeeListFromDB(employeeRepository).stream().findFirst().orElseThrow().getName());
    }

    @Test
    void getEmployee() throws Exception {
        int employeeId = 1;
        mockMvc.perform(post(EMPLOYEES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(returnJsonEmployeeDTO(employeeId).toString()))
                .andExpect(status().isOk());
        employeeId = returnEmployeeListFromDB(employeeRepository).stream().findFirst().orElseThrow().getId();
        mockMvc.perform(get(EMPLOYEES_URL + "/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employeeId));
    }

    @Test
    void deleteEmployee_ExistingRecord() throws Exception {
        int employeeId = 1;
        mockMvc.perform(post(EMPLOYEES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(returnJsonEmployeeDTO(employeeId).toString()))
                .andExpect(status().isOk());
        employeeId = returnEmployeeListFromDB(employeeRepository).stream().findFirst().orElseThrow().getId();
        mockMvc.perform(delete(EMPLOYEES_URL + "/{id}", employeeId))
                .andExpect(status().isOk());
    }

    @Test
    void delEmployee_Exception() throws Exception {
        int employeeId = 1;
        mockMvc.perform(delete(EMPLOYEES_URL + "/{id}", employeeId))
                .andExpect(status().isOk());
    }

    @Test
    void getEmployeeWithSalaryHigherThan_IfOneTwoOrNoneRecordsExist() throws Exception {
        int employeeId = 50;
        String jsonString = returnJsonEmployeeDTOList(employeeId, employeeId - 1);
        mockMvc.perform(post(EMPLOYEES_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk());

        int salary = employeeId - 1;
        mockMvc.perform(get(EMPLOYEES_URL + "/salaryHigherThan/{salary}", salary))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        salary = employeeId - 2;
        mockMvc.perform(get(EMPLOYEES_URL + "/salaryHigherThan/{salary}", salary))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
        salary = employeeId + 1;
        mockMvc.perform(get(EMPLOYEES_URL + "/salaryHigherThan/{salary}", salary))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getEmployeesOnPosition_IfOneTwoOrNoneRecordsExist() throws Exception {
        int employeeId = 1;


        String jsonString = returnJsonEmployeeDTOList(employeeId, employeeId, employeeId + 1);
        mockMvc.perform(post(EMPLOYEES_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk());
        String position = Integer.toString(employeeId);
        mockMvc.perform(get(EMPLOYEES_URL + "?position={position}", position))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
        position = Integer.toString(employeeId + 1);
        mockMvc.perform(get(EMPLOYEES_URL + "?position={position}", position))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getEmployeeInfo() throws Exception {
        int employeeId = 49;
        mockMvc.perform(post(EMPLOYEES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(returnJsonEmployeeDTO(employeeId).toString()))
                .andExpect(status().isOk());
        returnEmployeeListFromDB(employeeRepository).forEach(System.out::println);
        String name = returnEmployeeListFromDB(employeeRepository).stream().findFirst().orElseThrow().getName();
        employeeId = returnEmployeeListFromDB(employeeRepository).stream().findFirst().orElseThrow().getId();
        mockMvc.perform(get(EMPLOYEES_URL + "/{employeeId}/Info", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name));
    }


    @Test
    void getEmployeePage_3ElementsAreOnThePage0And2OnThePage1() throws Exception {
        Integer page0 = 0;
        Integer page1 = 1;
        Integer size = 3;
        String jsonString = returnJsonEmployeeDTOList(21, 22, 23, 24, 25);
        mockMvc.perform(post(EMPLOYEES_URL + "/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk());
        System.out.println(jsonString);
        mockMvc.perform(get(EMPLOYEES_URL + "/page?page={page}&size={size}", page0, size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
        System.out.println(jsonString);
        mockMvc.perform(get(EMPLOYEES_URL + "/page?page={page}&size={size}", page1, size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}