package com.skillfactory.orm.service;

import com.skillfactory.orm.model.Course;
import com.skillfactory.orm.model.Enrollment;
import com.skillfactory.orm.model.User;
import com.skillfactory.orm.model.enums.Role;
import com.skillfactory.orm.repository.CourseRepository;
import com.skillfactory.orm.repository.EnrollmentRepository;
import com.skillfactory.orm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional
    public Course createCourse(String title, String description, Long teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new com.skillfactory.orm.exception.EntityNotFoundException(
                        "Teacher not found with id: " + teacherId));

        if (teacher.getRole() != Role.TEACHER && teacher.getRole() != Role.ADMIN) {
            throw new RuntimeException("User is not a teacher");
        }

        Course course = Course.builder()
                .title(title)
                .description(description)
                .teacher(teacher)
                .build();

        return courseRepository.save(course);
    }

    @Transactional
    public Enrollment enrollStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new com.skillfactory.orm.exception.EntityNotFoundException(
                        "Course not found with id: " + courseId));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new com.skillfactory.orm.exception.EntityNotFoundException(
                        "Student not found with id: " + studentId));

        if (enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).isPresent()) {
            throw new RuntimeException("Student already enrolled");
        }

        Enrollment enrollment = Enrollment.builder()
                .course(course)
                .student(student)
                .enrollDate(LocalDateTime.now())
                .build();

        return enrollmentRepository.save(enrollment);
    }

    @Transactional(readOnly = true)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
