package com.mika.assesment.model;

/**
 * Created by ТСД on 26.10.2014.
 */
public class ReportEntry {
    private Long employeeId;
    private Double workingHours;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Double workingHours) {
        this.workingHours = workingHours;
    }
}
