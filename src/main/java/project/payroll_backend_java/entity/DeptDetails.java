package project.payroll_backend_java.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "dept_details")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@NoArgsConstructor
@AllArgsConstructor
public class DeptDetails {
    public String getD_id() {
        return d_id;
    }

    public String getD_name() {
        return d_name;
    }

    public List<EmpDetails> getEmployees() {
        return employees;
    }

    @Id
    @Column(name = "d_id", length = 5, nullable = false)
    private String d_id;

    @Column(name = "d_name", length = 30, nullable = false, unique = true)
    private String d_name;

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public void setD_name(String d_name) {
        this.d_name = d_name;
    }

    public void setEmployees(List<EmpDetails> employees) {
        this.employees = employees;
    }

    // Add @JsonIgnore to prevent infinite recursion during JSON serialization
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // THIS IS THE KEY FIX
    private List<EmpDetails> employees;


}
