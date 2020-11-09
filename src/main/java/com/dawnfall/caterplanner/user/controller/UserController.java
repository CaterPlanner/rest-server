package com.dawnfall.caterplanner.user.controller;

import com.dawnfall.caterplanner.application.security.jwt.JwtPayload;
import com.dawnfall.caterplanner.common.model.network.Response;
import com.dawnfall.caterplanner.user.model.request.UserResource;
import com.dawnfall.caterplanner.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    public Response read(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return userService.read(payload.getId(), id);
    }

    @GetMapping("{id}/purposes")
    public Response readPurposes(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return userService.getUserPurposes(id);
    }

    @GetMapping("myProfile")
    public Response readMyProfile(@AuthenticationPrincipal JwtPayload payload){
        return userService.read(payload.getId(), payload.getId());
    }

    @PutMapping("myProfile")
    public Response updateMyProfile(@AuthenticationPrincipal JwtPayload payload, @RequestBody UserResource resource){
        return userService.update(payload.getId(), resource);
    }

    @GetMapping("myActivePurposes")
    public Response myActivePurposes(@AuthenticationPrincipal JwtPayload payload){
        return userService.getActivePurposes(payload.getId());
    }


}
