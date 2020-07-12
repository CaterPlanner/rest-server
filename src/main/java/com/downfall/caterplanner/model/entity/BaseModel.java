package com.downfall.caterplanner.model.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @CreatedDate
    @Column(name = "CREATE_AT", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "UPDATE_AT", columnDefinition = "DATETIME")
    private LocalDateTime updateAt;

    @Column(name = "DELETE_AT", columnDefinition = "DATETIME")
    private LocalDateTime deleteAt;

    @Column(name = "DELETE_BY", columnDefinition = "DATETIME")
    private LocalDateTime delteBy;
}
