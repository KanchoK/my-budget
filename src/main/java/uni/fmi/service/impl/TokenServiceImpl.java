package uni.fmi.service.impl;

import uni.fmi.model.User;
import uni.fmi.service.TokenService;
import io.jsonwebtoken.*;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import java.util.Calendar;
import java.util.Date;

public class TokenServiceImpl implements TokenService {
    private static final Logger LOG = Logger.getLogger(TokenServiceImpl.class);

    @Context
    private Configuration config;

    @Override
    public String generateTokenForUser(User user) {
        String secretKey = (String) config.getProperty("token.secret.key");

        if (secretKey == null || secretKey.isEmpty()) {
            LOG.warn("Problem with reading token.secret.key property");
            return null;
        }

        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.HOUR, 1);

        Claims claims = Jwts.claims()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(currentDate)
                .setExpiration(c.getTime());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        String secretKey = (String) config.getProperty("token.secret.key");

        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException e) {
            LOG.error("Exception was thrown", e);
            return false;
        }
    }
}
