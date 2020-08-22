package com.downfall.caterplanner.purpose.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class GoalResource {

    private Long purposeId;

    @NotNull(message = "아이디 값은 필수입니다.")
    private int id;

    @NotBlank(message = "이름은 비어있으면 안됩니다.")
    private String name;

    @NotEmpty(message = "설명글은 필수입니다.")
    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotBlank(message = "색깔값은 비어있으면 안됩니다.")
    private String color;

    @NotBlank(message = "사이클 값은 비어있으면 안됩니다.")
    private String cycle;

    @Min(0)
    @Max(3)
    @NotNull(message = "상태값은 필수입니다.")
    private int stat;

}
