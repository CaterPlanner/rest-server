package com.downfall.caterplanner.model.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
@Data
@Builder
public class Banners {

    @Id
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User user;

    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR(256)", nullable = false)
    private String description;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String punisher;

    //report_id
}
