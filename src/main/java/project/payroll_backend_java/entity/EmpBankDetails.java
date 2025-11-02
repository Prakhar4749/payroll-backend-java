package project.payroll_backend_java.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "emp_bank_details")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@NoArgsConstructor
@AllArgsConstructor
public class EmpBankDetails {

    @Id
    @Column(name = "e_id", length = 5, nullable = false)
    private String e_id;

    @Column(name = "e_name", length = 30, nullable = false)
    private String e_name;

    @Column(name = "e_bank_name", length = 50, nullable = false)
    private String e_bank_name;

    @Column(name = "e_bank_acc_number", nullable = false)
    private Long e_bank_acc_number;

    @Column(name = "e_pan_number", length = 10, nullable = false)
    private String e_pan_number;

    @Column(name = "e_bank_IFSC", length = 11, nullable = false)
    private String e_bank_IFSC;

    @Column(name = "e_cpf_or_gpf_number", length = 20)
    private String e_cpf_or_gpf_number;



    @OneToOne
    @MapsId
    @JoinColumn(name = "e_id") // shared primary key
    @JsonIgnore
    private EmpDetails emp_details;

    public String getE_id() {
        return e_id;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }

    public String getE_name() {
        return e_name;
    }

    public void setE_name(String e_name) {
        this.e_name = e_name;
    }

    public String getE_bank_name() {
        return e_bank_name;
    }

    public void setE_bank_name(String e_bank_name) {
        this.e_bank_name = e_bank_name;
    }

    public Long getE_bank_acc_number() {
        return e_bank_acc_number;
    }

    public void setE_bank_acc_number(Long e_bank_acc_number) {
        this.e_bank_acc_number = e_bank_acc_number;
    }

    public String getE_pan_number() {
        return e_pan_number;
    }

    public void setE_pan_number(String e_pan_number) {
        this.e_pan_number = e_pan_number;
    }

    public String getE_bank_IFSC() {
        return e_bank_IFSC;
    }

    public void setE_bank_IFSC(String e_bank_IFSC) {
        this.e_bank_IFSC = e_bank_IFSC;
    }

    public String getE_cpf_or_gpf_number() {
        return e_cpf_or_gpf_number;
    }

    public void setE_cpf_or_gpf_number(String e_cpf_or_gpf_number) {
        this.e_cpf_or_gpf_number = e_cpf_or_gpf_number;
    }

    public EmpDetails getEmp_details() {
        return emp_details;
    }

    public void setEmp_details(EmpDetails emp_details) {
        this.emp_details = emp_details;
    }
}


