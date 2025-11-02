package project.payroll_backend_java.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "emp_earning_details")

@NoArgsConstructor
@AllArgsConstructor
public class EmpEarningDetails {

    @Id
    @Column(name = "e_id", length = 5, nullable = false)
    private String e_id;

    @Column(name = "e_name", length = 30, nullable = false)
    private String e_name;

    @Column(name = "basic_salary", nullable = false)
    private Integer basic_salary;

    @Column(name = "special_pay")
    private Integer special_pay;

    @Column(name = "dearness_allowance")
    private Integer dearness_allowance;

    @Column(name = "DA")
    private Integer DA;

    @Column(name = "ADA")
    private Integer ADA;

    @Column(name = "interim_relief")
    private Integer interim_relief;

    @Column(name = "HRA")
    private Integer HRA;

    @Column(name = "CCA")
    private Integer CCA;

    @Column(name = "conveyance")
    private Integer conveyance;

    @Column(name = "medical")
    private Integer medical;

    @Column(name = "washing_allowance")
    private Integer washing_allowance;

    @Column(name = "BDP")
    private Integer BDP;

    @Column(name = "arrears")
    private Integer arrears;

    // ✅ Corrected Relationship
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "e_id", referencedColumnName = "e_id", insertable = false, updatable = false)
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

    public Integer getBasic_salary() {
        return basic_salary;
    }

    public void setBasic_salary(Integer basic_salary) {
        this.basic_salary = basic_salary;
    }

    public Integer getSpecial_pay() {
        return special_pay;
    }

    public void setSpecial_pay(Integer special_pay) {
        this.special_pay = special_pay;
    }

    public Integer getDearness_allowance() {
        return dearness_allowance;
    }

    public void setDearness_allowance(Integer dearness_allowance) {
        this.dearness_allowance = dearness_allowance;
    }

    public Integer getDA() {
        return DA;
    }

    public void setDA(Integer DA) {
        this.DA = DA;
    }

    public Integer getADA() {
        return ADA;
    }

    public void setADA(Integer ADA) {
        this.ADA = ADA;
    }

    public Integer getInterim_relief() {
        return interim_relief;
    }

    public void setInterim_relief(Integer interim_relief) {
        this.interim_relief = interim_relief;
    }

    public Integer getHRA() {
        return HRA;
    }

    public void setHRA(Integer HRA) {
        this.HRA = HRA;
    }

    public Integer getCCA() {
        return CCA;
    }

    public void setCCA(Integer CCA) {
        this.CCA = CCA;
    }

    public Integer getConveyance() {
        return conveyance;
    }

    public void setConveyance(Integer conveyance) {
        this.conveyance = conveyance;
    }

    public Integer getMedical() {
        return medical;
    }

    public void setMedical(Integer medical) {
        this.medical = medical;
    }

    public Integer getWashing_allowance() {
        return washing_allowance;
    }

    public void setWashing_allowance(Integer washing_allowance) {
        this.washing_allowance = washing_allowance;
    }

    public Integer getBDP() {
        return BDP;
    }

    public void setBDP(Integer BDP) {
        this.BDP = BDP;
    }

    public Integer getArrears() {
        return arrears;
    }

    public void setArrears(Integer arrears) {
        this.arrears = arrears;
    }

    public EmpDetails getEmp_details() {
        return emp_details;
    }

    public void setEmp_details(EmpDetails emp_details) {
        this.emp_details = emp_details;
    }
}
