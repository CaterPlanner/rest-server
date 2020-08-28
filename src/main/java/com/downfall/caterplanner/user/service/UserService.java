package com.downfall.caterplanner.user.service;

import com.downfall.caterplanner.application.exception.HttpRequestException;
import com.downfall.caterplanner.common.entity.User;
import com.downfall.caterplanner.common.repository.UserRepository;
import com.downfall.caterplanner.purpose.model.request.PurposeResource;
import com.downfall.caterplanner.purpose.model.response.ResponseGoal;
import com.downfall.caterplanner.purpose.model.response.ResponsePurpose;
import com.downfall.caterplanner.user.model.response.ResponseUser;
import com.downfall.caterplanner.user.model.request.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public ResponseUser read(Long userId, Long id){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new HttpRequestException("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND));

        return ResponseUser.builder()
                    .isOwner(userId.equals(id))
                    .name(user.getName())
                    .joinDate(user.getCreateDate())
                    .profileUrl(user.getProfileUrl())
                    .backImageUrl(user.getBackImageUrl())
                    .purposeList(
                            user.getPurposeList().stream()
                                    .map(p -> ResponsePurpose.builder()
                                                    .name(p.getName())
                                                    .id(p.getId())
                                                    .photoUrl(p.getPhotoUrl())
                                                    .stat(p.getStat().getValue())
                                                    .build()).collect(Collectors.toList()))
                    .build();
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

    public List<ResponsePurpose> getHasPurposes(Long myId) {
        User user = userRepository.findById(myId).orElseThrow(() -> new HttpRequestException("존재하지 않는 사용자입니다.", HttpStatus.BAD_REQUEST));

        return user.getPurposeList().stream().map(
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
                                                .stat(goal.getStat().getValue())
                                                .startDate(goal.getStartDate())
                                                .endDate(goal.getEndDate())
                                                .build()).collect(Collectors.toList()))
                        .build()
        ).collect(Collectors.toList());
    }
}
