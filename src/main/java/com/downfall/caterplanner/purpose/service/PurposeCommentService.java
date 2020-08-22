package com.downfall.caterplanner.purpose.service;

import com.downfall.caterplanner.application.exception.HttpRequestException;
import com.downfall.caterplanner.common.entity.Purpose;
import com.downfall.caterplanner.common.entity.PurposeComment;
import com.downfall.caterplanner.common.entity.User;
import com.downfall.caterplanner.common.repository.PurposeCommentRepository;
import com.downfall.caterplanner.common.repository.PurposeRepository;
import com.downfall.caterplanner.common.repository.UserRepository;
import com.downfall.caterplanner.purpose.model.request.PurposeCommentResource;
import com.downfall.caterplanner.purpose.model.response.ResponsePurposeComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}