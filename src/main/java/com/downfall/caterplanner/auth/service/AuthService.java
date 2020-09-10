package com.downfall.caterplanner.auth.service;

import com.downfall.caterplanner.application.exception.HttpRequestException;
import com.downfall.caterplanner.application.security.jwt.JwtFactory;
import com.downfall.caterplanner.application.security.jwt.JwtPayload;
import com.downfall.caterplanner.application.security.jwt.JwtVerifier;
import com.downfall.caterplanner.auth.model.RequestRefreshToken;
import com.downfall.caterplanner.auth.model.UserToken;
import com.downfall.caterplanner.common.entity.User;
import com.downfall.caterplanner.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtFactory jwtFactory;

    @Autowired
    private JwtVerifier jwtVerifier;


    private final long EXPIRED_TIME = 1000 * 60 * 60 * 2;

    public UserToken signIn(String email, String name, String pictureUrl, String fcmToken) {
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            return userRepository.save(
                    User.builder()
                            .email(email)
                            .name(name)
                            .profileUrl(pictureUrl)
                            .fcmToken(fcmToken)
                            .build()
            );
        });

        return getUserToken(user);
    }

    public UserToken tokenRefresh(RequestRefreshToken requestRefreshToken){
        JwtPayload payload = jwtVerifier.decode(requestRefreshToken.getToken());

        User user = userRepository.findById(payload.getId()).orElseThrow(() -> new HttpRequestException("토큰의 사용자는 존재하지 않은 정보입니다.", HttpStatus.BAD_REQUEST));

        if(!user.getRefreshToken().equals(requestRefreshToken.getRefreshToken()))
            throw new HttpRequestException("허용되지 않은 refresh token 입니다.", HttpStatus.UNAUTHORIZED);

        return getUserToken(user);
    }

    public void logout(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new HttpRequestException("존재하지 않은 사용자입니다.", HttpStatus.BAD_REQUEST));
        user.setRefreshToken(null);
        userRepository.save(user);
    }

    private UserToken getUserToken(User user) {
        String refreshToken = createRefreshToken(10);
        user.setRefreshToken(refreshToken);

        userRepository.save(user);

        Date nextExpried = createNextExpired();


        return UserToken.builder()
                .token(jwtFactory.createToken(JwtPayload.builder()
                        .id(user.getId())
                        .expired(nextExpried).build()))
                .refreshToken(user.getRefreshToken())
                .expired(nextExpried.getTime())
                .build();
    }

    private String createRefreshToken(int length){
        final String token = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/!#";
        char[] text = new char[length];

        for(int i = 0; i < text.length; i++){
            text[i] = token.charAt((int) (Math.random() * token.length()));
        }
        return new String(text);
    }

    private Date createNextExpired(){
        return new Date(new Date().getTime() + EXPIRED_TIME);
    }

}
