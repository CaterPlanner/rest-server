package com.dawnfall.caterplanner.auth.controller;


import com.dawnfall.caterplanner.application.exception.HttpRequestException;
import com.dawnfall.caterplanner.application.security.jwt.JwtPayload;
import com.dawnfall.caterplanner.common.model.network.ResponseHeader;
import com.dawnfall.caterplanner.auth.model.RequestAuthToken;
import com.dawnfall.caterplanner.auth.model.RequestRefreshToken;
import com.dawnfall.caterplanner.auth.model.UserToken;
import com.dawnfall.caterplanner.auth.service.AuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> authGoogle(@RequestBody @Valid RequestAuthToken resource){


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
    public ResponseEntity<?> refreshToken(@RequestBody @Valid RequestRefreshToken resource){
        System.out.println("refreshToken 요청 들어옴");
        return ResponseHeader.<UserToken>builder()
                .data(authService.tokenRefresh(resource))
                .status(HttpStatus.CREATED)
                .message("token refresh 성공")
                .build();
    }

    @GetMapping("logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal JwtPayload payload){
        authService.logout(payload.getId());
        return ResponseHeader.builder()
                    .status(HttpStatus.OK)
                    .message("로그아웃 성공")
                    .build();
    }




}