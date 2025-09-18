package project.payroll_backend_java.controller;

import project.payroll_backend_java.entity.DeptDetails;
import project.payroll_backend_java.service.DeptDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/dept")
@CrossOrigin(origins = "*")
public class DeptController {

    @Autowired
    private DeptDetailsService deptDetailsService;

    // Caching mechanism (EXACT from Node.js)
    private static boolean toRunSaved = false;
    private static List<DeptDetails> dataWhichIsNotChange = null;

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getAllDeptDetails() {
        Map<String, Object> response = new HashMap<>();

        if (toRunSaved) {
            response.put("success", true);
            response.put("message", "data has been fetched successfully");
            response.put("result", dataWhichIsNotChange);
            return ResponseEntity.ok(response);
        }

        try {
            List<DeptDetails> departments = deptDetailsService.getAllDepartments();
            dataWhichIsNotChange = departments;
            toRunSaved = true;

            response.put("success", true);
            response.put("message", "data has been fetched successfully");
            response.put("result", departments);
            return ResponseEntity.ok(response);

        } catch (Exception err) {
            response.put("success", false);
            response.put("message", "error in fetching details");
            response.put("result", err.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{d_id}")
    public ResponseEntity<Map<String, Object>> getDIdDetails(@PathVariable("d_id") String dId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<DeptDetails> department = deptDetailsService.getDepartmentById(dId);
            List<DeptDetails> result = department.map(List::of).orElse(List.of());

            response.put("success", true);
            response.put("message", "data has been fetched successfully");
            response.put("result", result);
            return ResponseEntity.ok(response);

        } catch (Exception err) {
            response.put("success", false);
            response.put("message", "error in fetching details");
            response.put("result", err.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/delete_d_id/{d_id}")
    public ResponseEntity<Map<String, Object>> deleteDId(@PathVariable("d_id") String dId) {
        Map<String, Object> response = new HashMap<>();
        toRunSaved = false;

        try {
            deptDetailsService.deleteDepartment(dId);
            response.put("success", true);
            response.put("message", "Department " + dId + " has been deleted");
            response.put("result", dId);
            return ResponseEntity.ok(response);

        } catch (Exception err) {
            response.put("success", false);
            response.put("message", "error in deleting " + dId + " department");
            response.put("result", err.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/add_dept")
    public ResponseEntity<Map<String, Object>> addNewDept(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        toRunSaved = false;

        try {
            String dId = request.get("d_id");
            String dName = request.get("d_name");

            DeptDetails newDept = new DeptDetails();
            newDept.setDId(dId);
            newDept.setDName(dName);
            deptDetailsService.saveDepartment(newDept);

            response.put("success", true);
            response.put("message", "the given department has been added");
            response.put("result", "");
            return ResponseEntity.ok(response);

        } catch (Exception err) {
            response.put("success", false);
            response.put("message", "the given department has not been added");
            response.put("result", "");
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/update_d_id")
    public ResponseEntity<Map<String, Object>> updateDept(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        toRunSaved = false;

        try {
            String dId = request.get("d_id");
            String newDId = request.get("new_d_id");
            String newDName = request.get("new_d_name");

            if (newDId == null || newDId.trim().isEmpty() ||
                    newDName == null || newDName.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "please enter valid new department ID and name");
                response.put("result", "error");
                return ResponseEntity.status(400).body(response);
            }

            deptDetailsService.deleteDepartment(dId);

            DeptDetails updatedDept = new DeptDetails();
            updatedDept.setDId(newDId);
            updatedDept.setDName(newDName);
            deptDetailsService.saveDepartment(updatedDept);

            response.put("success", true);
            response.put("message", "the department details has been updated successfully");
            response.put("result", "");
            return ResponseEntity.ok(response);

        } catch (Exception err) {
            response.put("success", false);
            response.put("message", "error in updating details");
            response.put("result", err.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/chk/1")
    public ResponseEntity<Map<String, Object>> chkIsItPresent(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String dId = request.get("d_id");
            String dName = request.get("d_name");

            String processedDId = (dId != null) ? dId.replaceAll("\\s", "").toUpperCase() : null;
            String processedDName = (dName != null) ? dName.replaceAll("\\s", "").toUpperCase() : null;

            boolean dIdExists = false;
            boolean dNameExists = false;

            List<DeptDetails> allDepts = deptDetailsService.getAllDepartments();

            if (processedDId != null && !processedDId.trim().isEmpty()) {
                for (DeptDetails dept : allDepts) {
                    if (dept.getDId().replaceAll("\\s", "").toUpperCase().equals(processedDId)) {
                        dIdExists = true;
                        break;
                    }
                }
            }

            if (processedDName != null && !processedDName.trim().isEmpty()) {
                for (DeptDetails dept : allDepts) {
                    if (dept.getDName().replaceAll("\\s", "").toUpperCase().equals(processedDName)) {
                        dNameExists = true;
                        break;
                    }
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("d_id", dIdExists);
            result.put("d_name", dNameExists);

            response.put("success", true);
            response.put("message", "d_id exit ");
            response.put("result", result);
            return ResponseEntity.ok(response);

        } catch (Exception err) {
            Map<String, Object> result = new HashMap<>();
            result.put("d_id", false);
            result.put("d_name", false);

            response.put("success", false);
            response.put("message", "d_id does not exist");
            response.put("result", result);
            return ResponseEntity.ok(response);
        }
    }
}
