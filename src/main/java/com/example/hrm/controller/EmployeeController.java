package com.example.hrm.controller;

import com.example.hrm.dto.EmployeeRequestDto;
import com.example.hrm.dto.EmployeeResponseDto;
import com.example.hrm.dto.EmployeeStatsDto;
import com.example.hrm.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeResponseDto> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public EmployeeResponseDto getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponseDto createEmployee(@Valid @RequestBody EmployeeRequestDto dto) {
        return employeeService.createEmployee(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    @GetMapping("/position/{position}")
    public List<EmployeeResponseDto> getEmployeesByPosition(@PathVariable String position) {
        return employeeService.getEmployeesByPosition(position);
    }

    @GetMapping("/stats")
    public EmployeeStatsDto getStats() {
        return employeeService.getStats();
    }
}