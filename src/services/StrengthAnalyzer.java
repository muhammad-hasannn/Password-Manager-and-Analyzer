package services;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StrengthAnalyzer {
    
    // Password requirements constants
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;
    private static final int RECOMMENDED_LENGTH = 12;
    
    /*
     * Analyzes password strength and returns a list of weaknesses.
     * If the list is empty, the password is strong and meets all requirements.
     * 
     * @param password The password to analyze
     * @return List of weakness messages. Empty list means password is strong.
     */
    public List<String> analyzePassword(String password) {
        List<String> weaknesses = new ArrayList<>();
        
        // Check if password is null or empty
        if (password == null || password.isEmpty()) {
            weaknesses.add("Password cannot be empty");
            return weaknesses;
        }
        
        // Check minimum length
        if (password.length() < MIN_LENGTH) {
            weaknesses.add("Password must be at least " + MIN_LENGTH + " characters long");
        }
        
        // Check maximum length
        if (password.length() > MAX_LENGTH) {
            weaknesses.add("Password must not exceed " + MAX_LENGTH + " characters");
        }
        
        // Check for uppercase letters
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            weaknesses.add("Password must contain at least one uppercase letter (A-Z)");
        }
        
        // Check for lowercase letters
        if (!Pattern.compile("[a-z]").matcher(password).find()) {
            weaknesses.add("Password must contain at least one lowercase letter (a-z)");
        }
        
        // Check for digits
        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            weaknesses.add("Password must contain at least one digit (0-9)");
        }
        
        // Check for special characters
        if (!Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]").matcher(password).find()) {
            weaknesses.add("Password must contain at least one special character (!@#$%^&*()_+-=[]{}|;:',.<>?/)");
        }
        
        // Check for common patterns
        if (containsCommonPatterns(password)) {
            weaknesses.add("Password contains common patterns (e.g., '123', 'abc', 'qwerty')");
        }
        
        // Check for repeated characters
        if (hasExcessiveRepeatedCharacters(password)) {
            weaknesses.add("Password contains too many repeated characters");
        }
        
        // Check for sequential characters
        if (hasSequentialCharacters(password)) {
            weaknesses.add("Password contains sequential characters (e.g., 'abcd', '1234')");
        }
        
        // Recommend longer password for better security
        if (password.length() < RECOMMENDED_LENGTH && weaknesses.isEmpty()) {
            weaknesses.add("Password is acceptable but recommended length is " + RECOMMENDED_LENGTH + "+ characters for better security");
        }
        
        return weaknesses;
    }
    
    /*
     * Checks if password contains common weak patterns
     */
    private boolean containsCommonPatterns(String password) {
        String lowerPassword = password.toLowerCase();
        String[] commonPatterns = {
            "password", "123456", "qwerty", "abc123", "letmein",
            "welcome", "monkey", "dragon", "master", "sunshine"
        };
        
        for (String pattern : commonPatterns) {
            if (lowerPassword.contains(pattern)) {
                return true;
            }
        }
        return false;
    }
    
    /*
     * Checks if password has more than 3 consecutive repeated characters
     */
    private boolean hasExcessiveRepeatedCharacters(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1) && 
                password.charAt(i) == password.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }
    
    /*
     * Checks if password contains sequential characters (3+ in a row)
     */
    private boolean hasSequentialCharacters(String password) {
        String lowerPassword = password.toLowerCase();
        
        for (int i = 0; i < lowerPassword.length() - 2; i++) {
            char first = lowerPassword.charAt(i);
            char second = lowerPassword.charAt(i + 1);
            char third = lowerPassword.charAt(i + 2);
            
            // Check if characters are sequential (ascending or descending)
            if ((second == first + 1 && third == second + 1) || 
                (second == first - 1 && third == second - 1)) {
                return true;
            }
        }
        return false;
    }
}  
