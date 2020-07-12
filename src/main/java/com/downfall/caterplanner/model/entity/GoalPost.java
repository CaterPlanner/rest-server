package com.downfall.caterplanner.model.entity;

import com.downfall.caterplanner.model.converter.ScopeConverter;
import com.downfall.caterplanner.model.enumerate.Scope;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "GOAL_POST")
@Data
@Builder
public class GoalPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "GOAL_ID")
    private Goal goal;

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Column(name = "SCOPE", columnDefinition = "TINYINT", nullable = false)
    @Convert(converter = ScopeConverter.class)
    private Scope scope;

}
