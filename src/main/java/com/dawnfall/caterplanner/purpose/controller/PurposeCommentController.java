package com.dawnfall.caterplanner.purpose.controller;

import com.dawnfall.caterplanner.application.security.jwt.JwtPayload;
import com.dawnfall.caterplanner.common.model.network.ResponseHeader;
import com.dawnfall.caterplanner.purpose.model.request.PurposeCommentResource;
import com.dawnfall.caterplanner.purpose.service.PurposeCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("purposeComment")
public class PurposeCommentController {

    @Autowired
    private PurposeCommentService purposeCommentService;

    @PostMapping
    public ResponseEntity<?> addComment(@AuthenticationPrincipal JwtPayload payload, @RequestBody PurposeCommentResource resource){
        purposeCommentService.create(payload.getId(), resource);
        return ResponseHeader.builder()
                    .status(HttpStatus.OK)
                    .message("댓글 생성 완료")
                    .build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> removeComment(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        purposeCommentService.delete(payload.getId(), id);
        return ResponseHeader.builder()
                .status(HttpStatus.OK)
                .message("댓글 삭제 완료")
                .build();
    }

    @PutMapping("{id}")
    public ResponseEntity<?> modifyComment(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id, @RequestBody PurposeCommentResource resource){
        purposeCommentService.update(payload.getId(), id, resource);
        return ResponseHeader.builder()
                .status(HttpStatus.OK)
                .message("댓글 수정 완료")
                .build();
    }
}
