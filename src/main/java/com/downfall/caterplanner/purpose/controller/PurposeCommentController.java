package com.downfall.caterplanner.purpose.controller;

import com.downfall.caterplanner.application.security.jwt.JwtPayload;
import com.downfall.caterplanner.common.model.network.ResponseHeader;
import com.downfall.caterplanner.purpose.model.request.PurposeCommentResource;
import com.downfall.caterplanner.purpose.service.PurposeCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("purposeComment")
public class PurposeCommentController {

    @Autowired
    private PurposeCommentService purposeCommentService;

    @PostMapping
    public ResponseHeader<?> addComment(@AuthenticationPrincipal JwtPayload payload, @RequestBody PurposeCommentResource resource){
        purposeCommentService.create(payload.getId(), resource);
        return ResponseHeader.builder()
                    .status(HttpStatus.OK)
                    .message("댓글 생성 완료")
                    .build();
    }

    @DeleteMapping("{id}")
    public ResponseHeader<?> removeComment(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        purposeCommentService.delete(payload.getId(), id);
        return ResponseHeader.builder()
                .status(HttpStatus.OK)
                .message("댓글 삭제 완료")
                .build();
    }

    @PutMapping("{id}")
    public ResponseHeader<?> modifyComment(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id, @RequestBody PurposeCommentResource resource){
        purposeCommentService.update(payload.getId(), id, resource);
        return ResponseHeader.builder()
                .status(HttpStatus.OK)
                .message("댓글 수정 완료")
                .build();
    }
}
