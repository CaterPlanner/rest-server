package com.downfall.caterplanner.model.entity;

import com.downfall.caterplanner.model.converter.BooleanToYNConverter;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DETAIL_PLAN")
@Data
@Builder
public class DetailPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "HEADER_ID")
    private DetailPlanHeader header;

    @Column(name = "CONSTRUCTOR_KEY", nullable = false)
    private int constructorKey;

    @Column(name = "CONSTRUCTOR_RELATION_TYPE", nullable = false)
    private char constructorRelationType;

    @Column(name = "NAME", columnDefinition = "VARCHAR(256)", nullable = false)
    private String name;

    @Column(name = "TYPE", columnDefinition = "CHAR(1)",nullable = false)
    private char type;

    @Column(name = "START_DATE",columnDefinition = "DATE", nullable = false)
    private LocalDate startDate;

    @Column(name = "END_DATE",columnDefinition = "DATE", nullable = false)
    private LocalDate endDate;

    @Column(name = "COLOR", columnDefinition = "CHAR(8)",nullable = false)
    private String color;

    @Column(name = "CYCLE", columnDefinition = "VARCHAR(3)",nullable = false)
    private String cycle;

    @Column(name = "STAT",columnDefinition = "CHAR(1)")
    private char stat;

    @Column(name = "CLEAR_CHECK",columnDefinition = "CHAR(1)", nullable = false)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean clearCheck;

}
