package com.downfall.caterplanner.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "BRIEFING")
@Data
@Builder
public class Briefing {

    @EmbeddedId
    private Key key;

    @CreatedDate
    @Column(name = "CREATE_AT", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "SCORE", columnDefinition = "CHAR(1)", nullable = false)
    private char score;

    @Embeddable
    class Key implements Serializable {

        @Column(name = "HEADER_ID")
        private Long commentId;

        @Column(name = "DETAIL_PLAN_KEY")
        private Long detailPlanKey;
    }

}
