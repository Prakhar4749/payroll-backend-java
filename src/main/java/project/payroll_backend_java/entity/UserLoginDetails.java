package project.payroll_backend_java.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "user_login_details",
        indexes = {
                @Index(name = "idx_username", columnList = "user_name", unique = true)
        }
)

@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDetails {

    @Id
    @Column(name = "user_name", length = 50, nullable = false, unique = true)
    private String user_name;

    @Column(name = "user_password", nullable = false)
    private String user_password;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }
}
