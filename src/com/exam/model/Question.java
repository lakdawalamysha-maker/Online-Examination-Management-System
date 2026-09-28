package com.exam.model;

import com.exam.service.Evaluatable; // REQUIREMENT 21: cross-package import

// REQUIREMENT 14: Second inheritance hierarchy - Question -> MCQQuestion, DescriptiveQuestion
// REQUIREMENT 17: Abstract class with an abstract method (display()).
// REQUIREMENT 18: Implements the Evaluatable interface (evaluate() left to subclasses).
public abstract class Question implements Evaluatable {

    // REQUIREMENT 1: Encapsulation
    private int questionId;
    private String questionText;
    private int marks;
    private QuestionType type; // REQUIREMENT 5: enum field

    // REQUIREMENT 11: static field - counts every Question object ever created, across subclasses.
    private static int totalQuestions = 0;

    // REQUIREMENT 9: Constructor overloading - default constructor
    public Question() {
        this("Untitled Question", 1, QuestionType.MCQ); // REQUIREMENT 12: this() constructor chaining
    }

    // REQUIREMENT 9: Constructor overloading - parameterized constructor
    public Question(String questionText, int marks, QuestionType type) {
        this.questionId = ++totalQuestions; // REQUIREMENT 11: static counter update
        this.questionText = questionText;
        this.marks = marks;
        this.type = type;
    }

    public int getQuestionId() { return questionId; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }
    public QuestionType getType() { return type; }

    // REQUIREMENT 11: static method
    public static int getTotalQuestions() { return totalQuestions; }

    // REQUIREMENT 17: abstract method - MCQ shows options, Descriptive shows free-text prompt.
    public abstract void display();

    // REQUIREMENT 19: Overriding toString()
    @Override
    public String toString() {
        return String.format("Q%d [%s | %d marks] %s", questionId, type, marks, questionText);
    }
}
