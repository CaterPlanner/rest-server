package com.downfall.caterplanner.common.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "purpose_cheer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class PurposeCheer {

    @EmbeddedId
    private Key key;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",insertable=false, updatable=false)
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "purpose_id",insertable=false, updatable=false)
    private Purpose purpose;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    public static class Key implements Serializable {
        @Column(name = "purpose_id")
        private Long purposeId;
        @Column(name = "user_id")
        private Long userId;
    }
}
