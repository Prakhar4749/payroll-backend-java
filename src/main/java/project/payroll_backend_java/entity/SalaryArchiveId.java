package project.payroll_backend_java.entity;

import java.io.Serializable;
import java.util.Objects;

public class SalaryArchiveId implements Serializable {

    private String eId;
    private String salaryMonth;
    private Integer salaryYear;

    // Default constructor
    public SalaryArchiveId() {}

    // Constructor with all fields
    public SalaryArchiveId(String eId, String salaryMonth, Integer salaryYear) {
        this.eId = eId;
        this.salaryMonth = salaryMonth;
        this.salaryYear = salaryYear;
    }

    // Getters and Setters
    public String getEId() {
        return eId;
    }

    public void setEId(String eId) {
        this.eId = eId;
    }

    public String getSalaryMonth() {
        return salaryMonth;
    }

    public void setSalaryMonth(String salaryMonth) {
        this.salaryMonth = salaryMonth;
    }

    public Integer getSalaryYear() {
        return salaryYear;
    }

    public void setSalaryYear(Integer salaryYear) {
        this.salaryYear = salaryYear;
    }

    // equals method (required for composite key)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SalaryArchiveId that = (SalaryArchiveId) obj;
        return Objects.equals(eId, that.eId) &&
                Objects.equals(salaryMonth, that.salaryMonth) &&
                Objects.equals(salaryYear, that.salaryYear);
    }

    // hashCode method (required for composite key)
    @Override
    public int hashCode() {
        return Objects.hash(eId, salaryMonth, salaryYear);
    }

    // toString method (useful for debugging)
    @Override
    public String toString() {
        return "SalaryArchiveId{" +
                "eId='" + eId + '\'' +
                ", salaryMonth='" + salaryMonth + '\'' +
                ", salaryYear=" + salaryYear +
                '}';
    }
}
