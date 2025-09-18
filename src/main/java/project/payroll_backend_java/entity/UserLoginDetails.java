package project.payroll_backend_java.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_login_details")
public class UserLoginDetails {

    @Id
    @Column(name = "user_name", length = 50, unique = true)
    private String userName;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    // Constructors
    public UserLoginDetails() {}

    public UserLoginDetails(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    // Getters and Setters
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }
}
