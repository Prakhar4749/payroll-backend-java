package project.payroll_backend_java.controller;

import project.payroll_backend_java.entity.DeptDetails;
import project.payroll_backend_java.service.DeptDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
            Map<String, Object> serviceResponse = deptDetailsService.getAllDept();

            if ((Boolean) serviceResponse.get("success")) {
                dataWhichIsNotChange = (List<DeptDetails>) serviceResponse.get("result");
                toRunSaved = true;
            }

            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            response.put("success", false);
            response.put("message", "error in fetching details");
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{d_id}")
    public ResponseEntity<Map<String, Object>> getDIdDetails(@PathVariable("d_id") String d_id) {
        try {
            Map<String, Object> serviceResponse = deptDetailsService.getDeptById(d_id);
            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "error in fetching details");
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/delete_d_id/{d_id}")
    public ResponseEntity<Map<String, Object>> deleteDId(@PathVariable("d_id") String d_id) {
        toRunSaved = false;

        try {
            Map<String, Object> serviceResponse = deptDetailsService.deleteDept(d_id);
            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "error in deleting " + d_id + " department");
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/add_dept")
    public ResponseEntity<Map<String, Object>> addNewDept(@RequestBody Map<String, String> request) {
        toRunSaved = false;

        try {
            String d_id = request.get("d_id");
            String d_name = request.get("d_name");

            Map<String, Object> serviceResponse = deptDetailsService.addNewDept(d_id, d_name);
            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "error adding department: " + err.getMessage());
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/update_d_id")
    public ResponseEntity<Map<String, Object>> updateDept(@RequestBody Map<String, String> request) {
        toRunSaved = false;

        try {
            String d_id = request.get("d_id");
            String new_d_id = request.get("new_d_id");
            String new_d_name = request.get("new_d_name");

            Map<String, Object> serviceResponse = deptDetailsService.updateDept(d_id, new_d_id, new_d_name);
            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "error in updating details: " + err.getMessage());
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/chk/1")
    public ResponseEntity<Map<String, Object>> chkIsItPresent(@RequestBody Map<String, String> request) {
        try {
            String d_id = request.get("d_id");
            String d_name = request.get("d_name");

            Map<String, Object> serviceResponse = deptDetailsService.checkDeptExists(d_id, d_name);
            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> result = new HashMap<>();
            result.put("d_id_exists", false);
            result.put("d_name_exists", false);

            response.put("success", false);
            response.put("message", "error checking department: " + err.getMessage());
            response.put("result", result);
            return ResponseEntity.ok(response);
        }
    }
}