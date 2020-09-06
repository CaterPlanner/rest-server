package com.downfall.caterplanner.purpose.model.request;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PurposeAchieve {

    @Min(0)
    @Max(100)
    private int achieve;

    @Min(0)
    @Max(3)
    private int stat;

    @Valid
    private List<GoalAchieve> modifiedGoalAchieve;

}


