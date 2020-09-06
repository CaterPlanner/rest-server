package com.downfall.caterplanner.purpose.model.request;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class GoalAchieve {

    @NotNull(message = "아이디는 필수입니다.")
    private Long id;

    @NotNull(message = "브리핑 갯수는 필수입니다.")
    private Integer briefingCount;

    @NotNull(message = "마지막 브리핑 날짜는 필수입니다.")
    private String lastBriefingDate;


}
