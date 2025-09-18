package project.payroll_backend_java.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.sql.DataSource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;

@Component
public class DatabaseHealthInterceptor implements HandlerInterceptor {

    @Autowired
    private DataSource dataSource;

    private long lastChecked = 0;
    private boolean isDbConnected = false ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long now = System.currentTimeMillis();

        // Only re-check the database every 30 seconds
        if (lastChecked == 0 || now - lastChecked > 30000) {
            try (Connection connection = dataSource.getConnection()) {
                isDbConnected = connection.isValid(5); // 5 second timeout
                System.out.println("Database connection is healthy.");
            } catch (Exception e) {
                isDbConnected = false;
                System.err.println("Database connection failed: " + e.getMessage());
            }
            lastChecked = now;
        }

        if (!isDbConnected) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Database connection failed.\"}");
            return false;
        }

        return true;
    }
}
