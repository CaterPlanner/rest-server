package com.downfall.caterplanner.purpose.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class GoalResource implements Serializable {

    private Long purposeId;

    private Integer id;

    private String name;

    private String description;

    private String startDate;

    private String endDate;

    private String color;

    private String cycle;

    private Integer briefingCount;

    private String lastBriefingDate;

    private Integer stat;

}
