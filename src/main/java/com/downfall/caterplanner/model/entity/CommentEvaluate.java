package com.downfall.caterplanner.model.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "COMMENT_EVALUATE")
@Data
@Builder
public class CommentEvaluate {

    @EmbeddedId
    private Key key;

    //보류

    @Embeddable
    class Key implements Serializable {

        @Column(name = "COMMENT_ID")
        private Long commentId;

        @Column(name = "USER_ID")
        private Long userId;
    }
}
