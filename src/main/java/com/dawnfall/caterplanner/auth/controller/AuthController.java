package com.dawnfall.caterplanner.auth.controller;


import com.dawnfall.caterplanner.application.exception.HttpRequestException;
import com.dawnfall.caterplanner.application.security.jwt.JwtPayload;
import com.dawnfall.caterplanner.common.ErrorCode;
import com.dawnfall.caterplanner.common.model.network.ErrorInfo;
import com.dawnfall.caterplanner.common.model.network.Response;
import com.dawnfall.caterplanner.auth.model.RequestAuthToken;
import com.dawnfall.caterplanner.auth.model.RequestRefreshToken;
import com.dawnfall.caterplanner.auth.service.AuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Response authGoogle(@RequestBody @Valid RequestAuthToken resource){

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

            return authService.signIn(email, name, pictureUrl, resource.getFcmToken());

        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            throw new HttpRequestException("구글 로그인 실패", new ErrorInfo(ErrorCode.FAILED_SOCIAL_LOGIN, "유효하지 않은 구글 토큰 입니다."));
        }

    }

    @PostMapping("refreshToken")
    public Response refreshToken(@RequestBody @Valid RequestRefreshToken resource){
        return authService.tokenRefresh(resource);
    }

    @GetMapping("logout")
    public Response logout(@AuthenticationPrincipal JwtPayload payload){
        return authService.logout(payload.getId());
    }




}
