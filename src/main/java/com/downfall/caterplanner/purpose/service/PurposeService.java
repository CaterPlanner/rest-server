package com.downfall.caterplanner.purpose.service;

import com.downfall.caterplanner.application.exception.HttpRequestException;
import com.downfall.caterplanner.common.entity.*;
import com.downfall.caterplanner.common.entity.enumerate.Scope;
import com.downfall.caterplanner.common.entity.enumerate.Stat;
import com.downfall.caterplanner.common.repository.PurposeRepository;
import com.downfall.caterplanner.common.repository.StoryRepository;
import com.downfall.caterplanner.common.repository.UserRepository;
import com.downfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.downfall.caterplanner.purpose.model.response.ResponsePurposeComment;
import com.downfall.caterplanner.purpose.model.request.PurposeAchieve;
import com.downfall.caterplanner.purpose.model.request.PurposeResource;
import com.downfall.caterplanner.purpose.model.response.ResponseGoal;
import com.downfall.caterplanner.story.model.response.ResponseStory;
import com.downfall.caterplanner.user.model.response.ResponseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PurposeService {

    @Autowired
    private PurposeRepository purposeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Transactional
    public ResponsePurpose create(Long authorId, PurposeResource resource){
        User author = userRepository.findById(authorId).orElseThrow(() -> new HttpRequestException("존재하지 않는 유저입니다.", HttpStatus.BAD_REQUEST));

        Purpose purpose = purposeRepository.save(
                Purpose.builder()
                    .name(resource.getName())
                    .description(resource.getDescription())
                    .photoUrl(resource.getPhoto())
                    .startDate(resource.getStartDate())
                    .endDate(resource.getEndDate())
                    .disclosureScope(Scope.findScope(resource.getDisclosureScope()))
                    .stat(Stat.findStat(resource.getStat()))
                    .user(author)
                    .build()
        );

        List<Goal> goalList = putDetailPlans(resource, purpose);

        return ResponsePurpose.defaultBuilder(purpose)
                    .detailPlans(goalList.stream()
                            .map(g -> getGoalBuild(g)).collect(Collectors.toList()))
                    .build();
    }

    public ResponsePurpose read(Long userId, Long purposeId){
        Purpose purpose = purposeRepository.findById(purposeId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        User author = purpose.getUser();

        List<PurposeCheer> purposeCheers = purpose.getCheers();
        boolean canCheers = true;

        for(PurposeCheer purposeCheer : purposeCheers){
            if(purposeCheer.getKey().getUserId().equals(userId))
                canCheers = false;
        }

        //스토리는 최대 10개까지만 보여줌
        List<ResponseStory> storiesHeader = storyRepository.findTop10ByPurposeId(purpose.getId()).stream()
                .map(story ->
                        ResponseStory.builder()
                            .id(story.getId())
                            .title(story.getTitle())
                            .type(story.getType().getValue())
                            .createDate(story.getCreateDate())
                            .build()
                ).collect(Collectors.toList());

        return ResponsePurpose.defaultBuilder(purpose)
                .achieve(purpose.getAchieve())
                .cheers(purposeCheers.size())
                .canCheers(canCheers)
                .storyTags(storiesHeader)
                .comments(purpose.getComments().stream()
                        .map(c -> ResponsePurposeComment.builder()
                                    .commentId(c.getId())
                                    .content(c.getContent())
                                    .createDate(c.getCreateDate())
                                    .userId(c.getUser().getId())
                                    .userName(c.getUser().getName())
                                    .userProfileUrl(c.getUser().getProfileUrl())
                                    .build()
                        ).collect(Collectors.toList())
                )
                .author(ResponseUser.builder()
                            .id(author.getId())
                            .name(author.getName())
                            .build())
                .detailPlans(purpose.getDetailPlans().stream()
                        .map(g -> getGoalBuild(g)).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public ResponsePurpose modifyAll(Long id, PurposeResource resource) {
        Purpose purpose = changePurpose(id, resource);

        List<Goal> goalList = putDetailPlans(resource, purpose);

        Purpose updatedPurpose = purposeRepository.save(purpose);

        //변경된 정보만 리턴
        return ResponsePurpose.defaultBuilder(purpose)
                .detailPlans(updatedPurpose.getDetailPlans().stream()
                        .map(g -> getGoalBuild(g)).collect(Collectors.toList()))
                .build();

    }

    @Transactional
    public ResponsePurpose modify(Long id, PurposeResource resource) {
        Purpose purpose = changePurpose(id, resource);

        Purpose updatedPurpose =  purposeRepository.save(purpose);

        //변경된 정보만 리턴
        return ResponsePurpose.defaultBuilder(purpose).build();
    }

    @Transactional
    public void update(Long id, PurposeAchieve data) {
        Purpose purpose = purposeRepository.findById(id).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        purpose
                .setAchieve(data.getAchieve())
                .setStat(Stat.findStat(data.getStat()));


        data.getDetailPlansAchieves().stream().forEach(
                goalAchieve -> {
                    purpose.getDetailPlans().get(goalAchieve.getId().intValue())
                            .setAchieve(goalAchieve.getAchieve())
                            .setStat(Stat.findStat(goalAchieve.getStat()));
                }
        );

        purposeRepository.save(purpose);
    }

    @Transactional
    public void delete(Long payloadId, Long id) {
        Purpose purpose = purposeRepository.findById(id).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        if(!purpose.getUser().getId().equals(id))
            throw new HttpRequestException("권한이 없습니다.", HttpStatus.UNAUTHORIZED);


        purposeRepository.deleteById(id);
    }


    private Purpose changePurpose(Long id, PurposeResource resource) {
        Purpose purpose = purposeRepository.findById(id).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        purpose.setName(resource.getName())
               .setPhotoUrl(resource.getPhoto())
               .setStartDate(resource.getStartDate())
               .setEndDate(resource.getEndDate())
               .setDisclosureScope(Scope.findScope(resource.getDisclosureScope()))
               .setStat(Stat.findStat(resource.getStat()));


        return purpose;
    }

    private List<Goal> putDetailPlans(PurposeResource resource, Purpose purpose) {
        List<Goal> goalList = resource.getDetailPlans().stream()
                .map(g -> Goal.builder()
                        .key(Goal.Key.of(g.getPurposeId(), g.getId()))
                        .name(g.getName())
                        .description(g.getDescription())
                        .color(g.getColor())
                        .startDate(g.getStartDate())
                        .endDate(g.getEndDate())
                        .cycle(g.getCycle())
                        .stat(Stat.findStat(g.getStat()))
                        .build()
                ).collect(Collectors.toList());

        purpose.setDetailPlans(goalList.size() == 0 ? null : goalList);
        return goalList;
    }


    private ResponseGoal getGoalBuild(Goal g) {
        return ResponseGoal.builder()
                .id(g.getKey().getGoalId())
                .purposeId(g.getKey().getPurposeId())
                .color(g.getColor())
                .cycle(g.getCycle())
                .description(g.getDescription())
                .name(g.getName())
                .startDate(g.getStartDate())
                .endDate(g.getEndDate())
                .stat(g.getStat().getValue())
                .build();
    }
}
