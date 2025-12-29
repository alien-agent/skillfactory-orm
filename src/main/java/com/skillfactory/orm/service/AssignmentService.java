package com.skillfactory.orm.service;

import com.skillfactory.orm.exception.EntityNotFoundException;
import com.skillfactory.orm.model.*;
import com.skillfactory.orm.repository.AssignmentRepository;
import com.skillfactory.orm.repository.LessonRepository;
import com.skillfactory.orm.repository.SubmissionRepository;
import com.skillfactory.orm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final LessonRepository lessonRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    @Transactional
    public Assignment createAssignment(Long lessonId, String title, String description, LocalDateTime dueDate) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found with id: " + lessonId));

        Assignment assignment = Assignment.builder()
                .lesson(lesson)
                .title(title)
                .description(description)
                .dueDate(dueDate)
                .build();

        return assignmentRepository.save(assignment);
    }

    @Transactional
    public Submission submitAssignment(Long assignmentId, Long studentId, String content) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with id: " + assignmentId));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));

        Submission submission = Submission.builder()
                .assignment(assignment)
                .student(student)
                .content(content)
                .submittedAt(LocalDateTime.now())
                .build();

        return submissionRepository.save(submission);
    }

    @Transactional(readOnly = true)
    public List<Submission> getSubmissionsForAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with id: " + assignmentId));

        // This might trigger lazy loading if we access fields, but returning entity is
        // fine for now
        // Be careful with serializing lazy collections in Controller
        return assignment.getSubmissions();
    }
}
