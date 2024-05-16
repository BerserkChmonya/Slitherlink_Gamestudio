package sk.tuke.gamestudio.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "app_user")
@NamedQueries({
        @NamedQuery(name = "User.getUser", query = "SELECT u FROM User u WHERE u.username=:username"),
        @NamedQuery(name = "User.resetUsers", query = "DELETE FROM User")
})
public class User implements Serializable {
    @Id
    private String username;
    private String password;
    public User(String username, String password) {
        this.username = username;
        this.password = hashPassword(password);
    }

    public User() {

    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(hashPassword(password));
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
