package com.downfall.caterplanner.purpose.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseGoal {

    private Long purposeId;

    private Integer id;

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private String color;

    private String cycle;

    private Integer briefingCount;

    private LocalDate lastBriefingDate;

    private Integer stat;

}
