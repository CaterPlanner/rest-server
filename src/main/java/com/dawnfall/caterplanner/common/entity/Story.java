package com.dawnfall.caterplanner.common.entity;

import com.dawnfall.caterplanner.common.entity.converter.ScopeConverter;
import com.dawnfall.caterplanner.common.entity.converter.StoryTypeConverter;
import com.dawnfall.caterplanner.common.entity.enumerate.Scope;
import com.dawnfall.caterplanner.common.entity.enumerate.StoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
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
    private StoryType type; //0 vLog 1 도움ㅇ청

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "purpose_id")
    private Purpose purpose;

    @Convert(converter = ScopeConverter.class)
    @Column(name ="disclosure_scope",  nullable = false)
    private Scope disclosureScope;

    //multiPleBag 문제
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true, mappedBy = "story")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<StoryLikes> likes;

    @OneToMany(mappedBy = "story" , cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StoryComment> comments;


}

/*
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 01', 'content 01', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 02', 'content 02', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 03', 'content 03', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 04', 'content 04', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 05', 'content 05', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 06', 'content 06', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 07', 'content 07', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 08', 'content 08', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 09', 'content 09', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 10', 'content 10', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 11', 'content 11', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 12', 'content 12', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 13', 'content 13', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 14', 'content 14', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 15', 'content 15', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 16', 'content 16', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 17', 'content 17', 0, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 18', 'content 18', 1, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 19', 'content 19', 1, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 20', 'content 20', 1, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 21', 'content 21', 1, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 22', 'content 22', 1, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 23', 'content 23', 1, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 24', 'content 24', 1, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 25', 'content 25', 1, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 26', 'content 26', 1, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 27', 'content 27', 1, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 28', 'content 28', 1, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 29', 'content 29', 1, 30, NOW());
INSERT INTO story (title, content, type, purpose_id, create_date) VALUES ('title 30', 'content 30', 1, 30, NOW());



 */
