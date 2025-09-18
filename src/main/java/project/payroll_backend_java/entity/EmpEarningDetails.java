package project.payroll_backend_java.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "emp_earning_details")
public class EmpEarningDetails {

    @Id
    @Column(name = "e_id", length = 5, nullable = false)
    private String eId;

    @Column(name = "e_name", length = 30, nullable = false)
    private String eName;

    @Column(name = "basic_salary", nullable = false)
    private Integer basicSalary;

    @Column(name = "special_pay")
    private Integer specialPay;

    @Column(name = "dearness_allowance")
    private Integer dearnessAllowance;

    @Column(name = "DA")
    private Integer da;

    @Column(name = "ADA")
    private Integer ada;

    @Column(name = "interim_relief")
    private Integer interimRelief;

    @Column(name = "HRA")
    private Integer hra;

    @Column(name = "CCA")
    private Integer cca;

    @Column(name = "conveyance")
    private Integer conveyance;

    @Column(name = "medical")
    private Integer medical;

    @Column(name = "washing_allowance")
    private Integer washingAllowance;

    @Column(name = "BDP")
    private Integer bdp;

    @Column(name = "arrears")
    private Integer arrears;

    // ADD THIS RELATIONSHIP - This is the key addition!
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "e_id")
    @JsonIgnore
    private EmpDetails empDetails;

    // Constructors
    public EmpEarningDetails() {}

    public EmpEarningDetails(String eId, String eName, Integer basicSalary) {
        this.eId = eId;
        this.eName = eName;
        this.basicSalary = basicSalary != null ? basicSalary : 0;
        // Initialize all other fields with 0
        this.specialPay = 0;
        this.dearnessAllowance = 0;
        this.da = 0;
        this.ada = 0;
        this.interimRelief = 0;
        this.hra = 0;
        this.cca = 0;
        this.conveyance = 0;
        this.medical = 0;
        this.washingAllowance = 0;
        this.bdp = 0;
        this.arrears = 0;
    }

    // All Getters and Setters
    public String getEId() { return eId; }
    public void setEId(String eId) { this.eId = eId; }

    public String getEName() { return eName; }
    public void setEName(String eName) { this.eName = eName; }

    public Integer getBasicSalary() { return basicSalary; }
    public void setBasicSalary(Integer basicSalary) { this.basicSalary = basicSalary; }

    public Integer getSpecialPay() { return specialPay; }
    public void setSpecialPay(Integer specialPay) { this.specialPay = specialPay; }

    public Integer getDearnessAllowance() { return dearnessAllowance; }
    public void setDearnessAllowance(Integer dearnessAllowance) { this.dearnessAllowance = dearnessAllowance; }

    public Integer getDa() { return da; }
    public void setDa(Integer da) { this.da = da; }

    public Integer getAda() { return ada; }
    public void setAda(Integer ada) { this.ada = ada; }

    public Integer getInterimRelief() { return interimRelief; }
    public void setInterimRelief(Integer interimRelief) { this.interimRelief = interimRelief; }

    public Integer getHra() { return hra; }
    public void setHra(Integer hra) { this.hra = hra; }

    public Integer getCca() { return cca; }
    public void setCca(Integer cca) { this.cca = cca; }

    public Integer getConveyance() { return conveyance; }
    public void setConveyance(Integer conveyance) { this.conveyance = conveyance; }

    public Integer getMedical() { return medical; }
    public void setMedical(Integer medical) { this.medical = medical; }

    public Integer getWashingAllowance() { return washingAllowance; }
    public void setWashingAllowance(Integer washingAllowance) { this.washingAllowance = washingAllowance; }

    public Integer getBdp() { return bdp; }
    public void setBdp(Integer bdp) { this.bdp = bdp; }

    public Integer getArrears() { return arrears; }
    public void setArrears(Integer arrears) { this.arrears = arrears; }

    // NEW GETTER/SETTER FOR RELATIONSHIP
    public EmpDetails getEmpDetails() { return empDetails; }
    public void setEmpDetails(EmpDetails empDetails) { this.empDetails = empDetails; }
}
