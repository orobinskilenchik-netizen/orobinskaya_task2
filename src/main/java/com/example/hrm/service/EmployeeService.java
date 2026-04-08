package com.example.hrm.service;

import com.example.hrm.dto.EmployeeRequestDto;
import com.example.hrm.dto.EmployeeResponseDto;
import com.example.hrm.dto.EmployeeStatsDto;
import com.example.hrm.exception.EmployeeNotFoundException;
import com.example.hrm.model.Employee;
import com.example.hrm.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeResponseDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(this::toDto).toList();
    }

    public EmployeeResponseDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
        return toDto(employee);
    }

    public EmployeeResponseDto createEmployee(EmployeeRequestDto dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setPosition(dto.getPosition());
        employee.setSalary(dto.getSalary());
        employee.setHireDate(dto.getHireDate());

        Employee savedEmployee = employeeRepository.save(employee);
        return toDto(savedEmployee);
    }

    public void deleteEmployee(Long id) {
        boolean exists = employeeRepository.existsById(id);
        if (!exists) {
            throw new EmployeeNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    public List<EmployeeResponseDto> getEmployeesByPosition(String position) {
        List<Employee> employees = employeeRepository.findByPosition(position);
        return employees.stream().map(this::toDto).toList();
    }

    public EmployeeStatsDto getStats() {
        List<Employee> employees = employeeRepository.findAll();

        if (employees.isEmpty()) {
            return new EmployeeStatsDto(BigDecimal.ZERO, null);
        }

        BigDecimal sum = BigDecimal.ZERO;
        Employee topEmployee = employees.get(0);

        for (Employee employee : employees) {
            sum = sum.add(employee.getSalary());

            if (employee.getSalary().compareTo(topEmployee.getSalary()) > 0) {
                topEmployee = employee;
            }
        }

        BigDecimal averageSalary = sum.divide(
                BigDecimal.valueOf(employees.size()),
                2,
                RoundingMode.HALF_UP
        );

        return new EmployeeStatsDto(averageSalary, toDto(topEmployee));
    }

    private EmployeeResponseDto toDto(Employee employee) {
        return new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getPosition(),
                employee.getSalary(),
                employee.getHireDate()
        );
    }
}