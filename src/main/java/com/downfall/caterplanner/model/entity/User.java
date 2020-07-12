package com.downfall.caterplanner.model.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@Data
@Builder
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "SOCIAL_ID")
    private String socialId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PICTURE_URL")
    private String pictureUrl;

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;


}
