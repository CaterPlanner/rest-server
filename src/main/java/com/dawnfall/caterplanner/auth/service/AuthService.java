package com.dawnfall.caterplanner.auth.service;

import com.dawnfall.caterplanner.application.exception.HttpRequestException;
import com.dawnfall.caterplanner.application.security.jwt.JwtFactory;
import com.dawnfall.caterplanner.application.security.jwt.JwtPayload;
import com.dawnfall.caterplanner.application.security.jwt.JwtVerifier;
import com.dawnfall.caterplanner.common.ErrorCode;
import com.dawnfall.caterplanner.common.entity.User;
import com.dawnfall.caterplanner.common.model.network.ErrorInfo;
import com.dawnfall.caterplanner.common.model.network.Response;
import com.dawnfall.caterplanner.common.repository.UserRepository;
import com.dawnfall.caterplanner.auth.model.RequestRefreshToken;
import com.dawnfall.caterplanner.auth.model.UserToken;
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


    public Response signIn(String email, String name, String pictureUrl, String fcmToken) {
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

        return new Response("로그인/토큰 발급 성공", getUserToken(user));
    }

    public Response tokenRefresh(RequestRefreshToken requestRefreshToken){
        JwtPayload payload = jwtVerifier.decode(requestRefreshToken.getRefreshToken());

        User user = userRepository.findById(payload.getId()).orElseThrow(
                () -> new HttpRequestException("토큰 재발급 실패", new ErrorInfo(ErrorCode.NOT_EXISTED,"토큰의 사용자는 존재하지 않은 정보입니다.")));

        if(!user.getRefreshToken().equals(requestRefreshToken.getRefreshToken()))
            throw new HttpRequestException("토큰 재발급 실패", new ErrorInfo(ErrorCode.UNAUTHORIZED, "허용되지 않은 재발급 인증 토큰 입니다."));

        return new Response("토큰 재발급 성공", getUserToken(user));
    }

    public Response logout(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new HttpRequestException("로그아웃 실패", new ErrorInfo(ErrorCode.NOT_EXISTED, "존재하지 않은 사용자입니다.")));
        user.setRefreshToken(null);
        userRepository.save(user);
        return new Response("로그아웃 성공");
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
