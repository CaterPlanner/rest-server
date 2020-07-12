package com.downfall.caterplanner.model.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PLAY_POST")
@Data
@Builder
public class PlayPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "GOAL_ID", nullable = true)
    private Goal goal;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID", nullable = true)
    private Group group;

    @Column(name = "SCOPE", columnDefinition = "CHAR(1)", nullable = false)
    private char scope;

    @Column(name = "TYPE", columnDefinition = "CHAR(1)", nullable = false)
    private char type;

    @Column(name = "EVENT_START_AT", columnDefinition = "DATETIME")
    private LocalDateTime eventStartAt;

    @Column(name = "EVENT_END_AT", columnDefinition = "DATETIME")
    private LocalDateTime eventEndAt;


}
