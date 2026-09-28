package com.exam.service;

// REQUIREMENT 18: Interface - defines a contract (no implementation) that any "checkable"
// question type must follow. Implemented by MCQQuestion and DescriptiveQuestion (com.exam.model),
// and accessed polymorphically via an Evaluatable reference in ExamService.conductExam().
public interface Evaluatable {
    // Any class implementing this interface must provide its own evaluation logic.
    double evaluate(String submittedAnswer);
}
