package com.dawnfall.caterplanner.purpose.service;

import com.dawnfall.caterplanner.application.exception.HttpRequestException;
import com.dawnfall.caterplanner.application.aws.S3Util;
import com.dawnfall.caterplanner.common.entity.Goal;
import com.dawnfall.caterplanner.common.entity.Purpose;
import com.dawnfall.caterplanner.common.entity.PurposeCheer;
import com.dawnfall.caterplanner.common.entity.User;
import com.dawnfall.caterplanner.common.entity.*;
import com.dawnfall.caterplanner.common.entity.enumerate.Scope;
import com.dawnfall.caterplanner.common.entity.enumerate.Stat;
import com.dawnfall.caterplanner.common.model.network.PageResult;
import com.dawnfall.caterplanner.common.repository.PurposeCommentRepository;
import com.dawnfall.caterplanner.common.repository.PurposeRepository;
import com.dawnfall.caterplanner.common.repository.StoryRepository;
import com.dawnfall.caterplanner.common.repository.UserRepository;
import com.dawnfall.caterplanner.purpose.model.request.GoalResource;
import com.dawnfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.dawnfall.caterplanner.purpose.model.request.PurposeAchieve;
import com.dawnfall.caterplanner.purpose.model.request.PurposeResource;
import com.dawnfall.caterplanner.purpose.model.response.ResponseGoal;
import com.dawnfall.caterplanner.story.model.response.ResponseStory;
import com.dawnfall.caterplanner.user.model.response.ResponseUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.stream.Stream;


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
    private PurposeCommentRepository purposeCommentRepository;

    @Autowired
    private S3Util s3Util;

    private ObjectMapper mapper = new ObjectMapper();


    private String makeFileName(Long id){
        return "purpose/purposePhoto-" +id;
    }

    private String getPhotoUrl(Long id, MultipartFile photo) throws IOException {
        return s3Util.upload(makeFileName(id), photo);
    }

    public ResponsePurpose create(Long userId, PurposeResource resource) throws IOException {
        User author = userRepository.findById(userId).orElseThrow(() -> new HttpRequestException("존재하지 않는 유저입니다.", HttpStatus.BAD_REQUEST));

        LocalDate startDate = LocalDate.parse(resource.getStartDate(), DateTimeFormatter.ISO_DATE);

        if(!startDate.equals(LocalDate.now()))
            throw new HttpRequestException("올바르지 않은 데이터입니다.", HttpStatus.BAD_REQUEST);

        Purpose purpose = purposeRepository.save(
                Purpose.builder()
                    .name(resource.getName())
                    .description(resource.getDescription())
                    .startDate( startDate)
                    .endDate( LocalDate.parse(resource.getEndDate(), DateTimeFormatter.ISO_DATE))
                    .disclosureScope(Scope.findScope(resource.getDisclosureScope()))
                    .stat(Stat.findStat(resource.getStat()))
                    .user(author)
                    .build()
        );

        List<Goal> goalList = decodeDetailPlans(resource, purpose);

        purpose.setDetailPlans(goalList.size() == 0 ? null : goalList);

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

        if(purpose.getDisclosureScope() == Scope.PRIVATE && !author.getId().equals(userId))
            throw new HttpRequestException("비공개한 목적입니다.", HttpStatus.UNAUTHORIZED);

        //스토리는 최대 10개까지만 보여줌
        List<ResponseStory> storiesHeader = storyRepository.findTop10ByPurposeId(purpose.getId()).stream()
                .map(story ->
                        ResponseStory.builder()
                            .id(story.getId())
                            .title(story.getTitle())
                            .type(story.getType().getValue())
                            .createDate(story.getCreatedDate())
                            .build()
                ).collect(Collectors.toList());

        return ResponsePurpose.defaultBuilder(purpose)
                .isOwner(userId.equals(purpose.getUser().getId()))
                .achieve(purpose.getAchieve())
                .cheersCount(purpose.getCheers().size())
                .commentCount(purpose.getComments().size())
                .createDate(purpose.getCreatedDate())
                .canCheer(isCanCheer(purpose.getCheers(), userId))
                .storyTags(storiesHeader)
//                .comments(purpose.getComments().stream()
//                        .map(c -> ResponsePurposeComment.builder()
//                                    .commentId(c.getId())
//                                    .content(c.getContent())
//                                    .createDate(c.getCreateDate())
//                                    .user(ResponseUser.builder()
//                                            .id(c.getUser().getId())
//                                            .name(c.getUser().getName())
//                                            .profileUrl(c.getUser().getProfileUrl())
//                                            .build())
//                                    .isOwner(c.getUser().getId().equals(userId))
//                                    .build()
//                        ).collect(Collectors.toList())
//                )
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

        List<Goal> goalList = decodeDetailPlans(resource, purpose);

        purpose.getDetailPlans().clear();
        purpose.getDetailPlans().addAll(goalList);

        if(resource.getAchieve() != null)
            purpose.setAchieve(resource.getAchieve());

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

        if(data.getAchieve() != null)
            purpose.setAchieve(data.getAchieve());

        if(data.getStat() != null)
            purpose.setStat(Stat.findStat(data.getStat()));

        if(data.getModifiedGoalAchieve() != null) {
            data.getModifiedGoalAchieve().stream().forEach(
                    goalAchieve -> {

                        Goal goal = purpose.getDetailPlans().get(goalAchieve.getId().intValue());
                        LocalDate currentBriefingDate = LocalDate.parse(goalAchieve.getLastBriefingDate(), DateTimeFormatter.ISO_DATE);

                        if(!currentBriefingDate.equals(LocalDate.now()) || (goal.getLastBriefingDate() != null && goal.getLastBriefingDate().equals(LocalDate.now())))

                            throw new HttpRequestException("잘못된 브리핑 데이터 값입니다.", HttpStatus.BAD_REQUEST);


                        purpose.getDetailPlans().get(goalAchieve.getId().intValue())
                                .setBriefingCount(goalAchieve.getBriefingCount())
                                .setLastBriefingDate(LocalDate.parse(goalAchieve.getLastBriefingDate(), DateTimeFormatter.ISO_DATE));
                    }
            );
        }

        Purpose purposed = purposeRepository.save(purpose);
        System.out.println(purposed.getAchieve());
    }

    public void delete(Long userId, Long purposeId) {
        Purpose purpose = purposeRepository.findById(purposeId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        if(!purpose.getUser().getId().equals(userId))
            throw new HttpRequestException("권한이 없습니다.", HttpStatus.UNAUTHORIZED);

        purposeRepository.deleteById(purposeId);
        s3Util.delete(makeFileName(purposeId));
    }

    public PageResult<?> readAll(Long userId, String prefix, Pageable pageable){

        Page<Purpose> result = purposeRepository.findByDisclosureScopeAndNameStartsWith(Scope.PUBLIC ,prefix == null ? "" : prefix , pageable);
        Stream<Purpose> pageableResultStream = result.get();

        return PageResult.of(pageable.getPageNumber() == result.getTotalPages() - 1,
                pageableResultStream.map(p -> {
                    User author = p.getUser();
                    return ResponsePurpose.builder()
                                .id(p.getId())
                                .name(p.getName())
                                .achieve(p.getAchieve())
                                .photoUrl(p.getPhotoUrl())
                                .endDate(p.getEndDate())
                                .stat(p.getStat().getValue())
                        .author(ResponseUser.builder()
                                .id(author.getId())
                                .name(author.getName())
                                .profileUrl(author.getProfileUrl())
                                .build())
                                .build();
                }).collect(Collectors.toList())
                );
    }


    private boolean isCanCheer(List<PurposeCheer> cheerList, Long targetId){
        boolean canLikes = true;

        for(PurposeCheer storyLikes : cheerList){
            if(storyLikes.getUserId().equals(targetId))
                canLikes = false;
        }
        return canLikes;
    }



    private Purpose changePurpose(Purpose purpose, PurposeResource resource) throws IOException {

        purpose.setName(resource.getName())
                .setDescription(resource.getDescription())
               .setStartDate(LocalDate.parse(resource.getStartDate(), DateTimeFormatter.ISO_DATE))
               .setEndDate(LocalDate.parse(resource.getEndDate(), DateTimeFormatter.ISO_DATE))
               .setDisclosureScope(Scope.findScope(resource.getDisclosureScope()))
               .setStat(Stat.findStat(resource.getStat()));

        if(resource.getPhoto() != null)
            purpose.setPhotoUrl(getPhotoUrl(purpose.getId(), resource.getPhoto()));

        return purpose;
    }

    private List<Goal> decodeDetailPlans(PurposeResource resource, Purpose purpose) throws JsonProcessingException {

        List<GoalResource> detailPlans = mapper.readValue(resource.getDetailPlans(),new TypeReference<List<GoalResource>>(){});

        List<Goal> goalList = detailPlans.stream()
                .map(g -> Goal.builder()
                        .key(Goal.Key.of(purpose.getId(), g.getId()))
                        .name(g.getName())
                        .description(g.getDescription())
                        .color(g.getColor())
                        .briefingCount(g.getBriefingCount())
                        .lastBriefingDate(g.getLastBriefingDate() == null ? null : LocalDate.parse(g.getLastBriefingDate(), DateTimeFormatter.ISO_DATE))
                        .startDate(LocalDate.parse(g.getStartDate(), DateTimeFormatter.ISO_DATE))
                        .endDate(LocalDate.parse(g.getEndDate(), DateTimeFormatter.ISO_DATE))
                        .cycle(g.getCycle())
                        .build()
                ).collect(Collectors.toList());

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
                .build();
    }
}
