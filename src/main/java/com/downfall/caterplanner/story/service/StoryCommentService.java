package com.downfall.caterplanner.story.service;

import com.downfall.caterplanner.application.exception.HttpRequestException;
import com.downfall.caterplanner.common.entity.Story;
import com.downfall.caterplanner.common.entity.StoryComment;
import com.downfall.caterplanner.common.entity.User;
import com.downfall.caterplanner.common.model.network.PageResult;
import com.downfall.caterplanner.common.repository.StoryCommentRepository;
import com.downfall.caterplanner.common.repository.StoryRepository;
import com.downfall.caterplanner.story.model.request.StoryCommentResource;
import com.downfall.caterplanner.story.model.response.ResponseStoryComment;
import com.downfall.caterplanner.user.model.response.ResponseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class StoryCommentService {

    @Autowired
    private StoryCommentRepository storyCommentRepository;

    @Autowired
    private StoryRepository storyRepository;

    //storyComment에서 fk 적용못함 넣으면 애초에 생성된다는 의미로 들어가기때문에 storycomment는 insertable = false 조건이 걸려 있어
    //아무일도 일어나지 않는다.

    @Transactional
    public void create(Long userId, StoryCommentResource resource) {
        Story story = storyRepository.findById(resource.getStoryId()).orElseThrow(
                () -> new HttpRequestException("존재하지 않는 스토리입니다.", HttpStatus.NOT_FOUND)
        );

        StoryComment comment =  storyCommentRepository.save(
                StoryComment.builder()
                        .storyId(story.getId())
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

    public PageResult<?> readAll(Long userId, Long storyId, Pageable pageable) {
        storyRepository.findById(storyId).orElseThrow(() -> new HttpRequestException("존재하지 않는 스토리입니다.", HttpStatus.NOT_FOUND));
        Page<StoryComment> pageComment = storyCommentRepository.findByStoryId(storyId, pageable);

        return PageResult.of(pageable.getPageNumber() >= pageComment.getTotalPages() - 1,
                pageComment.get()
                        .map(s -> {

                                    User user = s.getUser();

                                    return ResponseStoryComment.builder()
                                            .commentId(s.getId())
                                            .content(s.getContent())
                                            .user(ResponseUser.builder()
                                                    .id(user.getId())
                                                    .name(user.getName())
                                                    .profileUrl(user.getProfileUrl())
                                                    .build())
                                            .isOwner(s.getUser().getId().equals(userId))
                                            .createDate(s.getCreatedDate())
                                            .build();
                                }
                        ).collect(Collectors.toList()));
    }
}
