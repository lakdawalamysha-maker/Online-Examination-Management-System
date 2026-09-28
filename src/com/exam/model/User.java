package com.exam.model;

import com.exam.service.Reportable; // REQUIREMENT 21: import across custom packages (model -> service)

// REQUIREMENT 14: Base class of the inheritance hierarchy User -> Student, Instructor
// REQUIREMENT 17: Abstract class (cannot be instantiated directly; forces subclasses to
//                  define displayDetails()).
// REQUIREMENT 18: Implements the Reportable interface.
public abstract class User implements Reportable {

    // REQUIREMENT 1: Encapsulation - all fields are private; only accessible via getters/setters.
    private int userId;          // instance variable (REQUIREMENT 2: scope - instance)
    private String name;
    private String username;
    private String password;
    private UserRole role;       // REQUIREMENT 5: enum used as a field type

    // REQUIREMENT 11: static field - shared by ALL User objects, tracks a running total.
    private static int totalUsers = 0;

    // REQUIREMENT 9: Constructor overloading - no-argument (default) constructor.
    public User() {
        // REQUIREMENT 12: 'this' keyword used for constructor chaining (calls the constructor below).
        this("Unknown", "guest", "guest123", UserRole.STUDENT);
    }

    // REQUIREMENT 9: Constructor overloading - parameterized constructor.
    public User(String name, String username, String password, UserRole role) {
        // REQUIREMENT 2: local variable scope - the parameters (name, username, ...) exist
        // only inside this constructor; 'this.name' refers to the INSTANCE field instead.
        // REQUIREMENT 12: 'this' keyword resolves field shadowing (parameter name == field name).
        this.userId = ++totalUsers; // REQUIREMENT 11: static counter incremented for every new user
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ---------- Getters & Setters (REQUIREMENT 1: Encapsulation) ----------
    public int getUserId() { return userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; } // REQUIREMENT 12: 'this' resolves shadowing

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    // REQUIREMENT 11: static method - operates on the static field, not on any one instance.
    public static int getTotalUsers() { return totalUsers; }

    // REQUIREMENT 17: abstract method - every concrete subclass MUST implement this differently.
    public abstract void displayDetails();

    // REQUIREMENT 19: Overriding Object's toString().
    @Override
    public String toString() {
        // REQUIREMENT 8: formatted output via String.format
        return String.format("User[id=%d, name=%s, username=%s, role=%s]", userId, name, username, role);
    }

    // REQUIREMENT 19: Overriding Object's equals() - two users are "equal" if their IDs match.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return this.userId == other.userId;
    }

    @Override
    public int hashCode() {
        // Kept consistent with equals(), as required whenever equals() is overridden.
        return Integer.hashCode(userId);
    }

    // REQUIREMENT 18: base implementation of the Reportable interface method.
    // Subclasses override this and call super.generateReport() (REQUIREMENT 15: 'super' keyword).
    @Override
    public String generateReport() {
        return "Report for " + name + " (ID: " + userId + ")";
    }
}
