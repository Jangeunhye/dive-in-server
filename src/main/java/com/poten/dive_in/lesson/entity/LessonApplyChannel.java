package com.poten.dive_in.lesson.entity;

import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.lesson.enums.LessonChannelType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class LessonApplyChannel extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_apply_channel_id")
    private Long id;

    private String applyUrl;

    @Enumerated(EnumType.STRING)
    private LessonChannelType applyUrlType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
}
