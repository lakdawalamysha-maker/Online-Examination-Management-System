package com.exam.service;

import com.exam.model.*; // REQUIREMENT 21: cross-package import (service -> model)
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Service-layer class: coordinates model objects, holds the in-memory "database" of the app.
public class ExamService {

    // REQUIREMENT 1: Encapsulation
    private List<Student> students = new ArrayList<>();
    private List<Instructor> instructors = new ArrayList<>();
    private List<Exam> exams = new ArrayList<>();
    private List<Result> results = new ArrayList<>();

    // REQUIREMENT 11: static field - total exam submissions across the whole application.
    private static int totalSubmissions = 0;

    public void registerStudent(Student s) { students.add(s); }
    public void registerInstructor(Instructor i) { instructors.add(i); }
    public void addExam(Exam e) { exams.add(e); }

    public List<Student> getStudents() { return students; }
    public List<Instructor> getInstructors() { return instructors; }
    public List<Exam> getExams() { return exams; }
    public List<Result> getResults() { return results; }

    // REQUIREMENT 13: String methods used for lookups
    public Student findStudentByUsername(String username) {
        for (Student s : students) {
            if (s.getUsername().equalsIgnoreCase(username)) { // REQUIREMENT 13
                return s; // REQUIREMENT 6: return as a jump statement
            }
        }
        return null;
    }

    public Exam findExamByTitleFragment(String fragment) {
        for (Exam e : exams) {
            // REQUIREMENT 13: String.contains() for a partial, case-insensitive search
            if (e.getTitle().toLowerCase().contains(fragment.toLowerCase())) {
                return e;
            }
        }
        return null;
    }

    // Runs a full exam attempt for one student and returns the graded Result.
    public Result conductExam(Student student, Exam exam, Scanner sc) {
        double totalScored = 0; // local variable (REQUIREMENT 2: local scope)
        double totalPossible = 0;
        List<Question> questions = exam.getQuestions();

        for (int i = 0; i < questions.size(); i++) { // REQUIREMENT 6: for loop
            Question q = questions.get(i);
            q.display(); // REQUIREMENT 16: dynamic binding - runs MCQQuestion or DescriptiveQuestion's display()

            System.out.print("Your answer (or type 'skip'): ");
            String answer = sc.nextLine().trim(); // REQUIREMENT 13: trim()

            if (answer.equalsIgnoreCase("skip")) { // REQUIREMENT 13
                totalPossible += q.getMarks();
                continue; // REQUIREMENT 6: continue jump statement - skips scoring, keeps looping
            }

            // REQUIREMENT 18: interface reference - Evaluatable hides whether q is an MCQ or Descriptive
            // question; evaluate() dispatches to the correct overridden implementation at runtime.
            Evaluatable evaluator = q;
            double marksAwarded = evaluator.evaluate(answer);

            totalScored += marksAwarded;
            totalPossible += q.getMarks();
        }

        totalSubmissions++; // REQUIREMENT 11: static counter update on every completed attempt

        Result result = new Result(student, exam, totalScored, totalPossible);
        results.add(result);
        student.addResult(result);
        exam.setStatus(ExamStatus.COMPLETED);
        return result;
    }

    // REQUIREMENT 11: static method
    public static int getTotalSubmissions() { return totalSubmissions; }
}
