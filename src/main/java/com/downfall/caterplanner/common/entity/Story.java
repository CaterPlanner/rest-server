package com.downfall.caterplanner.common.entity;

import com.downfall.caterplanner.common.entity.converter.StoryTypeConverter;
import com.downfall.caterplanner.common.entity.enumerate.StoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class Story extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Convert(converter = StoryTypeConverter.class)
    private StoryType type;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "purpose_id")
    private Purpose purpose;


    //multiPleBag 문제
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true, mappedBy = "story")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<StoryLikes> likes;

    @OneToMany(mappedBy = "story" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StoryComment> comments;


}

