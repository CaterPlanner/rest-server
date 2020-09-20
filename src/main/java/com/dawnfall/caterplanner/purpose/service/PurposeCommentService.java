package com.dawnfall.caterplanner.purpose.service;

import com.dawnfall.caterplanner.application.exception.HttpRequestException;
import com.dawnfall.caterplanner.common.entity.Purpose;
import com.dawnfall.caterplanner.common.entity.PurposeComment;
import com.dawnfall.caterplanner.common.entity.User;
import com.dawnfall.caterplanner.common.model.network.PageResult;
import com.dawnfall.caterplanner.common.repository.PurposeCommentRepository;
import com.dawnfall.caterplanner.common.repository.PurposeRepository;
import com.dawnfall.caterplanner.purpose.model.request.PurposeCommentResource;
import com.dawnfall.caterplanner.story.model.response.ResponseStoryComment;
import com.dawnfall.caterplanner.user.model.response.ResponseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class PurposeCommentService {

    @Autowired
    private PurposeCommentRepository purposeCommentRepository;

    @Autowired
    private PurposeRepository purposeRepository;

    @Transactional
    public void create(Long userId, PurposeCommentResource resource) {
        Purpose purpose = purposeRepository.findById(resource.getPurposeId()).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));


        PurposeComment comment =  purposeCommentRepository.save(
                PurposeComment.builder()
                        .purpose(purpose)
                        .userId(userId)
                        .content(resource.getContent())
                        .build()
        );
    }

    @Transactional
    public void delete(Long userId, Long commentId) {
        PurposeComment comment = purposeCommentRepository.findById(commentId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        if(!comment.getUser().getId().equals(userId))
            throw new HttpRequestException("권한이 없습니다.", HttpStatus.UNAUTHORIZED);

        purposeCommentRepository.deleteById(commentId);
    }

    @Transactional
    public void update(Long userId, Long commentId, PurposeCommentResource resource) {
        PurposeComment comment = purposeCommentRepository.findById(commentId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        if(!comment.getUser().getId().equals(userId))
            throw new HttpRequestException("권한이 없습니다.", HttpStatus.UNAUTHORIZED);

        comment
                .setContent(resource.getContent());
    }

    public PageResult<?> readAll(Long userId, Long purposeId, Pageable pageable) {
        purposeRepository.findById(purposeId).orElseThrow(() -> new HttpRequestException("존재하지 않은 목적입니다.", HttpStatus.NOT_FOUND));
        Page<PurposeComment> pageComment = purposeCommentRepository.findByPurposeId(purposeId, pageable);

        return PageResult.of(pageable.getPageNumber() == pageComment.getTotalPages() - 1,
                pageComment.get()
                        .map(p -> {

                            User user = p.getUser();
                            return ResponseStoryComment.builder()
                                .id(p.getId())
                                .content(p.getContent())
                                .user(ResponseUser.builder()
                                        .id(user.getId())
                                        .profileUrl(user.getProfileUrl())
                                        .name(user.getName())
                                        .build())
                                .isOwner(p.getUser().getId().equals(userId))
                                .createDate(p.getCreatedDate())
                                .build();}
                        ).collect(Collectors.toList()));
    }
}