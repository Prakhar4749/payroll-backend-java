package project.payroll_backend_java.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "emp_bank_details")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmpBankDetails {

    @Id
    @Column(name = "e_id", length = 5, nullable = false)
    private String eId;

    @Column(name = "e_name", length = 30, nullable = false)
    private String eName;

    @Column(name = "e_bank_name", length = 50, nullable = false)
    private String eBankName;

    @Column(name = "e_bank_acc_number", nullable = false)
    private Long eBankAccNumber;

    @Column(name = "e_pan_number", length = 10, nullable = false)
    private String ePanNumber;

    @Column(name = "e_bank_IFSC", length = 11, nullable = false)
    private String eBankIFSC;

    @Column(name = "e_cpf_or_gpf_number", length = 20)
    private String eCpfOrGpfNumber;

    // ADD THIS RELATIONSHIP
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "e_id")
    @JsonIgnore
    private EmpDetails empDetails;

    // Constructors
    public EmpBankDetails() {}

    public EmpBankDetails(String eId, String eName, String bankName, Long accountNumber, String panNumber, String ifsc) {
        this.eId = eId;
        this.eName = eName;
        this.eBankName = bankName;
        this.eBankAccNumber = accountNumber;
        this.ePanNumber = panNumber;
        this.eBankIFSC = ifsc;
    }

    // All Getters and Setters
    public String getEId() { return eId; }
    public void setEId(String eId) { this.eId = eId; }

    public String getEName() { return eName; }
    public void setEName(String eName) { this.eName = eName; }

    public String getEBankName() { return eBankName; }
    public void setEBankName(String eBankName) { this.eBankName = eBankName; }

    public Long getEBankAccNumber() { return eBankAccNumber; }
    public void setEBankAccNumber(Long eBankAccNumber) { this.eBankAccNumber = eBankAccNumber; }

    public String getEPanNumber() { return ePanNumber; }
    public void setEPanNumber(String ePanNumber) { this.ePanNumber = ePanNumber; }

    public String getEBankIFSC() { return eBankIFSC; }
    public void setEBankIFSC(String eBankIFSC) { this.eBankIFSC = eBankIFSC; }

    public String getECpfOrGpfNumber() { return eCpfOrGpfNumber; }
    public void setECpfOrGpfNumber(String eCpfOrGpfNumber) { this.eCpfOrGpfNumber = eCpfOrGpfNumber; }

    // NEW GETTER/SETTER FOR RELATIONSHIP
    public EmpDetails getEmpDetails() { return empDetails; }
    public void setEmpDetails(EmpDetails empDetails) { this.empDetails = empDetails; }
}
