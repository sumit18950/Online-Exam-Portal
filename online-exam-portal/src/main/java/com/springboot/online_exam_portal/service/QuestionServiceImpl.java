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
import java.util.Objects;

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
        // Resolve the Exam entity from DB so the foreign key is properly set
        if (question.getExam() != null && question.getExam().getId() != 0) {
            Exams exam = examsRepo.findById(question.getExam().getId())
                    .orElseThrow(() -> new RuntimeException("Exam not found with id: " + question.getExam().getId()));
            question.setExam(exam);
        }

        // Resolve the Subject entity from DB so the foreign key is properly set
        if (question.getSubject() != null && question.getSubject().getId() != 0) {
            Subject subject = subjectRepo.findById(question.getSubject().getId())
                    .orElseThrow(() -> new RuntimeException("Subject not found with id: " + question.getSubject().getId()));
            question.setSubject(subject);
        }

        // Essential: Link every option to the question object
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
        return questionRepo.findById(Objects.requireNonNull(id)).orElseThrow(() -> new RuntimeException("Not Found"));
    }

    @Override
    @Transactional
    public Questions updateQuestion(Integer id, Questions questionDetails) {
        Questions existing = getQuestionById(id);

        existing.setQuestion_text(questionDetails.getQuestion_text());
        existing.setQuestion_type(questionDetails.getQuestion_type());

        existing.setMarks(questionDetails.getMarks());

        // Update Options: Clear existing and add new list to maintain sync
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
        questionRepo.deleteById(Objects.requireNonNull(id));
    }
}

