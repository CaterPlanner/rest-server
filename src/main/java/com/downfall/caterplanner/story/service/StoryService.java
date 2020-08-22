package com.downfall.caterplanner.story.service;

import com.downfall.caterplanner.application.exception.HttpRequestException;
import com.downfall.caterplanner.common.entity.Purpose;
import com.downfall.caterplanner.common.entity.Story;
import com.downfall.caterplanner.common.entity.StoryLikes;
import com.downfall.caterplanner.common.entity.User;
import com.downfall.caterplanner.common.entity.enumerate.StoryType;
import com.downfall.caterplanner.common.repository.PurposeRepository;
import com.downfall.caterplanner.common.repository.StoryRepository;
import com.downfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.downfall.caterplanner.story.model.request.StoryResource;
import com.downfall.caterplanner.story.model.response.ResponseStory;
import com.downfall.caterplanner.story.model.response.ResponseStoryComment;
import com.downfall.caterplanner.user.model.response.ResponseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.Response;
import java.util.List;
import java.util.stream.Collectors;

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

        User user = purpose.getUser();

        return ResponseStory.builder()
                .id(story.getId())
                .title(story.getTitle())
                .content(story.getContent())
                .likesCount(0)
                .commentCount(0)
                .purpose(
                        ResponsePurpose.builder()
                                .id(purpose.getId())
                                .name(purpose.getName())
                                .build())
                .profile(
                        ResponseUser.builder()
                                .id(userId)
                                .name(user.getName())
                                .profileUrl(user.getProfileUrl())
                                .build()
                ).build();
    }


    public List<ResponseStory> findPurposeStories(Long userId, Long purposeId){
        Purpose purpose = purposeRepository.findById(purposeId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.BAD_REQUEST));

        List<Story> stories = storyRepository.findByPurposeId(purpose.getId());


        return stories.stream()
                .map(s -> getResponseStory(userId, s)).collect(Collectors.toList());
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

    public ResponseStory read(Long userId, Long id) {
        Story story = storyRepository.findById(id).orElseThrow(() -> new HttpRequestException("존재하지 않는 스토리입니다.", HttpStatus.NOT_FOUND));

        return getResponseStory(userId, story);
    }

    private ResponseStory getResponseStory(Long userId, Story story) {
        List<StoryLikes> likes = story.getLikes();
        boolean canLikes = true;

        for(StoryLikes storyLikes : likes){
            if(storyLikes.getKey().getUserId().equals(userId))
                canLikes = false;
        }

        Purpose p = story.getPurpose();
        User user = p.getUser();

        return ResponseStory.builder()
                .id(story.getId())
                .content(story.getContent())
                .canLikes(canLikes)
                .likesCount(likes.size())
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
                .profile(
                        ResponseUser.builder()
                                .id(user.getId())
                                .name(user.getName())
                                .backImageUrl(user.getBackImageUrl())
                                .build())
                .purpose(
                        ResponsePurpose.builder()
                                .id(p.getId())
                                .name(p.getName())
                                .build()
                ).build();
    }

    public void delete(Long userId, Long purposeId) {
        Story story = storyRepository.findById(purposeId).orElseThrow(() -> new HttpRequestException("존재하지 않는 스토리입니다.", HttpStatus.NOT_FOUND));

        if(!story.getPurpose().getUser().getId().equals(userId) )
            throw new HttpRequestException("권한이 없습니다.", HttpStatus.UNAUTHORIZED);

        storyRepository.deleteById(userId);
    }
}
