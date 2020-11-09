package com.dawnfall.caterplanner.story.controller;

import com.dawnfall.caterplanner.application.security.jwt.JwtPayload;
import com.dawnfall.caterplanner.common.model.network.Response;
import com.dawnfall.caterplanner.story.model.request.StoryCommentResource;
import com.dawnfall.caterplanner.story.service.StoryCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("storyComment")
public class StoryCommentController {

    @Autowired
    private StoryCommentService storyCommentService;

    @PostMapping
    public Response addComment(@AuthenticationPrincipal JwtPayload payload, @RequestBody StoryCommentResource resource){
        return storyCommentService.create(payload.getId(), resource);
    }

    @DeleteMapping("{id}")
    public Response removeComment(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return storyCommentService.delete(payload.getId(), id);
    }

    @PutMapping("{id}")
    public Response modifyComment(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id, @RequestBody StoryCommentResource resource){
        return storyCommentService.update(payload.getId(), id, resource);
    }

}
