package com.example.hrm.controller;

import com.example.hrm.dto.EmployeeRequestDto;
import com.example.hrm.dto.EmployeeResponseDto;
import com.example.hrm.dto.EmployeeStatsDto;
import com.example.hrm.exception.EmployeeNotFoundException;
import com.example.hrm.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateEmployee() throws Exception {
        EmployeeRequestDto requestDto = new EmployeeRequestDto(
                "Иван Иванов",
                "Java Developer",
                new BigDecimal("150000.00"),
                LocalDate.of(2024, 1, 15)
        );

        EmployeeResponseDto responseDto = new EmployeeResponseDto(
                1L,
                "Иван Иванов",
                "Java Developer",
                new BigDecimal("150000.00"),
                LocalDate.of(2024, 1, 15)
        );

        given(employeeService.createEmployee(any(EmployeeRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Иван Иванов"))
                .andExpect(jsonPath("$.position").value("Java Developer"));
    }

    @Test
    void shouldReturnAllEmployees() throws Exception {
        List<EmployeeResponseDto> employees = List.of(
                new EmployeeResponseDto(
                        1L,
                        "Иван Иванов",
                        "Java Developer",
                        new BigDecimal("150000.00"),
                        LocalDate.of(2024, 1, 15)
                )
        );

        given(employeeService.getAllEmployees()).willReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Иван Иванов"));
    }

    @Test
    void shouldReturn404WhenEmployeeNotFound() throws Exception {
        given(employeeService.getEmployeeById(100L))
                .willThrow(new EmployeeNotFoundException("Employee not found with id: 100"));

        mockMvc.perform(get("/api/employees/100"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Employee not found with id: 100"));
    }

    @Test
    void shouldReturnEmptyStats() throws Exception {
        EmployeeStatsDto statsDto = new EmployeeStatsDto(BigDecimal.ZERO, null);

        given(employeeService.getStats()).willReturn(statsDto);

        mockMvc.perform(get("/api/employees/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageSalary").value(0));
    }
}