package project.payroll_backend_java.entity;

import java.io.Serializable;
import java.util.Objects;

public class SalaryArchiveId implements Serializable {

    private String e_id;
    private String salary_month;
    private Integer salary_year;

    // Default constructor
    public SalaryArchiveId() {}

    // Constructor with all fields
    public SalaryArchiveId(String e_id, String salary_month, Integer salary_year) {
        this.e_id = e_id;
        this.salary_month = salary_month;
        this.salary_year = salary_year;
    }

    // Getters and Setters
    public String getE_id() {
        return e_id;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }

    public String getSalary_month() {
        return salary_month;
    }

    public void setSalary_month(String salary_month) {
        this.salary_month = salary_month;
    }

    public Integer getSalary_year() {
        return salary_year;
    }

    public void setSalary_year(Integer salary_year) {
        this.salary_year = salary_year;
    }

    // equals method (required for composite key)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SalaryArchiveId that = (SalaryArchiveId) obj;
        return Objects.equals(e_id, that.e_id) &&
                Objects.equals(salary_month, that.salary_month) &&
                Objects.equals(salary_year, that.salary_year);
    }

    // hashCode method (required for composite key)
    @Override
    public int hashCode() {
        return Objects.hash(e_id, salary_month, salary_year);
    }

    // toString method (useful for debugging)
    @Override
    public String toString() {
        return "SalaryArchiveId{" +
                "e_id='" + e_id + '\'' +
                ", salary_month='" + salary_month + '\'' +
                ", salary_year=" + salary_year +
                '}';
    }
}