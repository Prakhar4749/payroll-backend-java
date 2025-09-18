package project.payroll_backend_java.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "dept_details")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DeptDetails {

    @Id
    @Column(name = "d_id", length = 5, nullable = false)
    private String dId;

    @Column(name = "d_name", length = 30, nullable = false)
    private String dName;

    // Add @JsonIgnore to prevent infinite recursion during JSON serialization
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // THIS IS THE KEY FIX
    private List<EmpDetails> employees;

    // Constructors
    public DeptDetails() {}

    public DeptDetails(String dId, String dName) {
        this.dId = dId;
        this.dName = dName;
    }

    // Getters and Setters
    public String getDId() { return dId; }
    public void setDId(String dId) { this.dId = dId; }

    public String getDName() { return dName; }
    public void setDName(String dName) { this.dName = dName; }

    public List<EmpDetails> getEmployees() { return employees; }
    public void setEmployees(List<EmpDetails> employees) { this.employees = employees; }
}
