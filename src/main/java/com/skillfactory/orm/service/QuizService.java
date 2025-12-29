package com.skillfactory.orm.service;

import com.skillfactory.orm.exception.EntityNotFoundException;
import com.skillfactory.orm.model.*;
import com.skillfactory.orm.repository.AnswerOptionRepository;
import com.skillfactory.orm.repository.QuizRepository;
import com.skillfactory.orm.repository.QuizSubmissionRepository;
import com.skillfactory.orm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizSubmissionRepository quizSubmissionRepository;
    private final UserRepository userRepository;
    private final AnswerOptionRepository answerOptionRepository;

    @Transactional
    public QuizSubmission submitQuiz(Long quizId, Long studentId, Map<Long, Long> answers) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found with id: " + quizId));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));

        int correctCount = 0;
        int totalQuestions = quiz.getQuestions().size();

        // Calculate score
        for (Map.Entry<Long, Long> entry : answers.entrySet()) {
            // Long questionId = entry.getKey(); // Not strictly needed to check if we trust
            // the option linkage
            Long selectedOptionId = entry.getValue();

            boolean isCorrect = answerOptionRepository.findById(selectedOptionId)
                    .map(AnswerOption::isCorrect)
                    .orElse(false);

            if (isCorrect) {
                correctCount++;
            }
        }

        // Avoid division by zero
        double score = totalQuestions > 0 ? ((double) correctCount / totalQuestions) * 100.0 : 0.0;

        QuizSubmission submission = QuizSubmission.builder()
                .quiz(quiz)
                .student(student)
                .score((int) score)
                .takenAt(LocalDateTime.now())
                .build();

        return quizSubmissionRepository.save(submission);
    }
}
