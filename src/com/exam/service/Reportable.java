package com.exam.service;

// REQUIREMENT 18 (second interface, implemented by unrelated classes User and Exam):
// Any class that can produce a human-readable summary implements this.
public interface Reportable {
    String generateReport();
}
