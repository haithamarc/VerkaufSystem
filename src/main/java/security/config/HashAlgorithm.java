package security.config;

import jakarta.enterprise.context.Dependent;
import jakarta.security.enterprise.identitystore.PasswordHash;

/**
 * A class implementing the PasswordHash interface to hash passwords.
 * This implementation does not provide a secure hashing algorithm.
 * It is recommended to use a secure algorithm like BCrypt or Argon2 in production environments.
 */
@Dependent
public class HashAlgorithm implements PasswordHash {

    /**
     * Generates a hashed version of the given password.
     * This method currently returns the password as is, without hashing.
     *
     * @param password The password to be hashed.
     * @return The hashed password.
     */
    @Override
    public String generate(char[] password) {
        return new String(password);
    }

    /**
     * Verifies if a given password matches the hashed password.
     * This method removes any digit groups enclosed in parentheses and any whitespace characters from the hashed password
     * before comparing it with the provided password.
     *
     * @param password The password to be verified.
     * @param hashedPassword The hashed password to be compared with.
     * @return true if the password matches the hashed password, false otherwise.
     */
    @Override
    public boolean verify(char[] password, String hashedPassword) {
        return String.valueOf(password).equals(hashedPassword.replaceAll( "(\\()(\\p{Digit}*(\\)))|\\s+",""));
    }
}
