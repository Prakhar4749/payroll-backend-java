package project.payroll_backend_java.service.impl;

import project.payroll_backend_java.entity.EmpBankDetails;
import project.payroll_backend_java.repository.EmpBankDetailsRepository;
import project.payroll_backend_java.service.EmpBankDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpBankDetailsServiceImpl implements EmpBankDetailsService {

    @Autowired
    private EmpBankDetailsRepository empBankDetailsRepository;

    @Override
    public EmpBankDetails saveBankDetails(EmpBankDetails bankDetails) {
        return empBankDetailsRepository.save(bankDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpBankDetails> getBankDetailsById(String eId) {
        return empBankDetailsRepository.findById(eId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpBankDetails> getAllBankDetails() {
        return empBankDetailsRepository.findAll();
    }

    @Override
    public EmpBankDetails updateBankDetails(EmpBankDetails bankDetails) {
        return empBankDetailsRepository.save(bankDetails);
    }

    @Override
    public void deleteBankDetails(String eId) {
        empBankDetailsRepository.deleteById(eId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpBankDetails> getBankDetailsByAccountNumber(Long accountNumber) {
        return empBankDetailsRepository.findByeBankAccNumber(accountNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpBankDetails> getBankDetailsByPanNumber(String panNumber) {
        return empBankDetailsRepository.findByePanNumber(panNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpBankDetails> getBankDetailsByBankName(String bankName) {
        return empBankDetailsRepository.findByeBankName(bankName);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByAccountNumber(Long accountNumber) {
        return empBankDetailsRepository.existsByeBankAccNumber(accountNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPanNumber(String panNumber) {
        return empBankDetailsRepository.existsByePanNumber(panNumber);
    }
}
