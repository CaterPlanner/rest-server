package com.downfall.caterplanner.model.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "GROUP")
@Data
@Builder
public class Group extends BaseModel {

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "OWNER_ID")
    private User owner;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "GROUP_CATEGORY",
            joinColumns = {@JoinColumn(name = "GROUP_ID")},
            inverseJoinColumns = {@JoinColumn(name = "CATEGORY_ID")}
    )
    private List<Category> categories;

    @Column(name = "NAME", columnDefinition = "VARCHAR(32)", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR(256)", nullable = false)
    private String description;

    @Column(name = "IMAGE_URL", columnDefinition = "VARCHAR(64)")
    private String imageUrl;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    private List<Goal> goals;

}
