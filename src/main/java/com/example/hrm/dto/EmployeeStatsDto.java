package com.example.hrm.dto;

import java.math.BigDecimal;

public class EmployeeStatsDto {

    private BigDecimal averageSalary;
    private EmployeeResponseDto topEmployee;

    public EmployeeStatsDto() {
    }

    public EmployeeStatsDto(BigDecimal averageSalary, EmployeeResponseDto topEmployee) {
        this.averageSalary = averageSalary;
        this.topEmployee = topEmployee;
    }

    public BigDecimal getAverageSalary() {
        return averageSalary;
    }

    public EmployeeResponseDto getTopEmployee() {
        return topEmployee;
    }

    public void setAverageSalary(BigDecimal averageSalary) {
        this.averageSalary = averageSalary;
    }

    public void setTopEmployee(EmployeeResponseDto topEmployee) {
        this.topEmployee = topEmployee;
    }
}