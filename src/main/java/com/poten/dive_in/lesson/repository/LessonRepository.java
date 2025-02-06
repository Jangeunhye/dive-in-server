package com.poten.dive_in.lesson.repository;

import com.poten.dive_in.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson,Long> {

    @Query("select l from Lesson l left join fetch l.academy left join fetch l.pool")
    List<Lesson> findAllJPQLFetch();

}
