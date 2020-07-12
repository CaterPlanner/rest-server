package com.downfall.caterplanner.model.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CATEGORY")
@Data
@Builder
public class Category extends BaseModel{

    @Column(name = "NAME", columnDefinition = "VARCHAR(32)", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR(256)", nullable = false)
    private String description;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "GROUP_CATEGORY",
            joinColumns = {@JoinColumn(name = "CATEGORY_ID")},
            inverseJoinColumns = {@JoinColumn(name = "GROUP_ID")}
    )
    private List<Group> groups;

}
