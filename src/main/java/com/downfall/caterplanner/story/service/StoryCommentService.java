package com.downfall.caterplanner.story.service;

import com.downfall.caterplanner.application.exception.HttpRequestException;
import com.downfall.caterplanner.common.entity.Story;
import com.downfall.caterplanner.common.entity.StoryComment;
import com.downfall.caterplanner.common.repository.StoryCommentRepository;
import com.downfall.caterplanner.common.repository.StoryRepository;
import com.downfall.caterplanner.story.model.request.StoryCommentResource;
import com.downfall.caterplanner.story.model.response.ResponseStory;
import com.downfall.caterplanner.story.model.response.ResponseStoryComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoryCommentService {

    @Autowired
    private StoryCommentRepository storyCommentRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Transactional
    public void create(Long userId, StoryCommentResource resource) {
        Story story = storyRepository.findById(resource.getStoryId()).orElseThrow(
                () -> new HttpRequestException("존재하지 않는 스토리입니다.", HttpStatus.NOT_FOUND)
        );

        StoryComment comment =  storyCommentRepository.save(
                StoryComment.builder()
                        .story(story)
                        .userId(userId)
                        .content(resource.getContent())
                        .build()
        );

    }

    @Transactional
    public void delete(Long userId, Long commentId) {
        StoryComment comment = storyCommentRepository.findById(commentId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        if(!comment.getUser().getId().equals(userId))
            throw new HttpRequestException("권한이 없습니다.", HttpStatus.UNAUTHORIZED);

        storyCommentRepository.deleteById(commentId);

    }

    @Transactional
    public void update(Long userId, Long commentId, StoryCommentResource resource) {
        StoryComment comment = storyCommentRepository.findById(commentId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        if(!comment.getUser().getId().equals(userId))
            throw new HttpRequestException("권한이 없습니다.", HttpStatus.UNAUTHORIZED);

        comment
                .setContent(resource.getContent());

    }
}
