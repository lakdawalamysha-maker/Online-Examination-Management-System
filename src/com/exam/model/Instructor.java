package com.exam.model;

// REQUIREMENT 14: Second subclass in the User hierarchy (User -> Instructor).
public class Instructor extends User {

    private String department; // REQUIREMENT 1: Encapsulation

    public Instructor() {
        super(); // REQUIREMENT 15: 'super' - parent's default constructor
        this.department = "General";
    }

    public Instructor(String name, String username, String password, String department) {
        super(name, username, password, UserRole.INSTRUCTOR); // REQUIREMENT 15: 'super'
        this.department = department;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    // REQUIREMENT 16: Overriding + dynamic binding - a completely different behaviour from
    // Student.displayDetails(), even though both are invoked the same way via a User reference.
    @Override
    public void displayDetails() {
        System.out.printf("Instructor -> Name: %-15s Department: %s%n", getName(), department);
    }

    @Override
    public String generateReport() {
        return super.generateReport() + " | Role: INSTRUCTOR | Department: " + department; // REQUIREMENT 15
    }
}
