package com.downfall.caterplanner.purpose.model.request;

import com.downfall.caterplanner.purpose.model.request.GoalResource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PurposeResource implements Serializable {

    @NotBlank(message = "이름은 비어있으면 안됩니다.")
    private String name;

    @NotEmpty(message = "설명글은 필수입니다.")
    private String description;

    private MultipartFile photo;

    @NotNull(message = "공개범위는 필수입니다.")
    private int disclosureScope;

    private String startDate;

    private String endDate;

    @Min(0)
    @Max(3)
    @NotNull(message = "상태값은 필수입니다.")
    private int stat;

    private Integer achieve;

    private String detailPlans;

}


