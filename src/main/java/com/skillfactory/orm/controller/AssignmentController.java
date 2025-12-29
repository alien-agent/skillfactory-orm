package com.skillfactory.orm.controller;

import com.skillfactory.orm.model.Assignment;
import com.skillfactory.orm.model.Submission;
import com.skillfactory.orm.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@RequestParam Long lessonId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDate) {
        return ResponseEntity.ok(assignmentService.createAssignment(lessonId, title, description, dueDate));
    }

    @PostMapping("/{assignmentId}/submit")
    public ResponseEntity<Submission> submitAssignment(@PathVariable Long assignmentId,
            @RequestParam Long studentId,
            @RequestParam String content) {
        return ResponseEntity.ok(assignmentService.submitAssignment(assignmentId, studentId, content));
    }

    @GetMapping("/{assignmentId}/submissions")
    public ResponseEntity<List<Submission>> getSubmissions(@PathVariable Long assignmentId) {
        return ResponseEntity.ok(assignmentService.getSubmissionsForAssignment(assignmentId));
    }
}
