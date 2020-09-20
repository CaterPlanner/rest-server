package com.dawnfall.caterplanner.purpose.model.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Data
public class PurposeAchieve {

    @Min(0)
    @Max(100)
    private Integer achieve;

    @Min(0)
    @Max(3)
    private Integer stat;

    @Valid
    private List<GoalAchieve> modifiedGoalAchieve;

}


