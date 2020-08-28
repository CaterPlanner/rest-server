package com.downfall.caterplanner.common.entity;


import com.downfall.caterplanner.common.entity.converter.StatConverter;
import com.downfall.caterplanner.common.entity.enumerate.Stat;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class Goal extends BaseModel{

    @EmbeddedId
    private Key key;

    @ManyToOne(cascade = CascadeType.PERSIST,  fetch = FetchType.EAGER)
    @JoinColumn(name ="purpose_id",insertable=false, updatable=false)
    private Purpose purpose;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "start_date" , nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String cycle;

    @Column(nullable = false)
    @Convert(converter = StatConverter.class)
    private Stat stat;

    @Column(name = "briefingCount", columnDefinition = "integer default 0")
    private int briefingCount;

    @Column(name = "last_briefing_date")
    private LocalDate lastBriefingDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    @Embeddable
    public static class Key implements  Serializable{

        @Column(name = "purpose_id")
        private Long purposeId;

        @Column(name = "goal_id")
        private Integer goalId;
    }

}
