package com.dawnfall.caterplanner.story.service;

import com.dawnfall.caterplanner.application.exception.HttpRequestException;
import com.dawnfall.caterplanner.common.ErrorCode;
import com.dawnfall.caterplanner.common.entity.Story;
import com.dawnfall.caterplanner.common.entity.StoryLikes;
import com.dawnfall.caterplanner.common.model.network.ErrorInfo;
import com.dawnfall.caterplanner.common.model.network.Response;
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

    public Response create(Long userId, Long storyId) {
        Story story = storyRepository.findById(storyId).orElseThrow(
                () -> new HttpRequestException("스토리 좋아요 실패", new ErrorInfo(ErrorCode.NOT_EXISTED,"존재하지 않는 스토리입니다.")));

        if(storyLikesRepository.findById(StoryLikes.Key.of(storyId, userId)).isPresent())
            throw new HttpRequestException("스토리 좋아요 실패", new ErrorInfo(ErrorCode.ALREADY ,"이미 응원하셨습니다."));

        storyLikesRepository.save(
                StoryLikes.builder()
                    .storyId(storyId)
                    .userId(userId)
                    .build()
        );
        return new Response("스토리 좋아요 성공");
    }

    public Response delete(Long userId, Long storyId) {
        Story story = storyRepository.findById(storyId).orElseThrow(
                () -> new HttpRequestException("스토리 좋아요 취소 실패" , new ErrorInfo(ErrorCode.NOT_EXISTED, "존재하지 않는 스토리입니다.")));
        storyLikesRepository.deleteByPK(storyId, userId);
        return new Response("스토리 좋아요 취소 성공");
    }
}
