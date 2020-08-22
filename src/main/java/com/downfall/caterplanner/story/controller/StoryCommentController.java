package com.downfall.caterplanner.story.controller;

import com.downfall.caterplanner.application.security.jwt.JwtPayload;
import com.downfall.caterplanner.common.model.network.ResponseHeader;
import com.downfall.caterplanner.story.model.request.StoryCommentResource;
import com.downfall.caterplanner.story.service.StoryCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("storyComment")
public class StoryCommentController {

    @Autowired
    private StoryCommentService storyCommentService;

    @PostMapping
    public ResponseHeader<?> addComment(@AuthenticationPrincipal JwtPayload payload, @RequestBody StoryCommentResource resource){
        storyCommentService.create(payload.getId(), resource);
        return ResponseHeader.builder()
                .status(HttpStatus.OK)
                .message("댓글 생성 완료")
                .build();
    }

    @DeleteMapping("{id}")
    public ResponseHeader<?> removeComment(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        storyCommentService.delete(payload.getId(), id);
        return ResponseHeader.builder()
                .status(HttpStatus.OK)
                .message("댓글 삭제 완료")
                .build();
    }

    @PutMapping("{id}")
    public ResponseHeader<?> modifyComment(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id, @RequestBody StoryCommentResource resource){
        storyCommentService.update(payload.getId(), id, resource);
        return ResponseHeader.builder()
                .status(HttpStatus.OK)
                .message("댓글 수정 완료")
                .build();
    }

}
