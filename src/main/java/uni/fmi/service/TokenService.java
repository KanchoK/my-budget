package uni.fmi.service;

import uni.fmi.model.User;

public interface TokenService {
    String generateTokenForUser(User user);

    boolean isTokenValid(String token);
}