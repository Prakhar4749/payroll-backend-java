package project.payroll_backend_java.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import project.payroll_backend_java.entity.DeptDetails;
import project.payroll_backend_java.entity.EmpBankDetails;
import project.payroll_backend_java.entity.EmpEarningDetails;
import project.payroll_backend_java.entity.EmpDeductionDetails;


@Entity
@Table(name = "emp_details")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

@NoArgsConstructor
@AllArgsConstructor
public class EmpDetails {

    @Id
    @Column(name = "e_id", length = 5, nullable = false)
    private String e_id;

    @Column(name = "e_name", length = 30, nullable = false)
    private String e_name;

    @Column(name = "e_mobile_number", nullable = false)
    private Long e_mobile_number;

    @Column(name = "e_gender", length = 10, nullable = false)
    private String e_gender;

    @Column(name = "e_email", length = 50)
    private String e_email;

    @Column(name = "e_address", length = 100)
    private String e_address;

    @Lob
    @Column(name = "e_photo", columnDefinition = "LONGTEXT")
    private byte[] e_photo;

    @Column(name = "d_id", insertable = false, updatable = false)
    private String d_id;

    @Column(name = "e_designation", length = 30)
    private String e_designation;

    @Column(name = "e_group", length = 20)
    private String e_group;

    @Column(name = "e_date_of_joining")
    private LocalDate e_date_of_joining;

    @Column(name = "e_DOB")
    private LocalDate e_DOB;

    // Department relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "d_id")
    @JsonIgnoreProperties({"employees"})
    private DeptDetails department;

    // One-to-one with bank details
    @OneToOne(mappedBy = "emp_details", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EmpBankDetails emp_bank_details;

    // One-to-one with deduction details
    @OneToOne(mappedBy = "emp_details", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EmpDeductionDetails emp_deduction_details;

    // One-to-one with earning details
    @OneToOne(mappedBy = "emp_details", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EmpEarningDetails emp_earning_details;


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

    public Long getE_mobile_number() {
        return e_mobile_number;
    }

    public void setE_mobile_number(Long e_mobile_number) {
        this.e_mobile_number = e_mobile_number;
    }

    public String getE_gender() {
        return e_gender;
    }

    public void setE_gender(String e_gender) {
        this.e_gender = e_gender;
    }

    public String getE_email() {
        return e_email;
    }

    public void setE_email(String e_email) {
        this.e_email = e_email;
    }

    public String getE_address() {
        return e_address;
    }

    public void setE_address(String e_address) {
        this.e_address = e_address;
    }

    public byte[] getE_photo() {
        return e_photo;
    }

    public void setE_photo(byte[] e_photo) {
        this.e_photo = e_photo;
    }

    public String getD_id() {
        return d_id;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public String getE_designation() {
        return e_designation;
    }

    public void setE_designation(String e_designation) {
        this.e_designation = e_designation;
    }

    public String getE_group() {
        return e_group;
    }

    public void setE_group(String e_group) {
        this.e_group = e_group;
    }

    public LocalDate getE_date_of_joining() {
        return e_date_of_joining;
    }

    public void setE_date_of_joining(LocalDate e_date_of_joining) {
        this.e_date_of_joining = e_date_of_joining;
    }

    public LocalDate getE_DOB() {
        return e_DOB;
    }

    public void setE_DOB(LocalDate e_DOB) {
        this.e_DOB = e_DOB;
    }

    public DeptDetails getDepartment() {
        return department;
    }

    public void setDepartment(DeptDetails department) {
        this.department = department;
    }

    public EmpBankDetails getEmp_bank_details() {
        return emp_bank_details;
    }

    public void setEmp_bank_details(EmpBankDetails emp_bank_details) {
        this.emp_bank_details = emp_bank_details;
    }

    public EmpDeductionDetails getEmp_deduction_details() {
        return emp_deduction_details;
    }

    public void setEmp_deduction_details(EmpDeductionDetails emp_deduction_details) {
        this.emp_deduction_details = emp_deduction_details;
    }

    public EmpEarningDetails getEmp_earning_details() {
        return emp_earning_details;
    }

    public void setEmp_earning_details(EmpEarningDetails emp_earning_details) {
        this.emp_earning_details = emp_earning_details;
    }
}
