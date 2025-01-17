package com.skyler.catalogo.infra.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.skyler.catalogo.infra.user.User;
import com.skyler.catalogo.infra.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class JwtService {

    private final UserRepository userRepository;

    private final String secret = "E9FJ2J093W240JF9G342IT-EWP34W2E";

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String signToken(String username){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(username)
                    .withExpiresAt(LocalDateTime.now().plusYears(1).toInstant(ZoneOffset.of("-03:00")))
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token)
                .getSubject();
    }

    public String getUsernameFromJWT(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret); // Use the same secret used for signing
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth-api")  // Certifique-se de que o emissor seja o mesmo usado na criação do token
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();  // O username foi salvo como "subject" no token
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error while verifying token", exception);
        }
    }

    public User getUserFromServelet(HttpServletRequest request){
        Optional<User> userOPT = userRepository.findByUsername(this.getUsernameFromJWT(this.getTokenFromServelet(request)));
        if(userOPT.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        return userOPT.get();
    }

    public String getTokenFromServelet(HttpServletRequest request){
        return request.getHeader("token");
    }
}
