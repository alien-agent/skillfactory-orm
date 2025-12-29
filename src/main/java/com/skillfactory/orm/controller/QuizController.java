package com.skillfactory.orm.controller;

import com.skillfactory.orm.model.QuizSubmission;
import com.skillfactory.orm.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    // POST /api/quizzes/{quizId}/submit?studentId=1
    // Body: { "questionId1": "optionId1", "questionId2": "optionId2" }
    @PostMapping("/{quizId}/submit")
    public ResponseEntity<QuizSubmission> submitQuiz(@PathVariable Long quizId,
            @RequestParam Long studentId,
            @RequestBody Map<Long, Long> answers) {
        return ResponseEntity.ok(quizService.submitQuiz(quizId, studentId, answers));
    }
}
