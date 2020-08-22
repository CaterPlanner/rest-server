package com.downfall.caterplanner.user.controller;

import com.downfall.caterplanner.application.security.jwt.JwtPayload;
import com.downfall.caterplanner.common.model.network.ResponseHeader;
import com.downfall.caterplanner.user.model.response.ResponseUser;
import com.downfall.caterplanner.user.model.request.UserResource;
import com.downfall.caterplanner.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("myProfile")
    public ResponseHeader<?> readMyProfile(@AuthenticationPrincipal JwtPayload payload){

        return ResponseHeader.<ResponseUser>builder()
                .data(userService.read(payload.getId(), true))
                .status(HttpStatus.OK)
                .build();
    }

    @PutMapping
    public ResponseHeader<?> updateMyProfile(@AuthenticationPrincipal JwtPayload payload, @RequestBody UserResource resource){
        return ResponseHeader.<ResponseUser>builder()
                .data(userService.update(payload.getId(), resource))
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("{id}")
    public ResponseHeader<?> read(@PathVariable("id") long id){
        return ResponseHeader.<ResponseUser>builder()
                .data(userService.read(id, false))
                .status(HttpStatus.OK)
                .build();
    }

}
