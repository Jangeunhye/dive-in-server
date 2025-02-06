package com.poten.dive_in.lesson.entity;

import com.poten.dive_in.academy.entity.Academy;
import com.poten.dive_in.common.entity.BaseTimeEntity;
import com.poten.dive_in.lesson.enums.LessonStatus;
import com.poten.dive_in.pool.entity.Pool;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Getter @Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
public class Lesson extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long id;

    private String lessonName;

    private String level;

    private String capacity;

    private String price;

    private String keyword;

    @Column(length = 3000)
    private String lessonDetail;

    private String lessonSchedule;

    @Enumerated(EnumType.STRING)
    private LessonStatus lessonStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academy_id")
    private Academy academy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pool_id")
    private Pool pool;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonImage> imageList;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonInstructor> lessonInstructorList;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonApplyChannel> applyChannelList;


    public void addImage(List<LessonImage> lessonImageList) {
        this.imageList = lessonImageList;
    }

    public void assignAcademy(Academy academy){
        this.academy = academy;
    }

    public void assignPool(Pool pool){
        this.pool = pool;
    }

    public void assignInstructors(List<LessonInstructor> lessonInstructorList) {
        this.lessonInstructorList.clear();
        this.lessonInstructorList.addAll(lessonInstructorList);
    }

    public void addImages(List<LessonImage> lessonImageList) {
        this.imageList.clear();
        this.imageList.addAll(lessonImageList);
    }

    public void assignApplyChannels(List<LessonApplyChannel> applyChannels) {
        this.applyChannelList.clear();
        this.applyChannelList.addAll(applyChannels);
    }



}
