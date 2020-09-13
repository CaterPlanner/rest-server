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


    private final long ACCESS_TOKEN_EXPIRED_TIME = 1000 * 60 * 60 * 2;
    private final long REFRESH_TOKEN_EXPIRED_TIME = 1000 * 60 * 60 * 24 * 7;


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
        JwtPayload payload = jwtVerifier.decode(requestRefreshToken.getRefreshToken());

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

        Date nextAccessNextExpired = getNextExpiredTime(ACCESS_TOKEN_EXPIRED_TIME);

        String accessToken = jwtFactory.createToken(
                JwtPayload.builder().id(user.getId()).expired(nextAccessNextExpired).build()
        );

        String refreshToken = jwtFactory.createToken(
                JwtPayload.builder().id(user.getId()).expired(getNextExpiredTime(REFRESH_TOKEN_EXPIRED_TIME)).build()
        );

        user.setRefreshToken(refreshToken);

        userRepository.save(user);


        return UserToken.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .expired(nextAccessNextExpired.getTime())
                .build();
    }

    private Date getNextExpiredTime(long time){
        return new Date(new Date().getTime() + time);
    }

}
