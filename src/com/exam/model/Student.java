package com.exam.model;

import java.util.ArrayList;
import java.util.List;

// REQUIREMENT 14: First subclass in the User hierarchy (User -> Student).
public class Student extends User {

    // REQUIREMENT 1: Encapsulation
    private List<Result> results; // REQUIREMENT 7: collection of objects (a student's Result history)

    // REQUIREMENT 9: Constructor overloading - default constructor
    public Student() {
        super(); // REQUIREMENT 15: 'super' - explicitly calls the parent's no-arg constructor
        this.results = new ArrayList<>();
    }

    // REQUIREMENT 9: Constructor overloading - parameterized constructor
    public Student(String name, String username, String password) {
        // REQUIREMENT 15: 'super' - calls User's parameterized constructor, fixing the role to STUDENT
        super(name, username, password, UserRole.STUDENT);
        this.results = new ArrayList<>();
    }

    public void addResult(Result r) { results.add(r); }
    public List<Result> getResults() { return results; }

    // REQUIREMENT 16: Overriding an abstract method - when a User reference actually points to a
    // Student object, calling displayDetails() runs THIS version at runtime (dynamic binding).
    @Override
    public void displayDetails() {
        // REQUIREMENT 8: formatted console output
        System.out.printf("Student -> Name: %-15s Username: %-10s Exams Taken: %d%n",
                getName(), getUsername(), results.size());
    }

    // REQUIREMENT 15: 'super' keyword - explicitly calls the parent class's method implementation
    // and extends the result rather than fully replacing it.
    @Override
    public String generateReport() {
        return super.generateReport() + " | Role: STUDENT | Exams Taken: " + results.size();
    }
}
