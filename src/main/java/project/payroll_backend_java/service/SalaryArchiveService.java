package project.payroll_backend_java.service;

import project.payroll_backend_java.entity.SalaryArchive;
import project.payroll_backend_java.entity.SalaryArchiveId;
import java.util.List;
import java.util.Optional;

public interface SalaryArchiveService {

    // Basic CRUD operations
    SalaryArchive saveSalaryRecord(SalaryArchive salaryRecord);
    Optional<SalaryArchive> getSalaryRecordById(SalaryArchiveId id);
    List<SalaryArchive> getAllSalaryRecords();
    void deleteSalaryRecord(SalaryArchiveId id);

    // Business logic methods
    List<SalaryArchive> getSalaryRecordsByEmployeeId(String eId);
    List<SalaryArchive> getSalaryRecordsByMonthAndYear(String month, Integer year);
    List<SalaryArchive> getSalaryRecordsByYear(Integer year);
    boolean existsByeIdAndSalaryMonthAndSalaryYear(String eId, String month, Integer year);
    Long calculateTotalSalaryPaid(String month, Integer year);

}
