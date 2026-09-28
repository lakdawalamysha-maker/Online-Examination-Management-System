package com.exam.model;

import com.exam.service.Reportable; // REQUIREMENT 21: cross-package import
import java.util.ArrayList;
import java.util.List;

// REQUIREMENT 20: 'final' class - once an Exam is defined it must not be subclassed/altered
// in structure, since that would break the integrity of a fixed, already-scheduled exam paper.
public final class Exam implements Reportable {

    // REQUIREMENT 2: 'final' constant - passing percentage never changes at runtime.
    public static final double PASS_PERCENTAGE = 40.0;

    // REQUIREMENT 1: Encapsulation
    private int examId;
    private String title;
    private List<Question> questions; // REQUIREMENT 7: collection of Question objects
    private ExamStatus status;        // REQUIREMENT 5: enum field

    private static int totalExams = 0; // REQUIREMENT 11: static field

    // REQUIREMENT 9: Constructor overloading - default constructor
    public Exam() {
        this("Untitled Exam"); // REQUIREMENT 12: this() chaining
    }

    // REQUIREMENT 9: Constructor overloading - parameterized constructor
    public Exam(String title) {
        this.examId = ++totalExams; // REQUIREMENT 11
        this.title = title;
        this.questions = new ArrayList<>();
        this.status = ExamStatus.UPCOMING;
    }

    public int getExamId() { return examId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<Question> getQuestions() { return questions; }
    public ExamStatus getStatus() { return status; }
    public void setStatus(ExamStatus status) { this.status = status; }

    // REQUIREMENT 10: Method overloading - add a single question.
    public void addQuestion(Question q) {
        questions.add(q);
    }

    // REQUIREMENT 10: Method overloading - same name, different parameter list (adds + announces).
    public void addQuestion(Question q, boolean announce) {
        questions.add(q);
        if (announce) {
            System.out.println("Added question to \"" + title + "\": " + q.getQuestionText());
        }
    }

    public static int getTotalExams() { return totalExams; }

    // Sums the marks of every question currently in the exam.
    public int getTotalMarks() {
        int total = 0; // REQUIREMENT 2: local variable, method scope only
        for (Question q : questions) {
            total += q.getMarks();
        }
        return total;
    }

    @Override
    public String toString() {
        return String.format("Exam[id=%d, title=%s, status=%s, questions=%d, totalMarks=%d]",
                examId, title, status, questions.size(), getTotalMarks());
    }

    // REQUIREMENT 18: Reportable implementation (second, unrelated implementer besides User).
    @Override
    public String generateReport() {
        return "Exam Report -> " + title + " | Total Marks: " + getTotalMarks()
                + " | Pass Mark: " + PASS_PERCENTAGE + "%";
    }
}
