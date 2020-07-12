package com.downfall.caterplanner.model.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "POSTS")
@Data
@Builder
public class Post {

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "AUTHOR_ID")
    private User author;

    @Column(name = "TITLE", columnDefinition = "VARCHAR(32)", nullable = false)
    private String title;

    @Column(name = "CONTENT", columnDefinition = "VARCHAR(256)", nullable = false)
    private String content;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private List<Comment> comments;

    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "POST_LIKE",
            joinColumns = {@JoinColumn(name = "POST_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID")}
    )
    private List<User> likeUsers;

}
