package com.skillfactory.orm.controller;

import com.skillfactory.orm.model.Course;
import com.skillfactory.orm.model.Enrollment;
import com.skillfactory.orm.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestParam String title,
            @RequestParam String description,
            @RequestParam Long teacherId) {
        return ResponseEntity.ok(courseService.createCourse(title, description, teacherId));
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<Enrollment> enrollStudent(@PathVariable Long courseId, @RequestParam Long studentId) {
        return ResponseEntity.ok(courseService.enrollStudent(courseId, studentId));
    }
}
