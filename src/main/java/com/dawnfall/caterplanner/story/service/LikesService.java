package com.dawnfall.caterplanner.story.service;

import com.dawnfall.caterplanner.application.exception.HttpRequestException;
import com.dawnfall.caterplanner.common.entity.Story;
import com.dawnfall.caterplanner.common.entity.StoryLikes;
import com.dawnfall.caterplanner.common.repository.StoryLikesRepository;
import com.dawnfall.caterplanner.common.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LikesService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private StoryLikesRepository storyLikesRepository;

    public void create(Long userId, Long storyId) {
        Story story = storyRepository.findById(storyId).orElseThrow(() -> new HttpRequestException("존재하지 않는 스토리입니다.", HttpStatus.NOT_FOUND));

        if(storyLikesRepository.findById(StoryLikes.Key.of(storyId, userId)).isPresent())
            throw new HttpRequestException("이미 응원하셨습니다.", HttpStatus.NOT_ACCEPTABLE);

        storyLikesRepository.save(
                StoryLikes.builder()
                    .storyId(storyId)
                    .userId(userId)
                    .build()
        );
    }

    public void delete(Long userId, Long storyId) {
        Story story = storyRepository.findById(storyId).orElseThrow(() -> new HttpRequestException("존재하지 않는 스토리입니다.", HttpStatus.NOT_FOUND));
        storyLikesRepository.deleteByPK(storyId, userId);
    }
}
