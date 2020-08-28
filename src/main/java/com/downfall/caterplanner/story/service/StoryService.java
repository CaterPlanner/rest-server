package com.downfall.caterplanner.story.service;

import com.downfall.caterplanner.application.exception.HttpRequestException;
import com.downfall.caterplanner.common.entity.Purpose;
import com.downfall.caterplanner.common.entity.Story;
import com.downfall.caterplanner.common.entity.StoryLikes;
import com.downfall.caterplanner.common.entity.User;
import com.downfall.caterplanner.common.entity.enumerate.StoryType;
import com.downfall.caterplanner.common.model.network.PageResult;
import com.downfall.caterplanner.common.repository.PurposeRepository;
import com.downfall.caterplanner.common.repository.StoryRepository;
import com.downfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.downfall.caterplanner.story.model.request.StoryResource;
import com.downfall.caterplanner.story.model.response.ResponseStory;
import com.downfall.caterplanner.story.model.response.ResponseStoryComment;
import com.downfall.caterplanner.user.model.response.ResponseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private PurposeRepository purposeRepository;

    public ResponseStory create(Long userId, StoryResource resource) {
        Purpose purpose = purposeRepository.findById(resource.getPurposeId()).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.BAD_REQUEST));

        Story story = storyRepository.save(
                Story.builder()
                        .purpose(purpose)
                        .title(resource.getTitle())
                        .content(resource.getContent())
                        .type(StoryType.findStoryType(resource.getType()))
                        .build()
        );

        return ResponseStory.builder()
                .id(story.getId())
                .build();
    }

    public ResponseStory read(Long userId, Long id) {
        Story story = storyRepository.findById(id).orElseThrow(() -> new HttpRequestException("존재하지 않는 스토리입니다.", HttpStatus.NOT_FOUND));

        Purpose p = story.getPurpose();
        User user = p.getUser();

        return ResponseStory.builder()
                .id(story.getId())
                .title(story.getTitle())
                .content(story.getContent())
                .canLikes(isCanLikes(story.getLikes(), userId))
                .likesCount(story.getLikes().size())
                .commentCount(story.getComments().size())
                .comments(story.getComments().stream()
                        .map(c -> ResponseStoryComment.builder()
                                .commentId(c.getId())
                                .content(c.getContent())
                                .userId(c.getUser().getId())
                                .userName(c.getUser().getName())
                                .userProfileUrl(c.getUser().getProfileUrl())
                                .createDate(c.getCreateDate())
                                .build()
                        ).collect(Collectors.toList()))
                .createDate(story.getCreateDate())
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
                                .build()
                ).build();
    }

    @Transactional
    public void update(Long userId, StoryResource resource) {
        Story story = storyRepository.findById(resource.getPurposeId()).orElseThrow(() -> new HttpRequestException("존재하지 않는 스토리입니다.", HttpStatus.NOT_FOUND));

        if(!story.getPurpose().getUser().getId().equals(userId) )
            throw new HttpRequestException("권한이 없습니다.", HttpStatus.UNAUTHORIZED);

        story
                .setTitle(resource.getTitle())
                .setContent(resource.getContent())
                .setType(StoryType.findStoryType(resource.getType()));

    }

    public void delete(Long userId, Long purposeId) {
        Story story = storyRepository.findById(purposeId).orElseThrow(() -> new HttpRequestException("존재하지 않는 스토리입니다.", HttpStatus.NOT_FOUND));

        if(!story.getPurpose().getUser().getId().equals(userId) )
            throw new HttpRequestException("권한이 없습니다.", HttpStatus.UNAUTHORIZED);

        storyRepository.deleteById(userId);
    }

    public PageResult<?> readPurposeStories(Long userId, Long purposeId, Pageable pageable){
        Purpose purpose = purposeRepository.findById(purposeId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.BAD_REQUEST));

        Page<Story> pageResult = storyRepository.findByPurposeId(purpose.getId(), pageable);

        return PageResult.of(pageable.getPageNumber() == pageResult.getTotalPages() - 1 ,
                    pageResult.get().map(s -> getResponseStoryByFront(userId, s)).collect(Collectors.toList()));
    }


    public PageResult<?> readAllForFront(Long userId, Integer type, Pageable pageable) {
        Page<Story> result = type == null ? storyRepository.findAll(pageable) : storyRepository.findAllByType(type, pageable);
        Stream<Story> pageableResultStream = result.get();

        return PageResult.of(pageable.getPageNumber() == result.getTotalPages() - 1 , pageableResultStream.map(s -> getResponseStoryByFront(userId, s)).collect(Collectors.toList()));
    }

    private boolean isCanLikes(List<StoryLikes> storyLikesList, Long targetId){
        boolean canLikes = true;

        for(StoryLikes storyLikes : storyLikesList){
            if(storyLikes.getKey().getUserId().equals(targetId))
                canLikes = false;
        }
        return canLikes;
    }

    private ResponseStory getResponseStoryByFront(Long userId, Story s){
        Purpose purpose = s.getPurpose();
        User author = purpose.getUser();

            return ResponseStory.builder()
                    .id(s.getId())
                    .title(s.getTitle())
                    .content(s.getContent())
                    .commentCount(s.getComments().size())
                    .likesCount(s.getLikes().size())
                    .type(s.getType().getValue())
                    .createDate(s.getCreateDate())
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
    }

}
