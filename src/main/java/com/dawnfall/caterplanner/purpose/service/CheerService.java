package com.dawnfall.caterplanner.purpose.service;

import com.dawnfall.caterplanner.application.exception.HttpRequestException;
import com.dawnfall.caterplanner.common.ErrorCode;
import com.dawnfall.caterplanner.common.entity.Purpose;
import com.dawnfall.caterplanner.common.entity.PurposeCheer;
import com.dawnfall.caterplanner.common.model.network.ErrorInfo;
import com.dawnfall.caterplanner.common.model.network.Response;
import com.dawnfall.caterplanner.common.repository.PurposeCheerRepository;
import com.dawnfall.caterplanner.common.repository.PurposeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CheerService {

    @Autowired
    private PurposeCheerRepository purposeCheerRepository;

    @Autowired
    private PurposeRepository purposeRepository;

    public Response create(Long userId, Long purposeId) {
        Purpose purpose = purposeRepository.findById(purposeId).orElseThrow(
                () -> new HttpRequestException("목적 응원 실패", new ErrorInfo(ErrorCode.NOT_EXISTED, "존재하지 않는 목적입니다.")));

        if(purposeCheerRepository.findById(PurposeCheer.Key.of(purposeId, userId)).isPresent())
            throw new HttpRequestException("목적 응원 실패", new ErrorInfo(ErrorCode.ALREADY, "이미 응원하셨습니다."));

        purposeCheerRepository.save(
                PurposeCheer.builder()
                    .purposeId(purposeId)
                    .userId(userId)
                    .build()
        );
        return new Response("목적 응원 성공");
    }

    public Response delete(Long userId, Long purposeId) {
        Purpose purpose = purposeRepository.findById(purposeId).orElseThrow(
                () -> new HttpRequestException("목적 응원 취소 실패", new ErrorInfo(ErrorCode.NOT_EXISTED,"존재하지 않는 목적입니다.")));
        purposeCheerRepository.deleteByPK(purposeId, userId);
        return new Response("목적 응원 취소 성공");
    }
}
