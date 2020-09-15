package com.downfall.caterplanner.user.service;

import com.downfall.caterplanner.application.exception.HttpRequestException;
import com.downfall.caterplanner.common.entity.Purpose;
import com.downfall.caterplanner.common.entity.User;
import com.downfall.caterplanner.common.entity.enumerate.Stat;
import com.downfall.caterplanner.common.repository.PurposeRepository;
import com.downfall.caterplanner.common.repository.UserRepository;
import com.downfall.caterplanner.purpose.model.response.ResponseGoal;
import com.downfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.downfall.caterplanner.user.model.response.ResponseUser;
import com.downfall.caterplanner.user.model.request.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurposeRepository purposeRepository;

    public ResponseUser read(Long userId, Long id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new HttpRequestException("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND));

        List<Purpose> purposeList = user.getPurposeList();

        int successCount = 0;

        for(Purpose purpose : purposeList){
            if(purpose.getStat() == Stat.SUCCESS)
                successCount++;
        }

        int successPer = successCount == 0 ? 0 : Math.round(((float)successCount / purposeList.size()) * 100);

        return ResponseUser.builder()
                    .isOwner(userId.equals(id))
                    .id(user.getId())
                    .name(user.getName())
                    .joinDate(user.getCreatedDate())
                    .profileUrl(user.getProfileUrl())
                    .backImageUrl(user.getBackImageUrl())
                    .successCount(successCount)
                    .successPer(successPer)
                    .purposeList(
                            purposeList.stream().filter(p -> p.getStat() == Stat.PROCEED)
                                    .map(p -> ResponsePurpose.builder()
                                                    .name(p.getName())
                                                    .id(p.getId())
                                                    .photoUrl(p.getPhotoUrl())
                                                    .stat(p.getStat().getValue())
                                                    .build()).collect(Collectors.toList()))
                    .build();
    }

    public List<ResponsePurpose> getUserPurposes(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new HttpRequestException("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND));

        return user.getPurposeList().stream()
                .map(p -> ResponsePurpose.builder()
                        .name(p.getName())
                        .id(p.getId())
                        .photoUrl(p.getPhotoUrl())
                        .stat(p.getStat().getValue())
                        .build()).collect(Collectors.toList());
    }

    public ResponseUser update(Long id, UserResource resource){
        User user = userRepository.findById(id).orElseThrow(() -> new HttpRequestException("존재하지 않는 사용자입니다.", HttpStatus.BAD_REQUEST));

        user.setName(resource.getName());
        user.setProfileUrl(resource.getProfileUrl());
        user.setBackImageUrl(resource.getBackImageUrl());

        User updatedUser = userRepository.save(user);

        return ResponseUser.builder()
                    .isOwner(true)
                    .name(updatedUser.getName())
                    .profileUrl(updatedUser.getProfileUrl())
                    .backImageUrl(updatedUser.getBackImageUrl())
                    .build();
    }

    public List<ResponsePurpose> getActivePurposes(Long myId) {
        User user = userRepository.findById(myId).orElseThrow(() -> new HttpRequestException("존재하지 않는 사용자입니다.", HttpStatus.BAD_REQUEST));


        return purposeRepository.findByUserIdAndStatIn(user.getId(), Arrays.asList(Stat.PROCEED, Stat.WAIT)).stream().map(
                p -> ResponsePurpose.defaultBuilder(p)
                        .isOwner(true)
                        .achieve(p.getAchieve())
                        .detailPlans(p.getDetailPlans().stream()
                                .map(goal -> ResponseGoal.builder()
                                                .id(goal.getKey().getGoalId())
                                                .purposeId(goal.getKey().getPurposeId())
                                                .briefingCount(goal.getBriefingCount())
                                                .lastBriefingDate(goal.getLastBriefingDate())
                                                .color(goal.getColor())
                                                .cycle(goal.getCycle())
                                                .description(goal.getDescription())
                                                .name(goal.getName())
                                                .startDate(goal.getStartDate())
                                                .endDate(goal.getEndDate())
                                                .build()).collect(Collectors.toList()))
                        .build()
        ).collect(Collectors.toList());
    }
}
