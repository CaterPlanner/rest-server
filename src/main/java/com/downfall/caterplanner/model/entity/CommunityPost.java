package com.downfall.caterplanner.model.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COMMUNITY_POST")
@Data
@Builder
public class CommunityPost {

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
    @JoinColumn(name = "GROUP_ID")
    private Group group;

//    @Column(name = "TYPE", columnDefinition = "CHAR(1)", nullable = false)
//    private char type;


    @Column(name = "EVENT_END_AT", columnDefinition = "DATETIME")
    private LocalDateTime eventEndAt;
}
