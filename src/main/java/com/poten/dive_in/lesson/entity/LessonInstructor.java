package com.poten.dive_in.lesson.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.instructor.entity.Instructor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class LessonInstructor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="lesson_instructor_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="instructor_id")
    private Instructor instructor;

}
