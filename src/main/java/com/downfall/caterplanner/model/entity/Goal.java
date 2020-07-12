package com.downfall.caterplanner.model.entity;

import com.downfall.caterplanner.model.converter.ScopeConverter;
import com.downfall.caterplanner.model.enumerate.Scope;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "GOAL")
@Data
@Builder
public class Goal extends BaseModel {

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "DETAIL_PLAN_HEADER_ID")
    private DetailPlanHeader detailPlanHeader;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "AUTHOR_ID")
    private User author;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @Column(name = "NAME", columnDefinition = "VARCHAR(32)", nullable = false)
    private String name;

    @Column(name = "SCOPE", columnDefinition = "TINYINT", nullable = false)
    @Convert(converter = ScopeConverter.class)
    private Scope scope;

    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR(256)", nullable = false)
    private String description;

    @Column(name = "IMAGE_URL", columnDefinition = "VARCHAR(64)", nullable = true)
    private String imageUrl;

    @Column(name = "DISCLOSURE_SCOPE", columnDefinition = "TINYINT", nullable = false)
    private int disclousreScope;

    @Column(name = "DECIMAL_DAY", columnDefinition = "DATE", nullable = false)
    private LocalDate decimalDate;

    @Column(name = "START_AT", columnDefinition = "DATETIME")
    private LocalDateTime startAt;

    @Column(name = "STOP_AT", columnDefinition = "DATETIME")
    private LocalDateTime stopAt;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "GOAL_CHEER",
            joinColumns = {@JoinColumn(name = "GOAL_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID")}
    )
    private List<User> cheerUsers;


}
