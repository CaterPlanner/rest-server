package com.downfall.caterplanner.purpose.controller;

import com.downfall.caterplanner.application.security.jwt.JwtPayload;
import com.downfall.caterplanner.common.model.network.PageResult;
import com.downfall.caterplanner.common.model.network.ResponseHeader;
import com.downfall.caterplanner.purpose.model.request.PurposeAchieve;
import com.downfall.caterplanner.purpose.model.request.PurposeResource;
import com.downfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.downfall.caterplanner.purpose.service.CheerService;
import com.downfall.caterplanner.purpose.service.PurposeCommentService;
import com.downfall.caterplanner.purpose.service.PurposeService;
import com.downfall.caterplanner.story.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("purpose")
public class PurposeController {

    @Autowired
    private PurposeCommentService purposeCommentService;

    @Autowired
    private PurposeService purposeService;

    @Autowired
    private CheerService cheerService;

    @Autowired
    private StoryService storyService;


    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> upload(HttpServletRequest request, @AuthenticationPrincipal JwtPayload payload, @ModelAttribute PurposeResource resource){
        try {
            return ResponseHeader.<ResponsePurpose>builder()
                        .data(purposeService.create(payload.getId(), resource))
                        .status(HttpStatus.CREATED)
                        .message("목적 업로드 완료")
                        .build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseHeader.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message("파일 스토로지 오류")
                        .build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<?> detail(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return ResponseHeader.<ResponsePurpose>builder()
                    .data(purposeService.read(payload.getId(), id))
                    .status(HttpStatus.OK)
                    .message("세부 목적 로딩 완료")
                    .build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<?> modify(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id, @Valid @ModelAttribute  PurposeResource resource){
        try {
            return ResponseHeader.builder()
                    .data(purposeService.modify(payload.getId(), id, resource))
                    .status(HttpStatus.OK)
                    .message("목적 수정 완료")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseHeader.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("파일 스토로지 오류")
                    .build();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> modifyAll(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id, @Valid @ModelAttribute PurposeResource resource){
        try {
            return ResponseHeader.builder()
                        .data(purposeService.modifyAll(payload.getId(), id, resource))
                        .status(HttpStatus.OK)
                        .message("목적 수정 완료")
                        .build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseHeader.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("파일 스토로지 오류")
                    .build();
        }
    }

    @PatchMapping("{id}/update")
    public ResponseEntity<?> update(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id, @Valid @RequestBody PurposeAchieve data){
        purposeService.update(payload.getId() ,id, data);
        return ResponseHeader.builder()
                    .status(HttpStatus.OK)
                    .message("목적 수행도 업데이트 완료")
                    .build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        purposeService.delete(payload.getId(), id);
        return ResponseHeader.builder()
                    .status(HttpStatus.OK)
                    .message("목적 삭제 완료")
                    .build();
    }

    @PatchMapping("{id}/cheer/positive")
    public ResponseEntity<?> positive(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        cheerService.create(payload.getId(), id);
        return ResponseHeader.builder()
                .status(HttpStatus.OK)
                .message("응원되었습니다.")
                .build();
    }

    @PatchMapping("{id}/cheer/negative")
    public ResponseEntity<?> negative(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        cheerService.delete(payload.getId(), id);
        return ResponseHeader.builder().status(HttpStatus.OK)
                .message("응원이 해제되었습니다.")
                .build();
    }

    @GetMapping("{id}/stories")
    public ResponseEntity<?> purposeStories(
            @AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id,
            @RequestParam("page") Integer page){

        return ResponseHeader.<PageResult<?>>builder()
                    .data(storyService.readPurposeStories(payload.getId(), id, PageRequest.of(page, 10)))
                    .message("목적에 대한 스토리들이 로드되었습니다.")
                    .status(HttpStatus.OK)
                    .build();
    }

    @GetMapping("{id}/comments")
    public ResponseEntity<?> purposeComments(
            @AuthenticationPrincipal JwtPayload payload,
            @PathVariable("id") Long purposeId,
            @RequestParam("page") Integer page){
        return ResponseHeader.<PageResult<?>>builder()
                    .data(purposeCommentService.readAll(payload.getId(), purposeId, PageRequest.of(page, 15)))
                    .message("댓글이 로드되었습니다.")
                    .status(HttpStatus.OK)
                    .build();

    }

    @GetMapping
    public ResponseEntity<?> list(@AuthenticationPrincipal JwtPayload payload,
                                  @RequestParam("prefix") String prefix,
                                  @RequestParam("page") Integer page){
        return ResponseHeader.<List<ResponsePurpose>>builder()
                    .data(purposeService.readAll(payload.getId(), prefix, PageRequest.of(page, 10)))
                    .status(HttpStatus.OK)
                    .message("목적들 로드 완료")
                    .build();
    }

}
