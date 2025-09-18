package project.payroll_backend_java.service.impl;

import project.payroll_backend_java.entity.EmpDeductionDetails;
import project.payroll_backend_java.repository.EmpDeductionDetailsRepository;
import project.payroll_backend_java.service.EmpDeductionDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpDeductionDetailsServiceImpl implements EmpDeductionDetailsService {

    @Autowired
    private EmpDeductionDetailsRepository empDeductionDetailsRepository;

    @Override
    public EmpDeductionDetails saveDeductionDetails(EmpDeductionDetails deductionDetails) {
        return empDeductionDetailsRepository.save(deductionDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpDeductionDetails> getDeductionDetailsById(String eId) {
        return empDeductionDetailsRepository.findById(eId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpDeductionDetails> getAllDeductionDetails() {
        return empDeductionDetailsRepository.findAll();
    }

    @Override
    public EmpDeductionDetails updateDeductionDetails(EmpDeductionDetails deductionDetails) {
        return empDeductionDetailsRepository.save(deductionDetails);
    }

    @Override
    public void deleteDeductionDetails(String eId) {
        empDeductionDetailsRepository.deleteById(eId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpDeductionDetails> getEmployeesWithLeaveDeductions() {
        return empDeductionDetailsRepository.findByLeaveDaysGreaterThan(0);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer calculateTotalDeductions(String eId) {
        // Simple implementation without custom query
        Optional<EmpDeductionDetails> deduction = empDeductionDetailsRepository.findById(eId);
        if (deduction.isPresent()) {
            EmpDeductionDetails details = deduction.get();
            // Basic calculation
            int total = (details.getLeaveDeductionAmount() != null ? details.getLeaveDeductionAmount() : 0) +
                    (details.getDeductionCPF() != null ? details.getDeductionCPF() : 0) +
                    (details.getGis() != null ? details.getGis() : 0) +
                    (details.getIncomeTax() != null ? details.getIncomeTax() : 0) +
                    (details.getProfessionalTax() != null ? details.getProfessionalTax() : 0);
            return total;
        }
        return 0;
    }
}
