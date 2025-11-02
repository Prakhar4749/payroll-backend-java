package project.payroll_backend_java.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public Map<String, Object> sendEmailWithAttachment(String to, String subject,
                                                       String text, String html,
                                                       MultipartFile file, String fileName) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate input
            if (to == null || to.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Recipient email cannot be empty");
                response.put("result", null);
                return response;
            }

            if (subject == null || subject.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email subject cannot be empty");
                response.put("result", null);
                return response;
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);

            // Use HTML content if provided, otherwise use plain text
            if (html != null && !html.trim().isEmpty()) {
                helper.setText(text, html);
            } else {
                helper.setText(text);
            }

            // Add attachment if provided
            if (file != null && !file.isEmpty()) {
                helper.addAttachment(fileName, file);
            }

            mailSender.send(message);

            response.put("success", true);
            response.put("message", "Email sent successfully");
            response.put("result", null);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error sending email: " + e.getMessage());
            response.put("result", null);
        }

        return response;
    }
}