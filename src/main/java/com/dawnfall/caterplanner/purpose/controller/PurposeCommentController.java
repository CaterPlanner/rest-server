package com.dawnfall.caterplanner.purpose.controller;

import com.dawnfall.caterplanner.application.security.jwt.JwtPayload;
import com.dawnfall.caterplanner.common.model.network.Response;
import com.dawnfall.caterplanner.purpose.model.request.PurposeCommentResource;
import com.dawnfall.caterplanner.purpose.service.PurposeCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("purposeComment")
public class PurposeCommentController {

    @Autowired
    private PurposeCommentService purposeCommentService;

    @PostMapping
    public Response addComment(@AuthenticationPrincipal JwtPayload payload, @RequestBody PurposeCommentResource resource){
        return purposeCommentService.create(payload.getId(), resource);
    }

    @DeleteMapping("{id}")
    public Response removeComment(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return purposeCommentService.delete(payload.getId(), id);
    }

    @PutMapping("{id}")
    public Response modifyComment(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id, @RequestBody PurposeCommentResource resource){
        return purposeCommentService.update(payload.getId(), id, resource);
    }
}
