package com.exam.model;

// REQUIREMENT 5: Enum - a fixed, type-safe set of user roles in the system.
// Enums are used instead of plain Strings/ints so invalid roles are impossible at compile time.
public enum UserRole {
    STUDENT,
    INSTRUCTOR,
    ADMIN
}
