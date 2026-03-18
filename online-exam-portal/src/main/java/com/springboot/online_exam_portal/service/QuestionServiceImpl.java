package com.springboot.online_exam_portal.service;
import com.springboot.online_exam_portal.entity.Exams;
import com.springboot.online_exam_portal.entity.Questions;
import com.springboot.online_exam_portal.entity.Subject;
import com.springboot.online_exam_portal.repository.ExamsRepository;
import com.springboot.online_exam_portal.repository.QuestionRepository;
import com.springboot.online_exam_portal.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private ExamsRepository examsRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @Override
    @Transactional
    public Questions saveQuestionAndOptions(Questions question) {
        // Resolve Exam from DB
        if (question.getExam() != null && question.getExam().getId() != 0) {
            Exams exam = examsRepo.findById(question.getExam().getId())
                    .orElseThrow(() -> new RuntimeException("Exam not found with id: " + question.getExam().getId()));
            question.setExam(exam);
        }

        // Resolve Subject from DB
        if (question.getSubject() != null && question.getSubject().getId() != 0) {
            Subject subject = subjectRepo.findById(question.getSubject().getId())
                    .orElseThrow(() -> new RuntimeException("Subject not found with id: " + question.getSubject().getId()));
            question.setSubject(subject);
        }

        // Link each option back to this question
        if (question.getOptions() != null) {
            question.getOptions().forEach(option -> option.setQuestion(question));
        }
        return questionRepo.save(question);
    }

    @Override
    public List<Questions> getAllQuestions() {
        return questionRepo.findAll();
    }

    @Override
    public Questions getQuestionById(Integer id) {
        return questionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + id));
    }

    @Override
    public List<Questions> getQuestionsBySubjectId(Integer subjectId) {
        return questionRepo.findBySubjectId(subjectId);
    }

    @Override
    @Transactional
    public Questions updateQuestion(Integer id, Questions questionDetails) {
        Questions existing = getQuestionById(id);

        existing.setQuestionText(questionDetails.getQuestionText());
        existing.setQuestionType(questionDetails.getQuestionType());
        existing.setMarks(questionDetails.getMarks());

        // Update exam & subject if provided
        if (questionDetails.getExam() != null && questionDetails.getExam().getId() != 0) {
            Exams exam = examsRepo.findById(questionDetails.getExam().getId())
                    .orElseThrow(() -> new RuntimeException("Exam not found"));
            existing.setExam(exam);
        }
        if (questionDetails.getSubject() != null && questionDetails.getSubject().getId() != 0) {
            Subject subject = subjectRepo.findById(questionDetails.getSubject().getId())
                    .orElseThrow(() -> new RuntimeException("Subject not found"));
            existing.setSubject(subject);
        }

        // Replace all options at once (orphanRemoval deletes old ones)
        existing.getOptions().clear();
        if (questionDetails.getOptions() != null) {
            questionDetails.getOptions().forEach(opt -> {
                opt.setQuestion(existing);
                existing.getOptions().add(opt);
            });
        }
        return questionRepo.save(existing);
    }

    @Override
    public void deleteQuestion(Integer id) {
        questionRepo.deleteById(id);
    }
}

