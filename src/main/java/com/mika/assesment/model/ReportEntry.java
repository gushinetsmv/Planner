package com.mika.assesment.model;

/**
 * Report entity model.
 *
 * @author Mikhail Gushinets
 * @since 07/11/14
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
