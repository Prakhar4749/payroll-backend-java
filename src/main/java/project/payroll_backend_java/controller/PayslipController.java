package project.payroll_backend_java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.payroll_backend_java.service.SalaryArchiveService;
import project.payroll_backend_java.service.EmailService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payslip")
@CrossOrigin(origins = "*")
public class PayslipController {

    @Autowired
    private SalaryArchiveService salaryArchiveService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/isit/{e_id}")
    public ResponseEntity<Map<String, Object>> checkEmployeeId(@PathVariable("e_id") String e_id) {
        try {
            Map<String, Object> serviceResponse = salaryArchiveService.checkEmployeeId(e_id);
            return ResponseEntity.ok(serviceResponse);
        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error checking employee ID: " + err.getMessage());
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/isit")
    public ResponseEntity<Map<String, Object>> checkPayslipInArchive(@RequestBody Map<String, Object> request) {
        try {
            String e_id = (String) request.get("e_id");
            String salary_month = (String) request.get("salary_month");
            Integer salary_year = request.get("salary_year") != null ?
                    Integer.valueOf(request.get("salary_year").toString()) : null;

            Map<String, Object> serviceResponse = salaryArchiveService.checkPayslipInArchive(e_id, salary_month, salary_year);
            return ResponseEntity.ok(serviceResponse);
        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error checking payslip: " + err.getMessage());
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    @PutMapping("/create_payslip")
    public ResponseEntity<Map<String, Object>> createSalaryArchive(@RequestBody Map<String, Object> request) {
        try {
            Map<String, Object> serviceResponse = salaryArchiveService.createSalaryArchive(request);
            return ResponseEntity.ok(serviceResponse);
        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error creating payslip: " + err.getMessage());
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/get_pdf")
    public ResponseEntity<Map<String, Object>> getPayslip(@RequestBody Map<String, Object> request) {
        try {
            String e_id = (String) request.get("e_id");
            String salary_month = (String) request.get("salary_month");
            Integer salary_year = request.get("salary_year") != null ?
                    Integer.valueOf(request.get("salary_year").toString()) : null;

            Map<String, Object> serviceResponse = salaryArchiveService.getPayslip(e_id, salary_month, salary_year);
            return ResponseEntity.ok(serviceResponse);
        } catch (Exception err) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error getting payslip: " + err.getMessage());
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/send_email")
    public ResponseEntity<Map<String, Object>> sendEmailWithPdf(
            @RequestParam(value = "to") String to,
            @RequestParam(value = "subject") String subject,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "html", required = false) String html,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "file_name", required = false) String file_name) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Validate required fields
            if (to == null || to.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Recipient email (to) is required");
                response.put("result", null);
                return ResponseEntity.ok(response);
            }

            if (subject == null || subject.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email subject is required");
                response.put("result", null);
                return ResponseEntity.ok(response);
            }

            // Call service
            Map<String, Object> serviceResponse = emailService.sendEmailWithAttachment(
                    to, subject, text, html, file, file_name
            );

            return ResponseEntity.ok(serviceResponse);

        } catch (Exception err) {
            response.put("success", false);
            response.put("message", "Error sending email: " + err.getMessage());
            response.put("result", null);
            return ResponseEntity.ok(response);
        }
    }

}