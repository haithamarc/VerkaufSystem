package config;

import jakarta.security.enterprise.identitystore.PasswordHash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class HashAlgorithm implements PasswordHash {

    HashAlgorithm (){

    }
    /**
     Generates a password hash using the SHA-256 algorithm.
     @param chars The password characters to be hashed.
     @return The hashed password as a Base64-encoded string.
     */
    @Override
    public String generate(char[] chars) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(String.valueOf(chars).getBytes(StandardCharsets.UTF_8));
            // Generate a hash of the password
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) { // If the SHA-256 algorithm is not available, throw an exception
            throw new RuntimeException("Error generating password hash", e);
        }
    }

    /**
     * Verifies the given password against the given hash.
     * @param password the password to verify
     * @param hashedPassword the hashed password to compare against
     * @return true if the password matches the hashed password, false otherwise
     */
    @Override
    public boolean verify(char[] password, String hashedPassword) {
        try {
            byte[] hashBytes = Base64.getDecoder().decode(hashedPassword);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] passwordBytes = String.valueOf(password).getBytes(StandardCharsets.UTF_8);
            byte[] generatedHashBytes = md.digest(passwordBytes);
            return Arrays.equals(hashBytes, generatedHashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error verifying password hash", e);
        }
    }
}