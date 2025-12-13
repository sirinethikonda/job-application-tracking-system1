package com.example.demo.auth;

import com.example.demo.company.Company;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Collection;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity(name = "AuthUser")
@Table(name = "auth_users")
@Data
@NoArgsConstructor
// Implementing UserDetails ensures Spring Security knows how to load and authorize this user.
public class AuthUser implements UserDetails { 

    public enum Role { CANDIDATE, RECRUITER, HIRING_MANAGER }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password; // This field stores the hashed password
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company; 
    
    // --- UserDetails Interface Implementations ---
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role.name()));
    }
    
    // Resolves "The method getUsername() is undefined for the type User" error
    @Override 
    public String getUsername() {
        return username; 
    }

    @Override
    public String getPassword() {
        return password;
    }
    
    // Ensure all these methods return true for simple project logic
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

	public static Object builder() {
		// TODO Auto-generated method stub
		return null;
	}
}