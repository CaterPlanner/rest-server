package com.downfall.caterplanner.purpose.service;

import com.downfall.caterplanner.application.aws.S3Util;
import com.downfall.caterplanner.application.exception.HttpRequestException;
import com.downfall.caterplanner.common.entity.*;
import com.downfall.caterplanner.common.entity.enumerate.Scope;
import com.downfall.caterplanner.common.entity.enumerate.Stat;
import com.downfall.caterplanner.common.repository.PurposeRepository;
import com.downfall.caterplanner.common.repository.StoryRepository;
import com.downfall.caterplanner.common.repository.UserRepository;
import com.downfall.caterplanner.purpose.model.request.GoalResource;
import com.downfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.downfall.caterplanner.purpose.model.response.ResponsePurposeComment;
import com.downfall.caterplanner.purpose.model.request.PurposeAchieve;
import com.downfall.caterplanner.purpose.model.request.PurposeResource;
import com.downfall.caterplanner.purpose.model.response.ResponseGoal;
import com.downfall.caterplanner.story.model.response.ResponseStory;
import com.downfall.caterplanner.user.model.response.ResponseUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class PurposeService {

    @Autowired
    private PurposeRepository purposeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private S3Util s3Util;

    private ObjectMapper mapper = new ObjectMapper();


    private String getPhotoUrl(Long id, MultipartFile photo) throws IOException {
        String newFileName = "purpose/purposePhoto-"+id;
        return s3Util.upload(newFileName, photo);
    }

    public ResponsePurpose create(Long userId, PurposeResource resource) throws IOException {
        User author = userRepository.findById(userId).orElseThrow(() -> new HttpRequestException("존재하지 않는 유저입니다.", HttpStatus.BAD_REQUEST));


        Purpose purpose = purposeRepository.save(
                Purpose.builder()
                    .name(resource.getName())
                    .description(resource.getDescription())
                    .startDate( LocalDate.parse(resource.getStartDate(), DateTimeFormatter.ISO_DATE))
                    .endDate( LocalDate.parse(resource.getEndDate(), DateTimeFormatter.ISO_DATE))
                    .disclosureScope(Scope.findScope(resource.getDisclosureScope()))
                    .stat(Stat.findStat(resource.getStat()))
                    .user(author)
                    .build()
        );

        List<Goal> goalList = putDetailPlans(resource, purpose);

        String photoUrl = getPhotoUrl(purpose.getId(), resource.getPhoto());

        purpose.setPhotoUrl(photoUrl);


        return ResponsePurpose.builder()
                        .id(purpose.getId())
                        .photoUrl(purpose.getPhotoUrl())
                        .build();
    }

    public ResponsePurpose read(Long userId, Long purposeId){
        Purpose purpose = purposeRepository.findById(purposeId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        User author = purpose.getUser();


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
                .isOwner(userId.equals(purpose.getUser().getId()))
                .achieve(purpose.getAchieve())
                .cheers(purpose.getCheers().size())
                .canCheers(isCanCheer(purpose.getCheers(), userId))
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
                            .profileUrl(author.getProfileUrl())
                            .build())
                .detailPlans(purpose.getDetailPlans().stream()
                        .map(g -> getGoalBuild(g)).collect(Collectors.toList()))
                .build();
    }

    public ResponsePurpose modifyAll(Long userId, Long purposeId, PurposeResource resource) throws IOException {

        Purpose purpose = purposeRepository.findById(purposeId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        if(!userId.equals(purpose.getUser().getId()))
            throw new HttpRequestException("권한이 없습니다" , HttpStatus.UNAUTHORIZED);

        changePurpose(purpose, resource);

        List<Goal> goalList = putDetailPlans(resource, purpose);


        Purpose updatedPurpose = purposeRepository.save(purpose);

        //변경된 정보만 리턴
        return ResponsePurpose.builder()
                .photoUrl(purpose.getPhotoUrl())
                .build();
    }

    public ResponsePurpose modify(Long userId, Long purposeId, PurposeResource resource) throws IOException {
        Purpose purpose = purposeRepository.findById(purposeId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        if(!userId.equals(purpose.getUser().getId()))
            throw new HttpRequestException("권한이 없습니다" , HttpStatus.UNAUTHORIZED);

        changePurpose(purpose, resource);

        Purpose updatedPurpose =  purposeRepository.save(purpose);

        //변경된 정보만 리턴
        return ResponsePurpose.builder()
                .photoUrl(purpose.getPhotoUrl())
                .build();
    }

    public void update(Long userId, Long purposeId, PurposeAchieve data) {
        Purpose purpose = purposeRepository.findById(purposeId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        purpose
                .setAchieve(data.getAchieve())
                .setStat(Stat.findStat(data.getStat()));


        data.getDetailPlansAchieves().stream().forEach(
                goalAchieve -> {
                    purpose.getDetailPlans().get(goalAchieve.getId().intValue())
                            .setBriefingCount(goalAchieve.getBriefingCount())
                            .setLastBriefingDate(LocalDate.parse(goalAchieve.getLastBriefingDate(), DateTimeFormatter.ISO_DATE))
                            .setStat(Stat.findStat(goalAchieve.getStat()));
                }
        );

        purposeRepository.save(purpose);
    }

    public void delete(Long userId, Long purposeId) {
        Purpose purpose = purposeRepository.findById(purposeId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        if(!purpose.getUser().getId().equals(purposeId))
            throw new HttpRequestException("권한이 없습니다.", HttpStatus.UNAUTHORIZED);


        purposeRepository.deleteById(purposeId);
    }

    public List<ResponsePurpose> readAll(Long userId, String prefix, Pageable pageable){
        return purposeRepository.findByNameStartsWith(prefix, pageable).get()
                .map(p -> ResponsePurpose.builder()
                                .id(p.getId())
                                .name(p.getName())
                                .achieve(p.getAchieve())
                                .description(p.getDescription())
                                .photoUrl(p.getPhotoUrl())
                                .endDate(p.getEndDate())
                                .stat(p.getStat().getValue())
                                .canCheers(isCanCheer(p.getCheers(), userId))
                                .cheers(p.getCheers().size())
                                .author(ResponseUser.builder()
                                        .id(p.getUser().getId())
                                        .name(p.getUser().getName())
                                        .profileUrl(p.getUser().getProfileUrl())
                                        .build())
                                .createDate(p.getCreateDate())
                                .build()).collect(Collectors.toList());
    }

    private boolean isCanCheer(List<PurposeCheer> cheerList, Long targetId){
        boolean canLikes = true;

        for(PurposeCheer storyLikes : cheerList){
            if(storyLikes.getKey().getUserId().equals(targetId))
                canLikes = false;
        }
        return canLikes;
    }


    private Purpose changePurpose(Purpose purpose, PurposeResource resource) throws IOException {

        purpose.setName(resource.getName())
               .setPhotoUrl(getPhotoUrl(purpose.getId(), resource.getPhoto()))
               .setStartDate(null)
               .setEndDate(null)
               .setDisclosureScope(Scope.findScope(resource.getDisclosureScope()))
               .setStat(Stat.findStat(resource.getStat()));
        if(resource.getPhoto() != null)
            purpose.setPhotoUrl(getPhotoUrl(purpose.getId(), resource.getPhoto()));

        return purpose;
    }

    private List<Goal> putDetailPlans(PurposeResource resource, Purpose purpose) throws JsonProcessingException {

        List<GoalResource> detailPlans = mapper.readValue(resource.getDetailPlans(),new TypeReference<List<GoalResource>>(){});

        List<Goal> goalList = detailPlans.stream()
                .map(g -> Goal.builder()
                        .key(Goal.Key.of(purpose.getId(), g.getId()))
                        .name(g.getName())
                        .description(g.getDescription())
                        .color(g.getColor())
                        .briefingCount(g.getBriefingCount())
                        .lastBriefingDate(LocalDate.parse(g.getLastBriefingDate(), DateTimeFormatter.ISO_DATE))
                        .startDate(LocalDate.parse(g.getStartDate(), DateTimeFormatter.ISO_DATE))
                        .endDate(LocalDate.parse(g.getEndDate(), DateTimeFormatter.ISO_DATE))
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
                .briefingCount(g.getBriefingCount())
                .lastBriefingDate(g.getLastBriefingDate())
                .startDate(g.getStartDate())
                .endDate(g.getEndDate())
                .stat(g.getStat().getValue())
                .build();
    }
}
