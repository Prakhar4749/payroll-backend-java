package project.payroll_backend_java.service.impl;

import project.payroll_backend_java.entity.EmpEarningDetails;
import project.payroll_backend_java.repository.EmpEarningDetailsRepository;
import project.payroll_backend_java.service.EmpEarningDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpEarningDetailsServiceImpl implements EmpEarningDetailsService {

    @Autowired
    private EmpEarningDetailsRepository empEarningDetailsRepository;

    @Override
    public EmpEarningDetails saveEarningDetails(EmpEarningDetails earningDetails) {
        return empEarningDetailsRepository.save(earningDetails);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpEarningDetails> getEarningDetailsById(String eId) {
        return empEarningDetailsRepository.findById(eId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpEarningDetails> getAllEarningDetails() {
        return empEarningDetailsRepository.findAll();
    }

    @Override
    public EmpEarningDetails updateEarningDetails(EmpEarningDetails earningDetails) {
        return empEarningDetailsRepository.save(earningDetails);
    }

    @Override
    public void deleteEarningDetails(String eId) {
        empEarningDetailsRepository.deleteById(eId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpEarningDetails> getEmployeesBySalaryRange(Integer minSalary, Integer maxSalary) {
        return empEarningDetailsRepository.findByBasicSalaryBetween(minSalary, maxSalary);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer calculateTotalEarnings(String eId) {
        // For now, return null or implement basic calculation
        Optional<EmpEarningDetails> earning = empEarningDetailsRepository.findById(eId);
        if (earning.isPresent()) {
            EmpEarningDetails details = earning.get();
            // Basic calculation without complex query
            int total = (details.getBasicSalary() != null ? details.getBasicSalary() : 0) +
                    (details.getHra() != null ? details.getHra() : 0) +
                    (details.getDa() != null ? details.getDa() : 0);
            return total;
        }
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public Double getAverageBasicSalary() {
        // Simple implementation without custom query
        List<EmpEarningDetails> allEarnings = empEarningDetailsRepository.findAll();
        if (allEarnings.isEmpty()) {
            return 0.0;
        }

        double sum = allEarnings.stream()
                .mapToInt(e -> e.getBasicSalary() != null ? e.getBasicSalary() : 0)
                .sum();

        return sum / allEarnings.size();
    }
}
