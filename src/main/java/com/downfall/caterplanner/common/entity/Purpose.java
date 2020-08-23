package com.downfall.caterplanner.common.entity;

import com.downfall.caterplanner.common.entity.converter.ScopeConverter;
import com.downfall.caterplanner.common.entity.enumerate.Scope;
import com.downfall.caterplanner.common.entity.enumerate.Stat;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class Purpose extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "photo_url" , nullable = false)
    private String photoUrl;

    @Convert(converter = ScopeConverter.class)
    @Column(name ="disclosure_scope",  nullable = false)
    private Scope disclosureScope;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Convert(converter =  ScopeConverter.class)
    @Column(nullable = false)
    private Stat stat;

    @Column(columnDefinition = "integer default 0")
    private int achieve;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true, mappedBy = "purpose")
    private List<Goal> detailPlans;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true, mappedBy = "purpose")
    private List<Story> stories;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true, mappedBy = "purpose")
    private List<PurposeCheer> cheers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true, mappedBy = "purpose")
    private List<PurposeComment> comments;

}
