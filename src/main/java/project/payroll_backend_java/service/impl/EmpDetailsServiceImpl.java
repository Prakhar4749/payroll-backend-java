package project.payroll_backend_java.service.impl;

import project.payroll_backend_java.entity.EmpDetails;
import project.payroll_backend_java.repository.EmpDetailsRepository;
import project.payroll_backend_java.service.EmpDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpDetailsServiceImpl implements EmpDetailsService {

    @Autowired
    private EmpDetailsRepository empDetailsRepository;

    @Override
    public EmpDetails saveEmployee(EmpDetails employee) {
        return empDetailsRepository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpDetails> getEmployeeById(String eId) {
        return empDetailsRepository.findById(eId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpDetails> getAllEmployees() {
        return empDetailsRepository.findAll();
    }

    @Override
    public EmpDetails updateEmployee(EmpDetails employee) {
        return empDetailsRepository.save(employee);
    }

    @Override
    public void deleteEmployee(String eId) {
        empDetailsRepository.deleteById(eId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpDetails> getEmployeesByDepartment(String dId) {
        return empDetailsRepository.findByDepartment_dId(dId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpDetails> getEmployeesByDesignation(String designation) {
        // Simple implementation without custom repository method
        return empDetailsRepository.findAll().stream()
                .filter(emp -> designation.equals(emp.getEDesignation()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpDetails> getEmployeesByGender(String gender) {
        // Simple implementation without custom repository method
        return empDetailsRepository.findAll().stream()
                .filter(emp -> gender.equals(emp.getEGender()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpDetails> searchEmployeesByName(String name) {
        // Simple implementation without custom repository method
        return empDetailsRepository.findAll().stream()
                .filter(emp -> emp.getEName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpDetails> getEmployeesJoinedBetween(LocalDate startDate, LocalDate endDate) {
        // Simple implementation without custom repository method
        return empDetailsRepository.findAll().stream()
                .filter(emp -> emp.getEDateOfJoining() != null &&
                        !emp.getEDateOfJoining().isBefore(startDate) &&
                        !emp.getEDateOfJoining().isAfter(endDate))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return empDetailsRepository.existsByeEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByMobileNumber(Long mobileNumber) {
        return empDetailsRepository.existsByeMobileNumber(mobileNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpDetails> getEmployeeWithAllDetails(String eId) {
        return empDetailsRepository.findByIdWithAllDetails(eId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getEmployeeCountByDepartment(String dId) {
        // Simple implementation - count employees in a department
        return empDetailsRepository.findAll().stream()
                .filter(emp -> emp.getDepartment() != null &&
                        dId.equals(emp.getDepartment().getDId()))
                .count();
    }
}
