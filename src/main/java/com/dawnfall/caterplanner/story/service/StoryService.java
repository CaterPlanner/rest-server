package com.dawnfall.caterplanner.story.service;

import com.dawnfall.caterplanner.application.exception.HttpRequestException;
import com.dawnfall.caterplanner.common.ErrorCode;
import com.dawnfall.caterplanner.common.entity.Purpose;
import com.dawnfall.caterplanner.common.entity.Story;
import com.dawnfall.caterplanner.common.entity.StoryLikes;
import com.dawnfall.caterplanner.common.entity.User;
import com.dawnfall.caterplanner.common.entity.enumerate.Scope;
import com.dawnfall.caterplanner.common.entity.enumerate.StoryType;
import com.dawnfall.caterplanner.common.model.network.ErrorInfo;
import com.dawnfall.caterplanner.common.model.network.Response;
import com.dawnfall.caterplanner.common.repository.PurposeRepository;
import com.dawnfall.caterplanner.common.repository.StoryCommentRepository;
import com.dawnfall.caterplanner.common.repository.StoryRepository;
import com.dawnfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.dawnfall.caterplanner.story.model.response.ResponseStoryComment;
import com.dawnfall.caterplanner.common.model.network.PageResult;
import com.dawnfall.caterplanner.story.model.request.StoryResource;
import com.dawnfall.caterplanner.story.model.response.ResponseStory;
import com.dawnfall.caterplanner.user.model.response.ResponseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private PurposeRepository purposeRepository;

    @Autowired
    private StoryCommentRepository storyCommentRepository;

    public Response create(Long userId, StoryResource resource) {
        Purpose purpose = purposeRepository.findById(resource.getPurposeId()).orElseThrow(
                () -> new HttpRequestException("스토리 생성 실패", new ErrorInfo(ErrorCode.NOT_EXISTED, "존재하지 않는 목적입니다.")));


        Story story = storyRepository.save(
                Story.builder()
                        .purpose(purpose)
                        .disclosureScope(Scope.findScope(resource.getDisclosureScope()))
                        .title(resource.getTitle())
                        .content(resource.getContent())
                        .type(StoryType.findStoryType(resource.getType()))
                        .build()
        );

        return new Response("스토리 생성 완료",  ResponseStory.builder()
                .id(story.getId())
                .build());
    }

    public Response read(Long userId, Long id) {
        Story story = storyRepository.findById(id).orElseThrow(
                () -> new HttpRequestException("스토리 로드 완료", new ErrorInfo(ErrorCode.NOT_EXISTED,"존재하지 않는 스토리입니다.")));


        Purpose p = story.getPurpose();
        User user = p.getUser();

        if(story.getDisclosureScope() == Scope.PRIVATE && !user.getId().equals(userId))
            throw new HttpRequestException("스토리 로드 실패", new ErrorInfo(ErrorCode.UNAUTHORIZED, "비공개한 스토리입니다."));


            return new Response("스토리 로드 성공" , ResponseStory.builder()
                .id(story.getId())
                .title(story.getTitle())
                .content(story.getContent())
                .canLikes(isCanLikes(story.getLikes(), userId))
                .likesCount(story.getLikes().size())
                .commentCount(story.getComments().size())
                .isOwner(user.getId().equals(userId))
                .comments(storyCommentRepository.findTop10ByStoryId(story.getId()).stream()
                        .map(c -> ResponseStoryComment.builder()
                                .id(c.getId())
                                .content(c.getContent())
                                .user(ResponseUser.builder()
                                        .id(c.getUser().getId())
                                        .name(c.getUser().getName())
                                        .profileUrl(c.getUser().getProfileUrl())
                                        .build())
                                .isOwner(c.getUser().getId().equals(userId))
                                .createDate(c.getCreatedDate())
                                .build()
                        ).collect(Collectors.toList()))
                .createDate(story.getCreatedDate())
                .author(
                        ResponseUser.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .profileUrl(user.getProfileUrl())
                                .build())
                .purpose(
                        ResponsePurpose.builder()
                                .id(p.getId())
                                .name(p.getName())
                                .achieve(p.getAchieve())
                                .photoUrl(p.getPhotoUrl())
                                .stat(p.getStat().getValue())
                                .build()
                ).build());
    }

    @Transactional
    public Response update(Long userId, Long storyId, StoryResource resource) {
        Story story = storyRepository.findById(storyId).orElseThrow(
                () -> new HttpRequestException("스토리 수정 실패", new ErrorInfo(ErrorCode.NOT_EXISTED, "존재하지 않는 스토리입니다.")));

        if(!story.getPurpose().getUser().getId().equals(userId) )
            throw new HttpRequestException("스토리 수정 실패", new ErrorInfo(ErrorCode.UNAUTHORIZED, "권한이 없습니다."));

        story
                .setTitle(resource.getTitle())
                .setContent(resource.getContent())
                .setType(StoryType.findStoryType(resource.getType()));

        return new Response("스토리 수정 성공");
    }

    public Response delete(Long userId, Long purposeId) {
        Story story = storyRepository.findById(purposeId).orElseThrow(
                () -> new HttpRequestException("스토리 삭제 실패", new ErrorInfo(ErrorCode.NOT_EXISTED, "존재하지 않는 스토리입니다.")));

        if(!story.getPurpose().getUser().getId().equals(userId) )
            throw new HttpRequestException("스토리 삭제 실패", new ErrorInfo(ErrorCode.UNAUTHORIZED, "권한이 없습니다."));

        storyRepository.delete(story);
        return new Response("스토리 삭제 성공");
    }


    public Response readAllForFront(Long userId, Integer type, Pageable pageable) {
        Page<Story> result = type == null ? storyRepository.findAllByDisclosureScope(Scope.PUBLIC, pageable) : storyRepository.findAllByType(type, pageable);
        Stream<Story> pageableResultStream = result.get();

        return new Response("피드 데이터 로드 성공", PageResult.of(pageable.getPageNumber() == result.getTotalPages() - 1 , pageableResultStream.map(s -> {
            Purpose purpose = s.getPurpose();
            User author = purpose.getUser();

            return ResponseStory.builder()
                    .id(s.getId())
                    .title(s.getTitle())
                    .content(s.getContent())
                    .commentCount(s.getComments().size())
                    .likesCount(s.getLikes().size())
                    .type(s.getType().getValue())
                    .createDate(s.getCreatedDate())
                    .canLikes(isCanLikes(s.getLikes(), userId))
                    .isOwner(author.getId().equals(userId))
                    .author(ResponseUser.builder()
                            .id(author.getId())
                            .name(author.getName())
                            .profileUrl(author.getProfileUrl())
                            .build())
                    .purpose(ResponsePurpose.builder()
                            .id(purpose.getId())
                            .name(purpose.getName())
                            .build())
                    .build();
        }).collect(Collectors.toList())));
    }

    private boolean isCanLikes(List<StoryLikes> storyLikesList, Long targetId){
        boolean canLikes = true;

        for(StoryLikes storyLikes : storyLikesList){
            if(storyLikes.getUserId().equals(targetId))
                canLikes = false;
        }
        return canLikes;
    }

}
