package com.downfall.caterplanner.story.controller;

import com.downfall.caterplanner.application.security.jwt.JwtPayload;
import com.downfall.caterplanner.common.model.network.PageResult;
import com.downfall.caterplanner.common.model.network.ResponseHeader;
import com.downfall.caterplanner.story.model.request.StoryResource;
import com.downfall.caterplanner.story.model.response.ResponseStory;
import com.downfall.caterplanner.story.service.LikesService;
import com.downfall.caterplanner.story.service.StoryCommentService;
import com.downfall.caterplanner.story.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseHeader<?> upload(@AuthenticationPrincipal JwtPayload payload, @RequestBody StoryResource resource){
        return ResponseHeader.<ResponseStory>builder()
                    .data(storyService.create(payload.getId(), resource))
                    .message("스토리 업로드 완료")
                    .status(HttpStatus.CREATED)
                    .build();
    }

    @GetMapping("{id}")
    public ResponseHeader<?> detail(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return ResponseHeader.<ResponseStory>builder()
                    .data(storyService.read(payload.getId(), id))
                    .message("세부 스토리 로드 완료")
                    .status(HttpStatus.OK)
                    .build();
    }

    @PutMapping("{id}")
    public ResponseHeader<?> modify(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id,  @RequestBody StoryResource resource){
        storyService.update(payload.getId(), resource);
        return ResponseHeader.builder()
                    .message("스토리 변경 완료")
                    .status(HttpStatus.OK)
                    .build();
    }

    @DeleteMapping("{id}")
    public ResponseHeader<?> delete(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        storyService.delete(payload.getId(), id);
        return ResponseHeader.builder()
                    .message("스토리 삭제 완료")
                    .status(HttpStatus.OK)
                    .build();
    }

    @PatchMapping("{id}/likes/positive")
    public ResponseHeader<?> positive(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        likesService.create(payload.getId(), id);
        return ResponseHeader.builder()
                .status(HttpStatus.OK)
                .message("좋아요 되었습니다.")
                .build();
    }

    @PatchMapping("{id}/likes/negative")
    public ResponseHeader<?> negative(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        likesService.delete(payload.getId(), id);
        return ResponseHeader.builder().status(HttpStatus.OK)
                .message("좋아요가 해제되었습니다.")
                .build();
    }

    @GetMapping("{id}/comment")
    public ResponseHeader<?> storyComments(
            @AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long storyId,
            @RequestParam("page") Integer page){
        return ResponseHeader.builder()
                .status(HttpStatus.OK)
                .data(storyCommentService.readAll(storyId, page, PageRequest.of(page, 20)))
                .message("댓글이 로드되었습니다.")
                .build();
    }

    @GetMapping
    public ResponseHeader<?> listFront(
            @AuthenticationPrincipal JwtPayload payload,
            @RequestParam(name = "type", required = false) Integer type,
            @RequestParam(name = "page") Integer page){

        return ResponseHeader.<PageResult<?>>builder()
                .status(HttpStatus.OK)
                .message("스토리(앞단)들 로드 완료")
                .data(storyService.readAllForFront(payload.getId(), type, PageRequest.of(page, 10)))
                .build();
    }

    //나중에 고민해 봅시다 list에서 전체 데이터를 가져아서하는게 좋은지? (request 1번), 처음에 간단 데이터 후에 세부 데이터로 (2차 데이터)
}
