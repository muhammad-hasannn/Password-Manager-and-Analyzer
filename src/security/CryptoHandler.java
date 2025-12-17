package security;

import java.util.Base64;

/*
 * Just 2 functions:
 * 1. encrypt(password) - shifts each character by its position
 * 2. decrypt(encrypted) - shifts back
 */
public class CryptoHandler {
    
    /**
     * Encrypts a password.
     * Each character shifts by: (position * 7 + 13)
     * 
     * @param password Password to encrypt
     * @return Encrypted password (Base64)
     */
    public static String encrypt(String password) {
        byte[] passwordBytes = password.getBytes();
        byte[] encrypted = new byte[passwordBytes.length];
        
        // Shift each character by its position
        for (int i = 0; i < passwordBytes.length; i++) {
            int shift = (i * 7 + 13); // Simple formula: position * 7 + 13
            encrypted[i] = (byte) (passwordBytes[i] + shift);
        }
        
        return Base64.getEncoder().encodeToString(encrypted);
    }
    
    /**
     * Decrypts a password.
     * 
     * @param encryptedPassword Encrypted password
     * @return Original password
     */
    public static String decrypt(String encryptedPassword) {
        byte[] encrypted = Base64.getDecoder().decode(encryptedPassword);
        byte[] decrypted = new byte[encrypted.length];
        
        // Shift back each character
        for (int i = 0; i < encrypted.length; i++) {
            int shift = (i * 7 + 13); // Same formula
            decrypted[i] = (byte) (encrypted[i] - shift);
        }
        
        return new String(decrypted);
    }
}