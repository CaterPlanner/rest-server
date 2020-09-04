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
@IdClass(StoryLikes.Key.class)
@Accessors(chain = true)
public class StoryLikes {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "story_id")
    private Long storyId;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",insertable=false, updatable=false)
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id",insertable=false, updatable=false)
    private Story story;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    public static class Key implements Serializable {
        private Long storyId;
        private Long userId;
    }
}
