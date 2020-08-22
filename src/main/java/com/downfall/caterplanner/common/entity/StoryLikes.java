package com.downfall.caterplanner.common.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "story_likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class StoryLikes {

    @EmbeddedId
    private Key key;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",insertable=false, updatable=false)
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id",insertable=false, updatable=false)
    private Story story;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    public static class Key implements Serializable {
        @Column(name = "story_id")
        private Long storyId;
        @Column(name = "user_id")
        private Long userId;
    }
}
