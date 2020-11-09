package com.dawnfall.caterplanner.story.controller;

import com.dawnfall.caterplanner.application.security.jwt.JwtPayload;
import com.dawnfall.caterplanner.common.model.network.Response;
import com.dawnfall.caterplanner.story.model.request.StoryResource;
import com.dawnfall.caterplanner.story.service.LikesService;
import com.dawnfall.caterplanner.story.service.StoryCommentService;
import com.dawnfall.caterplanner.story.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("story")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private StoryCommentService storyCommentService;

    @PostMapping
    public Response upload(@AuthenticationPrincipal JwtPayload payload, @RequestBody StoryResource resource ){
        return storyService.create(payload.getId(), resource);
    }

    @GetMapping("{id}")
    public Response detail(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return storyService.read(payload.getId(), id);
    }

    @PutMapping("{id}")
    public Response modify(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id,  @RequestBody StoryResource resource){
        return storyService.update(payload.getId(), id, resource);
    }

    @DeleteMapping("{id}")
    public Response delete(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return storyService.delete(payload.getId(), id);
    }

    @PatchMapping("{id}/likes/positive")
    public Response positive(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return likesService.create(payload.getId(), id);
    }

    @PatchMapping("{id}/likes/negative")
    public Response negative(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return likesService.delete(payload.getId(), id);
    }

    @GetMapping("{id}/comments")
    public Response storyComments(
            @AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long storyId,
            @RequestParam("page") Integer page){
        return storyCommentService.readAll(payload.getId(), storyId, PageRequest.of(page, 15, Sort.by("createdDate").descending()));
    }

    @GetMapping
    public Response feed(
            @AuthenticationPrincipal JwtPayload payload,
            @RequestParam(name = "type", required = false) Integer type,
            @RequestParam(name = "page") Integer page){

        return storyService.readAllForFront(payload.getId(), type, PageRequest.of(page, 10, Sort.by("createdDate").descending()));
    }

    //나중에 고민해 봅시다 list에서 전체 데이터를 가져아서하는게 좋은지? (request 1번), 처음에 간단 데이터 후에 세부 데이터로 (2차 데이터)
}
