package com.poten.dive_in.lesson.service;

import com.poten.dive_in.academy.entity.Academy;
import com.poten.dive_in.academy.repository.AcademyRepository;
import com.poten.dive_in.common.service.S3Service;
import com.poten.dive_in.instructor.entity.Instructor;
import com.poten.dive_in.instructor.repository.InstructorRepository;
import com.poten.dive_in.lesson.dto.LessonListResponseDto;
import com.poten.dive_in.lesson.dto.LessonRequestDto;
import com.poten.dive_in.lesson.dto.LessonDetailResponseDto;
import com.poten.dive_in.lesson.entity.Lesson;
import com.poten.dive_in.lesson.entity.LessonApplyChannel;
import com.poten.dive_in.lesson.entity.LessonImage;
import com.poten.dive_in.lesson.entity.LessonInstructor;
import com.poten.dive_in.lesson.repository.LessonRepository;
import com.poten.dive_in.pool.entity.Pool;
import com.poten.dive_in.pool.repository.PoolRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.poten.dive_in.common.service.S3Service.extractFileName;


@RequiredArgsConstructor
@Service
public class LessonService {

    private final S3Service s3Service;

    private final LessonRepository lessonRepository;
    private final InstructorRepository instructorRepository;
    private final AcademyRepository academyRepository;
    private final PoolRepository poolRepository;


    @Transactional(readOnly = true)
    public List<LessonListResponseDto> getLessonList(){
        List<Lesson> lessonList = lessonRepository.findAllJPQLFetch();
        return lessonList.stream().map(LessonListResponseDto::ofEntity).toList();
    }

    @Transactional(readOnly = true)
    public LessonDetailResponseDto getLessonDetail(Long lessonId){
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(()->new EntityNotFoundException("존재하지 않는 수업입니다."));
        return LessonDetailResponseDto.ofEntity(lesson);
    }


    @Transactional
    public LessonDetailResponseDto createLesson(LessonRequestDto lessonRequestDto, List<MultipartFile> multipartFileList){

        // Academy 존재 여부 확인
        Academy academy = lessonRequestDto.getAcademyId() != null ?
                academyRepository.findById(lessonRequestDto.getAcademyId())
                        .orElseThrow(() -> new EntityNotFoundException("등록되지 않은 업체입니다.")) : null;

        // Pool 존재 여부 확인
        Pool pool = lessonRequestDto.getPoolId() != null ?
                poolRepository.findById(lessonRequestDto.getPoolId())
                        .orElseThrow(() -> new EntityNotFoundException("등록되지 않은 수영장입니다.")) : null;


        // Instructor 목록 확인
        List<Instructor> instructors = new ArrayList<>();
        if (lessonRequestDto.getInstructorIdList() != null && !lessonRequestDto.getInstructorIdList().isEmpty()) {
            instructors = instructorRepository.findAllById(lessonRequestDto.getInstructorIdList());
            if (instructors.isEmpty()) {
                throw new EntityNotFoundException("등록되지 않은 강사가 존재합니다.");
            }
        }

        // Lesson 엔티티 생성
        Lesson lesson = lessonRequestDto.toEntity();


        if (academy != null) lesson.assignAcademy(academy);

        if (!instructors.isEmpty()) {
            List<LessonInstructor> lessonInstructorList = new ArrayList<>();
            for (Instructor instructor : instructors) {
                LessonInstructor lessonInstructor = LessonInstructor.builder()
                        .lesson(lesson)
                        .instructor(instructor)
                        .build();
                lessonInstructorList.add(lessonInstructor);
            }
            lesson.assignInstructors(lessonInstructorList);  // Instructor 설정
        }

        // Apply Channel 설정 (있을 때만 처리)
        if (lessonRequestDto.getApplyChannelDtoList() != null && !lessonRequestDto.getApplyChannelDtoList().isEmpty()) {
            List<LessonApplyChannel> applyChannels = lessonRequestDto.getApplyChannelDtoList().stream()
                    .map(dto -> dto.toEntity(lesson))
                    .collect(Collectors.toList());
            lesson.assignApplyChannels(applyChannels);
        }


        // 이미지가 있는 경우만
        if (multipartFileList != null && !multipartFileList.isEmpty()){
            List<LessonImage> lessonImageList = uploadAndCreateLessonImages(multipartFileList,lesson);
            lesson.addImage(lessonImageList);
        }

        if (pool != null) {
            lesson.assignPool(pool);
            pool.addLesson(lesson);
        }

        lessonRepository.save(lesson);

        return LessonDetailResponseDto.ofEntity(lesson);

    }


    // Lesson 리스트 생성 및 S3 업로드 처리
    private List<LessonImage> uploadAndCreateLessonImages(List<MultipartFile> multipartFileList, Lesson lesson) {
        List<LessonImage> lessonImageList = new ArrayList<>();
        List<String> uploadFileList = s3Service.uploadFile(multipartFileList);

        for (int i = 0; i < uploadFileList.size(); i++) {

            LessonImage lessonImage = LessonImage.builder()
                    .imageUrl(uploadFileList.get(i))
                    .lesson(lesson)
                    .build();
            lessonImageList.add(lessonImage);
        }

        return lessonImageList;
    }

    // S3에 저장된 이미지 삭제 처리
    private void deleteLessonImagesFromS3(List<LessonImage> lessonImageList) {
        if (lessonImageList != null && !lessonImageList.isEmpty()) {
            lessonImageList.forEach(lessonImage -> {
                String fileName = extractFileName(lessonImage.getImageUrl());
                s3Service.deleteFile(fileName);
            });
        }
    }
}
