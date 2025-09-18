package project.payroll_backend_java.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "emp_details")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmpDetails {

    @Id
    @Column(name = "e_id", length = 5, nullable = false)
    private String eId;

    @Column(name = "e_name", length = 30, nullable = false)
    private String eName;

    @Column(name = "e_mobile_number", nullable = false)
    private Long eMobileNumber;

    @Column(name = "e_gender", length = 10, nullable = false)
    private String eGender;

    @Column(name = "e_email", length = 50)
    private String eEmail;

    @Column(name = "e_address", length = 100)
    private String eAddress;

    @Lob
    @Column(name = "e_photo", columnDefinition = "LONGTEXT")
    private byte[] ePhoto;

    @Column(name = "e_designation", length = 30)
    private String eDesignation;

    @Column(name = "e_group", length = 20)
    private String eGroup;

    @Column(name = "e_date_of_joining")
    private LocalDate eDateOfJoining;

    @Column(name = "e_DOB")
    private LocalDate eDOB;

    // Department relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "d_id")
    @JsonIgnoreProperties({"employees"})
    private DeptDetails department;

    // ADD THESE RELATIONSHIPS - This is what was missing!
    @OneToOne(mappedBy = "empDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EmpBankDetails bankDetails;

    @OneToOne(mappedBy = "empDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EmpDeductionDetails deductionDetails;

    @OneToOne(mappedBy = "empDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EmpEarningDetails earningDetails;

    // Constructors
    public EmpDetails() {}

    // All existing getters and setters...
    public String getEId() { return eId; }
    public void setEId(String eId) { this.eId = eId; }

    public String getEName() { return eName; }
    public void setEName(String eName) { this.eName = eName; }

    public Long getEMobileNumber() { return eMobileNumber; }
    public void setEMobileNumber(Long eMobileNumber) { this.eMobileNumber = eMobileNumber; }

    public String getEGender() { return eGender; }
    public void setEGender(String eGender) { this.eGender = eGender; }

    public String getEEmail() { return eEmail; }
    public void setEEmail(String eEmail) { this.eEmail = eEmail; }

    public String getEAddress() { return eAddress; }
    public void setEAddress(String eAddress) { this.eAddress = eAddress; }

    public byte[] getEPhoto() { return ePhoto; }
    public void setEPhoto(byte[] ePhoto) { this.ePhoto = ePhoto; }

    public String getEDesignation() { return eDesignation; }
    public void setEDesignation(String eDesignation) { this.eDesignation = eDesignation; }

    public String getEGroup() { return eGroup; }
    public void setEGroup(String eGroup) { this.eGroup = eGroup; }

    public LocalDate getEDateOfJoining() { return eDateOfJoining; }
    public void setEDateOfJoining(LocalDate eDateOfJoining) { this.eDateOfJoining = eDateOfJoining; }

    public LocalDate getEDOB() { return eDOB; }
    public void setEDOB(LocalDate eDOB) { this.eDOB = eDOB; }

    public DeptDetails getDepartment() { return department; }
    public void setDepartment(DeptDetails department) { this.department = department; }

    // NEW GETTERS AND SETTERS FOR RELATIONSHIPS
    public EmpBankDetails getBankDetails() { return bankDetails; }
    public void setBankDetails(EmpBankDetails bankDetails) { this.bankDetails = bankDetails; }

    public EmpDeductionDetails getDeductionDetails() { return deductionDetails; }
    public void setDeductionDetails(EmpDeductionDetails deductionDetails) { this.deductionDetails = deductionDetails; }

    public EmpEarningDetails getEarningDetails() { return earningDetails; }
    public void setEarningDetails(EmpEarningDetails earningDetails) { this.earningDetails = earningDetails; }
}
