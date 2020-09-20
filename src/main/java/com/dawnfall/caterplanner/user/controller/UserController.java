package com.dawnfall.caterplanner.user.controller;

import com.dawnfall.caterplanner.application.security.jwt.JwtPayload;
import com.dawnfall.caterplanner.common.model.network.ResponseHeader;
import com.dawnfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.dawnfall.caterplanner.user.model.response.ResponseUser;
import com.dawnfall.caterplanner.user.model.request.UserResource;
import com.dawnfall.caterplanner.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<?> read(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return ResponseHeader.<ResponseUser>builder()
                .data(userService.read(payload.getId(), id))
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("{id}/purposes")
    public ResponseEntity<?> readPurposes(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return ResponseHeader.<List<ResponsePurpose>>builder()
                .data(userService.getUserPurposes(id))
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("myProfile")
    public ResponseEntity<?> readMyProfile(@AuthenticationPrincipal JwtPayload payload){

        return ResponseHeader.<ResponseUser>builder()
                .data(userService.read(payload.getId(), payload.getId()))
                .status(HttpStatus.OK)
                .build();
    }

    @PutMapping("myProfile")
    public ResponseEntity<?> updateMyProfile(@AuthenticationPrincipal JwtPayload payload, @RequestBody UserResource resource){
        return ResponseHeader.<ResponseUser>builder()
                .data(userService.update(payload.getId(), resource))
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("myActivePurposes")
    public ResponseEntity<?> myActivePurposes(@AuthenticationPrincipal JwtPayload payload){
        return ResponseHeader.<List<ResponsePurpose>>builder()
                    .data(userService.getActivePurposes(payload.getId()))
                    .status(HttpStatus.OK)
                    .build();
    }


}
