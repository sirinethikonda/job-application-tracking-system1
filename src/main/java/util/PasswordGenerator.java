package util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {
        // Instantiate the standard BCrypt encoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // --- 1. Define the plain text password ---
        String plainPassword = "test1234"; 

        // --- 2. Generate the hash ---
        String hashedPassword = encoder.encode(plainPassword);

        // --- 3. Print the result ---
        System.out.println("------------------------------------------");
        System.out.println("Original Password: " + plainPassword);
        System.out.println("BCrypt Hash: " + hashedPassword);
        System.out.println("------------------------------------------");
    }
}