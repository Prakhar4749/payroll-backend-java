package project.payroll_backend_java.service.impl;

import project.payroll_backend_java.entity.SalaryArchive;
import project.payroll_backend_java.entity.SalaryArchiveId;
import project.payroll_backend_java.repository.SalaryArchiveRepository;
import project.payroll_backend_java.service.SalaryArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SalaryArchiveServiceImpl implements SalaryArchiveService {

    @Autowired
    private SalaryArchiveRepository salaryArchiveRepository;

    @Override
    public SalaryArchive saveSalaryRecord(SalaryArchive salaryRecord) {
        return salaryArchiveRepository.save(salaryRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SalaryArchive> getSalaryRecordById(SalaryArchiveId id) {
        return salaryArchiveRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryArchive> getAllSalaryRecords() {
        return salaryArchiveRepository.findAll();
    }

    @Override
    public void deleteSalaryRecord(SalaryArchiveId id) {
        salaryArchiveRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryArchive> getSalaryRecordsByEmployeeId(String eId) {
        return salaryArchiveRepository.findByeId(eId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryArchive> getSalaryRecordsByMonthAndYear(String month, Integer year) {
        return salaryArchiveRepository.findBySalaryMonthAndSalaryYear(month, year);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaryArchive> getSalaryRecordsByYear(Integer year) {
        return salaryArchiveRepository.findBySalaryYear(year);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByeIdAndSalaryMonthAndSalaryYear(String eId, String month, Integer year) {
        SalaryArchiveId id = new SalaryArchiveId();
        id.setEId(eId);
        id.setSalaryMonth(month);
        id.setSalaryYear(year);
        return salaryArchiveRepository.existsById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public Long calculateTotalSalaryPaid(String month, Integer year) {
        // Simple implementation - sum up net payable amounts
        List<SalaryArchive> records = salaryArchiveRepository.findBySalaryMonthAndSalaryYear(month, year);
        return records.stream()
                .mapToLong(record -> record.getNetPayable() != null ? record.getNetPayable() : 0)
                .sum();
    }
}
