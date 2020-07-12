package com.downfall.caterplanner.model.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DETAIL_PLAN_HEADER")
@Data
@Builder
public class DetailPlanHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "AUTHOR_ID")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "BASE_ID", columnDefinition = "INT")
    private DetailPlanHeader base;

    @CreatedDate
    @Column(name = "CREATE_AT", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "UPDATE_AT", columnDefinition = "DATETIME")
    private LocalDateTime updateAt;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "HEADER_ID")
    private List<DetailPlan> detailPlans;
}
