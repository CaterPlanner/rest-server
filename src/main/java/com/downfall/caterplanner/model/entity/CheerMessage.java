package com.downfall.caterplanner.model.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "CHEER_MESSAGE")
@Data
@Builder
public class CheerMessage extends BaseModel {

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "AUTHOR_ID")
    private User author;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "GOAL_ID")
    private Goal goal;

    @Column(name = "CONTENT", columnDefinition = "VARCHAR(256)", nullable = false)
    private String content;

}
