package uni.fmi.service;

public interface PasswordService {
    String hashPassword(String passwordPlaintext);

    boolean checkPassword(String passwordPlaintext, String storedHash);
}
