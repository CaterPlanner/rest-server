package com.downfall.caterplanner.purpose.model.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class GoalAchieve {

    @NotNull(message = "아이디는 필수입니다.")
    private Long id;

    @Min(0)
    @Max(100)
    @NotNull(message = "달성도 수치는 필수입니다.")
    private int achieve;

    @Min(1)
    @Max(3)
    private int stat;

}
