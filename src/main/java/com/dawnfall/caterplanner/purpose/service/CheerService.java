package com.dawnfall.caterplanner.purpose.service;

import com.dawnfall.caterplanner.application.exception.HttpRequestException;
import com.dawnfall.caterplanner.common.entity.Purpose;
import com.dawnfall.caterplanner.common.entity.PurposeCheer;
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

    public void create(Long userId, Long purposeId) {
        Purpose purpose = purposeRepository.findById(purposeId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));

        if(purposeCheerRepository.findById(PurposeCheer.Key.of(purposeId, userId)).isPresent())
            throw new HttpRequestException("이미 응원하셨습니다.", HttpStatus.NOT_ACCEPTABLE);

        purposeCheerRepository.save(
                PurposeCheer.builder()
                    .purposeId(purposeId)
                    .userId(userId)
                    .build()
        );
    }

    public void delete(Long userId, Long purposeId) {
        Purpose purpose = purposeRepository.findById(purposeId).orElseThrow(() -> new HttpRequestException("존재하지 않는 목적입니다.", HttpStatus.NOT_FOUND));
        purposeCheerRepository.deleteByPK(purposeId, userId);
    }
}