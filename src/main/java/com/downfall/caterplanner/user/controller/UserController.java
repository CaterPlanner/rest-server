package com.downfall.caterplanner.user.controller;

import com.downfall.caterplanner.application.security.jwt.JwtPayload;
import com.downfall.caterplanner.common.model.network.ResponseHeader;
import com.downfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.downfall.caterplanner.user.model.response.ResponseUser;
import com.downfall.caterplanner.user.model.request.UserResource;
import com.downfall.caterplanner.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    public ResponseHeader<?> read(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return ResponseHeader.<ResponseUser>builder()
                .data(userService.read(payload.getId(), id))
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("myProfile")
    public ResponseHeader<?> readMyProfile(@AuthenticationPrincipal JwtPayload payload){

        return ResponseHeader.<ResponseUser>builder()
                .data(userService.read(payload.getId(), payload.getId()))
                .status(HttpStatus.OK)
                .build();
    }

    @PutMapping("myProfile")
    public ResponseHeader<?> updateMyProfile(@AuthenticationPrincipal JwtPayload payload, @RequestBody UserResource resource){
        return ResponseHeader.<ResponseUser>builder()
                .data(userService.update(payload.getId(), resource))
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("myPurposes")
    public ResponseHeader<?> myPurposes(@AuthenticationPrincipal JwtPayload payload){
        return ResponseHeader.<List<ResponsePurpose>>builder()
                    .data(userService.getHasPurposes(payload.getId()))
                    .status(HttpStatus.OK)
                    .build();
    }


}
