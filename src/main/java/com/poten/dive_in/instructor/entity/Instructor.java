package com.poten.dive_in.instructor.entity;

import com.poten.dive_in.academy.entity.Academy;
import com.poten.dive_in.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Instructor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_id")
    private Long id;

    private String instructorName;

    private String instructorInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academy_id" )
    private Academy academy;
}
