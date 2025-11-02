package project.payroll_backend_java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.payroll_backend_java.entity.DeptDetails;
import project.payroll_backend_java.repository.DeptDetailsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeptDetailsService {

    @Autowired
    private DeptDetailsRepository deptRepo;

    public Map<String, Object> getAllDept() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<DeptDetails> departments = deptRepo.findAllDept();
            response.put("success", true);
            response.put("message", "data has been fetched successfully");
            response.put("result", departments);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "error fetching departments: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }

    public Map<String, Object> getDeptById(String d_id) {
        Map<String, Object> response = new HashMap<>();
        try {
            DeptDetails dept = deptRepo.findDeptById(d_id);
            if (dept != null) {
                response.put("success", true);
                response.put("message", "department found successfully");
                response.put("result", dept);
            } else {
                response.put("success", false);
                response.put("message", "department with d_id '" + d_id + "' not found");
                response.put("result", null);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "error retrieving department: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }

    public Map<String, Object> deleteDept(String d_id) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Check if department exists
            DeptDetails dept = deptRepo.findDeptById(d_id);
            if (dept == null) {
                response.put("success", false);
                response.put("message", "department with d_id '" + d_id + "' not found");
                response.put("result", null);
                return response;
            }

            int result = deptRepo.deleteDeptById(d_id);
            if (result > 0) {
                response.put("success", true);
                response.put("message", "department deleted successfully");
                response.put("result", null);
            } else {
                response.put("success", false);
                response.put("message", "failed to delete department");
                response.put("result", null);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "error deleting department: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }

    public Map<String, Object> addNewDept(String d_id, String d_name) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate input
            if (d_id == null || d_id.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "d_id cannot be empty");
                response.put("result", null);
                return response;
            }

            if (d_name == null || d_name.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "d_name cannot be empty");
                response.put("result", null);
                return response;
            }

            // Check if department already exists
            Map<String, Object> checkResult = deptRepo.checkIsitPresent(d_id, d_name);

            Long d_id_count = ((Number) checkResult.get("d_id_count")).longValue();
            Long d_name_count = ((Number) checkResult.get("d_name_count")).longValue();

            // Check for duplicate ID
            if (d_id_count > 0) {
                response.put("success", false);
                response.put("message", "d_id '" + d_id + "' already exists");
                response.put("result", null);
                return response;
            }

            // Check for duplicate name
            if (d_name_count > 0) {
                response.put("success", false);
                response.put("message", "d_name '" + d_name + "' already exists");
                response.put("result", null);
                return response;
            }

            // Add new department
            int result = deptRepo.addNewDept(d_id, d_name);
            if (result > 0) {
                response.put("success", true);
                response.put("message", "department added successfully");
                response.put("result", deptRepo.findDeptById(d_id));
            } else {
                response.put("success", false);
                response.put("message", "failed to add department");
                response.put("result", null);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "error adding department: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }

    public Map<String, Object> updateDept(String d_id, String new_d_id, String new_d_name) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate input
            if (new_d_id == null || new_d_id.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "new_d_id cannot be empty");
                response.put("result", null);
                return response;
            }

            if (new_d_name == null || new_d_name.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "new_d_name cannot be empty");
                response.put("result", null);
                return response;
            }

            // Check if the department exists
            DeptDetails existingDept = deptRepo.findDeptById(d_id);
            if (existingDept == null) {
                response.put("success", false);
                response.put("message", "department with d_id '" + d_id + "' not found");
                response.put("result", null);
                return response;
            }

            // Check if updating to a different ID or name
            boolean idChanged = !d_id.equals(new_d_id);
            boolean nameChanged = !existingDept.getD_name().equals(new_d_name);

            if (idChanged || nameChanged) {
                Map<String, Object> checkResult = deptRepo.checkIsitPresent(new_d_id, new_d_name);

                Long d_id_count = ((Number) checkResult.get("d_id_count")).longValue();
                Long d_name_count = ((Number) checkResult.get("d_name_count")).longValue();

                // If ID is being changed and new ID already exists
                if (idChanged && d_id_count > 0) {
                    response.put("success", false);
                    response.put("message", "d_id '" + new_d_id + "' already exists");
                    response.put("result", null);
                    return response;
                }

                // If name is being changed and new name already exists
                if (nameChanged && d_name_count > 0) {
                    response.put("success", false);
                    response.put("message", "d_name '" + new_d_name + "' already exists");
                    response.put("result", null);
                    return response;
                }
            }

            // Update department
            int result = deptRepo.addNewDept(d_id, new_d_name, new_d_id);
            if (result > 0) {
                response.put("success", true);
                response.put("message", "department updated successfully");
                response.put("result", deptRepo.findDeptById(new_d_id));
            } else {
                response.put("success", false);
                response.put("message", "failed to update department");
                response.put("result", null);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "error updating department: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }

    public Map<String, Object> checkDeptExists(String d_id, String d_name) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> checkResult = deptRepo.checkIsitPresent(d_id, d_name);

            Long d_id_count = ((Number) checkResult.get("d_id_count")).longValue();
            Long d_name_count = ((Number) checkResult.get("d_name_count")).longValue();

            Map<String, Object> result = new HashMap<>();
            result.put("d_id_exists", d_id_count > 0);
            result.put("d_name_exists", d_name_count > 0);

            response.put("success", true);
            response.put("message", "check completed successfully");
            response.put("result", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "error checking department: " + e.getMessage());
            response.put("result", null);
        }
        return response;
    }
}