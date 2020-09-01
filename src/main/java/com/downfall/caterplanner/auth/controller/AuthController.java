package com.downfall.caterplanner.auth.controller;


import com.downfall.caterplanner.application.exception.HttpRequestException;
import com.downfall.caterplanner.application.security.jwt.JwtPayload;
import com.downfall.caterplanner.auth.model.RequestAuthToken;
import com.downfall.caterplanner.auth.model.RequestRefreshToken;
import com.downfall.caterplanner.auth.model.UserToken;
import com.downfall.caterplanner.auth.service.AuthService;
import com.downfall.caterplanner.common.model.network.ResponseHeader;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("social/google")
    public ResponseHeader<?> authGoogle(@RequestBody @Valid RequestAuthToken resource){


        try {
            final String CLIENT_ID = "824557913187-k8onrhv3nt0q5cbfvc2hf6gamspjseo1.apps.googleusercontent.com";


            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(resource.getIdToken());
            
            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");

            System.out.println("google 로그인 요청 들어옴");

            return ResponseHeader.<UserToken>builder()
                    .data(authService.signIn(email, name, pictureUrl, resource.getFcmToken()))
                    .message("구글 토큰 인증 완료")
                    .status(HttpStatus.CREATED)
                    .build();

        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            throw new HttpRequestException("구글 토큰 인증에 실패하였습니다." , HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("refreshToken")
    public ResponseHeader<?> refreshToken(@RequestBody @Valid RequestRefreshToken resource){
        System.out.println("refreshToken 요청 들어옴");
        return ResponseHeader.<UserToken>builder()
                .data(authService.tokenRefresh(resource))
                .status(HttpStatus.CREATED)
                .message("token refresh 성공")
                .build();
    }

    @GetMapping("logout")
    public ResponseHeader<?> logout(@AuthenticationPrincipal JwtPayload payload){
        authService.logout(payload.getId());
        System.out.println("로그아웃 요청 들어옴");
        return ResponseHeader.builder()
                    .status(HttpStatus.OK)
                    .message("로그아웃 성공")
                    .build();
    }




}
