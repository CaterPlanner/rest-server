package com.dawnfall.caterplanner.story.service;

import com.dawnfall.caterplanner.application.exception.HttpRequestException;
import com.dawnfall.caterplanner.common.ErrorCode;
import com.dawnfall.caterplanner.common.entity.Story;
import com.dawnfall.caterplanner.common.entity.StoryComment;
import com.dawnfall.caterplanner.common.entity.User;
import com.dawnfall.caterplanner.common.model.network.ErrorInfo;
import com.dawnfall.caterplanner.common.model.network.Response;
import com.dawnfall.caterplanner.common.repository.StoryCommentRepository;
import com.dawnfall.caterplanner.common.repository.StoryRepository;
import com.dawnfall.caterplanner.common.model.network.PageResult;
import com.dawnfall.caterplanner.story.model.request.StoryCommentResource;
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
public class StoryCommentService {

    @Autowired
    private StoryCommentRepository storyCommentRepository;

    @Autowired
    private StoryRepository storyRepository;

    //storyComment에서 fk 적용못함 넣으면 애초에 생성된다는 의미로 들어가기때문에 storycomment는 insertable = false 조건이 걸려 있어
    //아무일도 일어나지 않는다.

    @Transactional
    public Response create(Long userId, StoryCommentResource resource) {
        Story story = storyRepository.findById(resource.getStoryId()).orElseThrow(
                () -> new HttpRequestException("스토리 댓글 생성 실패", new ErrorInfo(ErrorCode.NOT_EXISTED,"존재하지 않는 스토리입니다."))
        );

        StoryComment comment =  storyCommentRepository.save(
                StoryComment.builder()
                        .storyId(story.getId())
                        .userId(userId)
                        .content(resource.getContent())
                        .build()
        );

        return new Response("스토리 댓글 생성 성공");
    }



    @Transactional
    public Response delete(Long userId, Long commentId) {
        StoryComment comment = storyCommentRepository.findById(commentId).orElseThrow(
                () -> new HttpRequestException("스토리 댓글 삭제 실패", new ErrorInfo(ErrorCode.NOT_EXISTED, "존재하지 않는 스토리 댓글 입니다.")));

        if(!comment.getUser().getId().equals(userId))
            throw new HttpRequestException("스토리 댓글 삭제 실패", new ErrorInfo(ErrorCode.UNAUTHORIZED,"권한이 없습니다."));

        storyCommentRepository.deleteById(commentId);
        return new Response("스토리 댓글 삭제 성공");
    }

    @Transactional
    public Response update(Long userId, Long commentId, StoryCommentResource resource) {
        StoryComment comment = storyCommentRepository.findById(commentId).orElseThrow(
                () -> new HttpRequestException("스토리 댓글 수정 실패", new ErrorInfo(ErrorCode.NOT_EXISTED, "존재하지 않는 목적입니다.")));

        if(!comment.getUser().getId().equals(userId))
            throw new HttpRequestException("스토리 댓글 수정 실패", new ErrorInfo(ErrorCode.UNAUTHORIZED,"권한이 없습니다."));

        comment
                .setContent(resource.getContent());

        return new Response("스토리 댓글 수정 성공");
    }

    public Response readAll(Long userId, Long storyId, Pageable pageable) {
        storyRepository.findById(storyId).orElseThrow(
                () -> new HttpRequestException("스토리 댓글 전체 로드 실패", new ErrorInfo(ErrorCode.NOT_EXISTED, "존재하지 않는 스토리입니다.")));
        Page<StoryComment> pageComment = storyCommentRepository.findByStoryId(storyId, pageable);

        return new Response("스토리 댓글 전체 로드 성공" , PageResult.of(pageable.getPageNumber() >= pageComment.getTotalPages() - 1,
                pageComment.get()
                        .map(s -> {

                                    User user = s.getUser();

                                    return ResponseStoryComment.builder()
                                            .id(s.getId())
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
                        ).collect(Collectors.toList())));
    }
}
