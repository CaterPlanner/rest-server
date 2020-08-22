package com.downfall.caterplanner.user.service;

import com.downfall.caterplanner.application.exception.HttpRequestException;
import com.downfall.caterplanner.common.entity.User;
import com.downfall.caterplanner.common.repository.UserRepository;
import com.downfall.caterplanner.user.model.response.ResponseUser;
import com.downfall.caterplanner.user.model.request.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseUser read(Long id, boolean isMe){

        User user = userRepository.findById(id)
                .orElseThrow(() -> new HttpRequestException("존재하지 않는 사용자입니다.", isMe ? HttpStatus.BAD_REQUEST : HttpStatus.NOT_FOUND));

        return ResponseUser.builder()
                    .name(user.getName())
                    .joinDate(user.getCreateDate())
                    .profileUrl(user.getProfileUrl())
                    .backImageUrl(user.getBackImageUrl())
                    .purposeList(user.getPurposeList())
                    .build();
    }

    public ResponseUser update(Long id, UserResource resource){
        User user = userRepository.findById(id).orElseThrow(() -> new HttpRequestException("존재하지 않는 사용자입니다.", HttpStatus.BAD_REQUEST));

        user.setName(resource.getName());
        user.setProfileUrl(resource.getProfileUrl());
        user.setBackImageUrl(resource.getBackImageUrl());

        User updatedUser = userRepository.save(user);
        return ResponseUser.builder()
                    .name(updatedUser.getName())
                    .joinDate(updatedUser.getCreateDate())
                    .profileUrl(updatedUser.getProfileUrl())
                    .backImageUrl(updatedUser.getBackImageUrl())
                    .purposeList(updatedUser.getPurposeList())
                    .build();
    }
}
