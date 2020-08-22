package com.downfall.caterplanner.purpose.controller;

import com.downfall.caterplanner.application.security.jwt.JwtPayload;
import com.downfall.caterplanner.common.model.network.ResponseHeader;
import com.downfall.caterplanner.purpose.model.request.PurposeAchieve;
import com.downfall.caterplanner.purpose.model.request.PurposeResource;
import com.downfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.downfall.caterplanner.purpose.service.CheerService;
import com.downfall.caterplanner.purpose.service.PurposeService;
import com.downfall.caterplanner.story.model.response.ResponseStory;
import com.downfall.caterplanner.story.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("purpose")
public class PurposeController {

    @Autowired
    private PurposeService purposeService;

    @Autowired
    private CheerService cheerService;

    @Autowired
    private StoryService storyService;


    @PostMapping
    public ResponseHeader<?> create(@AuthenticationPrincipal JwtPayload payload, @Valid  @RequestBody PurposeResource resource){
        return ResponseHeader.<ResponsePurpose>builder()
                    .data(purposeService.create(payload.getId(), resource))
                    .status(HttpStatus.CREATED)
                    .message("목적 생성 완료")
                    .build();
    }

    @GetMapping("{id}")
    public ResponseHeader<?> detail(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return ResponseHeader.<ResponsePurpose>builder()
                    .data(purposeService.read(payload.getId(), id))
                    .status(HttpStatus.OK)
                    .message("목적 로딩 완료")
                    .build();
    }

    @PatchMapping("{id}")
    public ResponseHeader<?> modify(@PathVariable("id") Long id, @Valid @RequestBody PurposeResource resource){
        return ResponseHeader.builder()
                .data(purposeService.modify(id, resource))
                .status(HttpStatus.OK)
                .message("목적 수정 완료")
                .build();
    }

    @PutMapping("{id}")
    public ResponseHeader<?> modifyAll(@PathVariable("id") Long id, @Valid @RequestBody PurposeResource resource){
        return ResponseHeader.builder()
                    .data(purposeService.modifyAll(id, resource))
                    .status(HttpStatus.OK)
                    .message("목적 수정 완료")
                    .build();
    }

    @PatchMapping("{id}/update")
    public ResponseHeader<?> update(@PathVariable("id") Long id, @Valid @RequestBody PurposeAchieve data){
        purposeService.update(id, data);
        return ResponseHeader.builder()
                    .status(HttpStatus.OK)
                    .message("목적 수행도 업데이트 완료")
                    .build();
    }

    @DeleteMapping("{id}")
    public ResponseHeader<?> delete(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        purposeService.delete(payload.getId(), id);
        return ResponseHeader.builder()
                    .status(HttpStatus.OK)
                    .message("목적 삭제 완료")
                    .build();
    }

    @PostMapping("{id}/cheer/positive")
    public ResponseHeader<?> addCheer(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        cheerService.create(payload.getId(), id);
        return ResponseHeader.builder()
                .status(HttpStatus.OK)
                .message("응원되었습니다.")
                .build();
    }

    @PostMapping("{id}/cheer/negative")
    public ResponseHeader<?> removeCheer(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        cheerService.delete(payload.getId(), id);
        return ResponseHeader.builder().status(HttpStatus.OK)
                .message("응원이 해제되었습니다.")
                .build();
    }

    @GetMapping("{id}/story")
    public ResponseHeader<?> purposeStories(@AuthenticationPrincipal JwtPayload payload, @PathVariable("id") Long id){
        return ResponseHeader.<List<ResponseStory>>builder()
                    .data(storyService.findPurposeStories(payload.getId(), id))
                    .status(HttpStatus.OK)
                    .build();
    }

}
