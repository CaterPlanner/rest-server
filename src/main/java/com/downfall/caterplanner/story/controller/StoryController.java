package com.downfall.caterplanner.story.controller;

import com.downfall.caterplanner.application.security.jwt.JwtPayload;
import com.downfall.caterplanner.common.model.network.ResponseHeader;
import com.downfall.caterplanner.story.model.request.StoryResource;
import com.downfall.caterplanner.story.model.response.ResponseStory;
import com.downfall.caterplanner.story.service.LikesService;
import com.downfall.caterplanner.story.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("story")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @Autowired
    private LikesService likesService;

    @PostMapping
    public ResponseHeader<?> create(@AuthenticationPrincipal JwtPayload payload, @RequestBody StoryResource resource){
        return ResponseHeader.<ResponseStory>builder()
                    .data(storyService.create(payload.getId(), resource))
                    .status(HttpStatus.CREATED)
                    .build();
    }

    @GetMapping("{id}")
    public ResponseHeader<?> read(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return ResponseHeader.<ResponseStory>builder()
                    .data(storyService.read(payload.getId(), id))
                    .status(HttpStatus.OK)
                    .build();
    }

    @PutMapping("{id}")
    public ResponseHeader<?> modify(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id,  @RequestBody StoryResource resource){
        storyService.update(payload.getId(), resource);
        return ResponseHeader.builder()
                    .status(HttpStatus.OK)
                    .build();
    }

    @DeleteMapping("{id}")
    public ResponseHeader<?> delete(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        storyService.delete(payload.getId(), id);
        return ResponseHeader.builder()
                    .status(HttpStatus.OK)
                    .build();
    }

    @PostMapping("{id}/likes/positive")
    public ResponseHeader<?> addCheer(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        likesService.create(payload.getId(), id);
        return ResponseHeader.builder()
                .status(HttpStatus.OK)
                .message("응원되었습니다.")
                .build();
    }

    @PostMapping("{id}/likes/negative")
    public ResponseHeader<?> removeCheer(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        likesService.delete(payload.getId(), id);
        return ResponseHeader.builder().status(HttpStatus.OK)
                .message("응원이 해제되었습니다.")
                .build();
    }



}
