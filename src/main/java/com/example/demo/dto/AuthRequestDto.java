package com.example.demo.dto;

public class AuthRequestDto {
    private String username;
    private String password;
    private String email;
    private String role;      // string role to match User.role ("CANDIDATE", "RECRUITER", ...)
    private Long companyId;
    private String fullName;

    public AuthRequestDto() {}

    public AuthRequestDto(String username, String password, String email, String role, Long companyId, String fullName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.companyId = companyId;
        this.fullName = fullName;
    }

    // getters & setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    @Override
    public String toString() {
        return "AuthRequestDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", companyId=" + companyId +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
