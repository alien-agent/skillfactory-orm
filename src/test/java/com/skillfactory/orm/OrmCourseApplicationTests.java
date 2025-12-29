package com.skillfactory.orm;

import com.skillfactory.orm.model.Course;
import com.skillfactory.orm.model.Enrollment;
import com.skillfactory.orm.model.User;
import com.skillfactory.orm.model.enums.Role;
import com.skillfactory.orm.repository.UserRepository;
import com.skillfactory.orm.service.CourseService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class OrmCourseApplicationTests {

        @Container
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
                        .withDatabaseName("testdb")
                        .withUsername("test")
                        .withPassword("test");

        @DynamicPropertySource
        static void configureProperties(DynamicPropertyRegistry registry) {
                registry.add("spring.datasource.url", postgres::getJdbcUrl);
                registry.add("spring.datasource.username", postgres::getUsername);
                registry.add("spring.datasource.password", postgres::getPassword);
        }

        @Autowired
        private CourseService courseService;

        @Autowired
        private UserRepository userRepository;

        @Test
        void contextLoads() {
                assertThat(postgres.isRunning()).isTrue();
        }

        @Test
        void shouldCreateCourseAndEnrollStudent() {
                // 1. Create Users
                User teacher = User.builder()
                                .name("Prof. Dumbledore")
                                .email("albus@hogwarts.edu")
                                .role(Role.TEACHER)
                                .build();
                userRepository.save(teacher);

                User student = User.builder()
                                .name("Harry Potter")
                                .email("harry@hogwarts.edu")
                                .role(Role.STUDENT)
                                .build();
                userRepository.save(student);

                // 2. Create Course
                Course course = courseService.createCourse("Defense Against the Dark Arts", "Practical tasks",
                                teacher.getId());

                assertThat(course.getId()).isNotNull();
                assertThat(course.getTitle()).isEqualTo("Defense Against the Dark Arts");
                assertThat(course.getTeacher().getId()).isEqualTo(teacher.getId());

                // 3. Enroll Student
                Enrollment enrollment = courseService.enrollStudent(course.getId(), student.getId());

                assertThat(enrollment.getId()).isNotNull();
                assertThat(enrollment.getStudent().getId()).isEqualTo(student.getId());
                assertThat(enrollment.getCourse().getId()).isEqualTo(course.getId());

                // 4. Verify Lazy Loading (simulated access)
                // In a real transactional test, we could access collections.
                // Here we just ensured the relationships are set and IDs are generated.
        }

        @Autowired
        private com.skillfactory.orm.service.AssignmentService assignmentService;

        @Autowired
        private com.skillfactory.orm.repository.ModuleRepository moduleRepository;

        @Autowired
        private com.skillfactory.orm.repository.LessonRepository lessonRepository;

        @Test
        void shouldCreateAssignmentAndSubmitSolution() {
                // 1. Setup Data
                User teacher = User.builder().name("Prof. McGonagall").email("minerva@hogwarts.edu").role(Role.TEACHER)
                                .build();
                userRepository.save(teacher);

                User student = User.builder().name("Hermione Granger").email("hermione@hogwarts.edu").role(Role.STUDENT)
                                .build();
                userRepository.save(student);

                Course course = courseService.createCourse("Transfiguration", "Turning things into other things",
                                teacher.getId());

                com.skillfactory.orm.model.Module module = com.skillfactory.orm.model.Module.builder().title("Basics")
                                .course(course).build();
                moduleRepository.save(module);

                com.skillfactory.orm.model.Lesson lesson = com.skillfactory.orm.model.Lesson.builder()
                                .title("Match to Needle")
                                .module(module).build();
                lessonRepository.save(lesson);

                // 2. Create Assignment
                com.skillfactory.orm.model.Assignment assignment = assignmentService.createAssignment(lesson.getId(),
                                "Turn match into needle", "Details...", java.time.LocalDateTime.now().plusDays(1));

                assertThat(assignment.getId()).isNotNull();
                assertThat(assignment.getTitle()).isEqualTo("Turn match into needle");

                // 3. Submit Solution
                com.skillfactory.orm.model.Submission submission = assignmentService.submitAssignment(
                                assignment.getId(),
                                student.getId(), "My perfect solution");

                assertThat(submission.getId()).isNotNull();
                assertThat(submission.getContent()).isEqualTo("My perfect solution");
                assertThat(submission.getAssignment().getId()).isEqualTo(assignment.getId());
                assertThat(submission.getStudent().getId()).isEqualTo(student.getId());
        }

        @Autowired
        private com.skillfactory.orm.service.QuizService quizService;

        @Autowired
        private com.skillfactory.orm.repository.QuizRepository quizRepository;

        @Autowired
        private com.skillfactory.orm.repository.QuestionRepository questionRepository;

        @Autowired
        private com.skillfactory.orm.repository.AnswerOptionRepository answerOptionRepository;

        @Test
        void shouldTakeQuizAndCalculateScore() {
                // 1. Setup Data
                User student = User.builder().name("Ron Weasley").email("ron@hogwarts.edu").role(Role.STUDENT).build();
                userRepository.save(student);

                User teacher = User.builder().name("Prof. Snape").email("snape@hogwarts.edu").role(Role.TEACHER)
                                .build();
                userRepository.save(teacher);

                Course course = courseService.createCourse("Potions", "Brewing glory", teacher.getId());

                com.skillfactory.orm.model.Module module = com.skillfactory.orm.model.Module.builder().title("Basics")
                                .course(course).build();
                moduleRepository.save(module);

                com.skillfactory.orm.model.Quiz quiz = com.skillfactory.orm.model.Quiz.builder()
                                .title("Potions 101")
                                .module(module)
                                .build();
                quizRepository.save(quiz);

                com.skillfactory.orm.model.Question question = com.skillfactory.orm.model.Question.builder()
                                .text("What color is Polyjuice Potion?")
                                .quiz(quiz)
                                .type(com.skillfactory.orm.model.enums.QuestionType.SINGLE_CHOICE)
                                .build();
                questionRepository.save(question);

                com.skillfactory.orm.model.AnswerOption correctOption = com.skillfactory.orm.model.AnswerOption
                                .builder()
                                .text("Mud-like")
                                .isCorrect(true)
                                .question(question)
                                .build();
                answerOptionRepository.save(correctOption);

                com.skillfactory.orm.model.AnswerOption wrongOption = com.skillfactory.orm.model.AnswerOption.builder()
                                .text("Gold")
                                .isCorrect(false)
                                .question(question)
                                .build();
                answerOptionRepository.save(wrongOption);

                // 2. Submit Quiz (Correct Answer)
                java.util.Map<Long, Long> answers = new java.util.HashMap<>();
                answers.put(question.getId(), correctOption.getId());

                com.skillfactory.orm.model.QuizSubmission submission = quizService.submitQuiz(quiz.getId(),
                                student.getId(), answers);

                assertThat(submission.getId()).isNotNull();
                assertThat(submission.getScore()).isEqualTo(100);
        }
}
