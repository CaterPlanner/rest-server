package com.dawnfall.caterplanner.purpose.controller;

import com.dawnfall.caterplanner.application.security.jwt.JwtPayload;
import com.dawnfall.caterplanner.common.ErrorCode;
import com.dawnfall.caterplanner.common.model.network.ErrorInfo;
import com.dawnfall.caterplanner.common.model.network.Response;
import com.dawnfall.caterplanner.purpose.service.PurposeService;
import com.dawnfall.caterplanner.purpose.model.request.PurposeAchieve;
import com.dawnfall.caterplanner.purpose.model.request.PurposeResource;
import com.dawnfall.caterplanner.purpose.service.CheerService;
import com.dawnfall.caterplanner.purpose.service.PurposeCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("purpose")
public class PurposeController {

    @Autowired
    private PurposeCommentService purposeCommentService;

    @Autowired
    private PurposeService purposeService;

    @Autowired
    private CheerService cheerService;

    @PostMapping(consumes = {"multipart/form-data"})
    public Response upload(HttpServletRequest request, @AuthenticationPrincipal JwtPayload payload, @ModelAttribute PurposeResource resource){
        try {
            return purposeService.create(payload.getId(), resource);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response("목적 생성 실패", new ErrorInfo(ErrorCode.FAILED_IMAGE_UPLOAD, "S3 오류"));
        }
    }

    @GetMapping("{id}")
    public Response detail(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return purposeService.read(payload.getId(), id);
    }

    @PatchMapping("{id}")
    public Response modify(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id, @Valid @ModelAttribute  PurposeResource resource){
        try {
            return purposeService.modify(payload.getId(), id, resource);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response("목적 생성 실패", new ErrorInfo(ErrorCode.FAILED_IMAGE_UPLOAD, "S3 오류"));
        }
    }

    @PutMapping("{id}")
    public Response modifyAll(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id, @Valid @ModelAttribute PurposeResource resource){
        try {
            return purposeService.modifyAll(payload.getId(), id, resource);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response("목적 생성 실패", new ErrorInfo(ErrorCode.FAILED_IMAGE_UPLOAD, "S3 오류"));
        }
    }

    @PatchMapping("{id}/update")
    public Response update(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id, @Valid @RequestBody PurposeAchieve data){
        return purposeService.update(payload.getId() ,id, data);
    }

    @DeleteMapping("{id}")
    public Response delete(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return purposeService.delete(payload.getId(), id);
    }

    @PatchMapping("{id}/cheer/positive")
    public Response positive(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return cheerService.create(payload.getId(), id);
    }

    @PatchMapping("{id}/cheer/negative")
    public Response negative(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return cheerService.delete(payload.getId(), id);
    }

    //PurposeService에서 처리한느게 나을것 으로 예상
    @GetMapping("{id}/stories")
    public Response purposeStories(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id, @RequestParam("page") Integer page){
        return purposeService.readPurposeStories(payload.getId(), id, PageRequest.of(page, 10));
    }

    @GetMapping("{id}/comments")
    public Response purposeComments(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long purposeId, @RequestParam("page") Integer page){
        return purposeCommentService.readAll(payload.getId(), purposeId, PageRequest.of(page, 15, Sort.by("createdDate").descending()));
    }

    @GetMapping
    public Response quest(@AuthenticationPrincipal JwtPayload payload, @RequestParam(name = "prefix", required = false) String prefix, @RequestParam("page") Integer page){
        return purposeService.quest(payload.getId(), prefix, PageRequest.of(page, 15, Sort.by("createdDate").descending()));
    }

}
