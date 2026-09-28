package com.exam.main;

import com.exam.model.*;
import com.exam.service.*;
import java.util.List;
import java.util.Scanner;

public class Main {

    // REQUIREMENT 2: final constant, class-level (static) scope - available for the whole app lifetime.
    public static final String APP_NAME = "Online Examination Management System";

    // REQUIREMENT 11: static field at the application level.
    private static int menuVisits = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // REQUIREMENT 8: Scanner for console input
        ExamService service = new ExamService();

        System.out.println("=============================================");
        System.out.println(" " + APP_NAME);
        System.out.println("=============================================");

        seedDemoData(service);
         // operator precedence, casting, object arrays, dynamic binding

        boolean running = true; // REQUIREMENT 2: local variable scope (exists only inside main)

        while (running) { // REQUIREMENT 6: while loop
            printMenu();
            int choice = readInt(sc);
            menuVisits++; // REQUIREMENT 11: static counter update

            switch (choice) { // REQUIREMENT 6: switch statement
                case 1:
                    registerStudentFlow(sc, service);
                    break; // REQUIREMENT 6: break jump statement
                case 2:
                    takeExamFlow(sc, service);
                    break;
                case 3:
                    viewResultsFlow(service);
                    break;
                case 4:
                    viewSystemStatsFlow(service);
                    break;
                case 5:
                    running = false; // ends the while loop on the next condition check
                    break;
                default:
                    System.out.println("Invalid choice, please select 1-5.");
            }
        }

        System.out.println("\nThank you for using " + APP_NAME + ". Goodbye!");
        sc.close();
    }

    // ------------------------------------------------------------------
    // Menu & input helpers
    // ------------------------------------------------------------------

    private static void printMenu() {
        System.out.println("\n--------------- MAIN MENU ---------------");
        System.out.println("1. Register a new student");
        System.out.println("2. Take an exam");
        System.out.println("3. View my results");
        System.out.println("4. View system statistics (Admin)");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int readInt(Scanner sc) {
        // Defensive reading loop so bad input never crashes the menu.
        while (true) { // REQUIREMENT 6: while loop
            String line = sc.nextLine().trim(); // REQUIREMENT 13: trim()
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    // ------------------------------------------------------------------
    // Flow 1: Register student
    // ------------------------------------------------------------------
    private static void registerStudentFlow(Scanner sc, ExamService service) {
        System.out.print("Enter your full name: ");
        String name = sc.nextLine().trim();
        System.out.print("Choose a username: ");
        String username = sc.nextLine().trim();
        System.out.print("Choose a password: ");
        String password = sc.nextLine().trim();

        // REQUIREMENT 9: method/constructor overloading in action - parameterized Student constructor
        Student student = new Student(name, username, password);
        service.registerStudent(student);

        System.out.printf("Registration successful! Welcome, %s (User ID: %d)%n",
                student.getName(), student.getUserId());
    }

    // ------------------------------------------------------------------
    // Flow 2: Take an exam
    // ------------------------------------------------------------------
    private static void takeExamFlow(Scanner sc, ExamService service) {
        if (service.getStudents().isEmpty()) {
            System.out.println("No students registered yet. Please register first (option 1).");
            return; // REQUIREMENT 6: return jump statement
        }
        if (service.getExams().isEmpty()) {
            System.out.println("No exams available yet.");
            return;
        }

        System.out.print("Enter your username: ");
        String username = sc.nextLine().trim();
        Student student = service.findStudentByUsername(username);

        if (student == null) {
            System.out.println("Student not found. Please register first.");
            return;
        }

        System.out.println("\nAvailable exams:");
        List<Exam> exams = service.getExams();
        for (int i = 0; i < exams.size(); i++) {
            System.out.printf("  %d. %s (%d questions, %d marks)%n",
                    i + 1, exams.get(i).getTitle(), exams.get(i).getQuestions().size(),
                    exams.get(i).getTotalMarks());
        }

        System.out.print("Select an exam number: ");
        int examChoice = readInt(sc);

        if (examChoice < 1 || examChoice > exams.size()) { // REQUIREMENT 6: if-else
            System.out.println("Invalid exam selection.");
            return;
        }

        Exam exam = exams.get(examChoice - 1);
        System.out.println("\nStarting exam: " + exam.getTitle());
        System.out.println("--------------------------------------------");

        Result result = service.conductExam(student, exam, sc);

        System.out.println("--------------------------------------------");
        System.out.println("Exam submitted!");
        System.out.printf("Score: %.2f / %.2f  (%.2f%%)  Final Score: %d  Status: %s%n",
                result.getScoredMarks(), result.getTotalMarks(), result.getPercentage(),
                result.getFinalScore(), result.isPass() ? "PASS" : "FAIL");
    }

    // ------------------------------------------------------------------
    // Flow 3: View results
    // ------------------------------------------------------------------
    private static void viewResultsFlow(ExamService service) {
        if (service.getResults().isEmpty()) {
            System.out.println("No results recorded yet.");
            return;
        }
        System.out.println("\n----------------- ALL RESULTS -----------------");
        for (Result r : service.getResults()) {
            System.out.println(r); // uses Result.toString() (REQUIREMENT 19)
        }
    }

    // ------------------------------------------------------------------
    // Flow 4: System statistics (uses interface references + polymorphism)
    // ------------------------------------------------------------------
    private static void viewSystemStatsFlow(ExamService service) {
        System.out.println("\n---------------- SYSTEM STATISTICS ----------------");
        System.out.printf("Total registered users   : %d%n", User.getTotalUsers());
        System.out.printf("Total questions created  : %d%n", Question.getTotalQuestions());
        System.out.printf("Total exams created      : %d%n", Exam.getTotalExams());
        System.out.printf("Total exam submissions   : %d%n", ExamService.getTotalSubmissions());
        System.out.printf("Menu visits this session : %d%n", menuVisits);

        System.out.println("\nUser Reports (via Reportable interface reference):");
        for (Student s : service.getStudents()) {
            Reportable reportable = s; // REQUIREMENT 18: interface reference
            System.out.println(" - " + reportable.generateReport());
        }
        for (Instructor i : service.getInstructors()) {
            Reportable reportable = i; // REQUIREMENT 18: same interface, different runtime class
            System.out.println(" - " + reportable.generateReport());
        }
        for (Exam e : service.getExams()) {
            Reportable reportable = e; // REQUIREMENT 18: interface implemented by an unrelated class
            System.out.println(" - " + reportable.generateReport());
        }
    }

    // ------------------------------------------------------------------
    // Demo data & core-language feature demonstration (run once at startup)
    // ------------------------------------------------------------------
    private static void seedDemoData(ExamService service) {
        Instructor instructor = new Instructor("Kaushlendra sir", "Kaushlendra", "pass123", "Computer Science");
        service.registerInstructor(instructor);

        Exam javaExam = new Exam("Core Java Basics");

       String[] options1 = {
        "Encapsulation",
        "Compilation",
        "Inheritance",
        "Iteration"
};

MCQQuestion q1 = new MCQQuestion(
        "Which OOP pillar hides internal state behind getters/setters?",
        5, options1, "A");


String[] options2 = {
        "Inheritance",
        "Polymorphism",
        "Compilation",
        "Iteration"
};

MCQQuestion q2 = new MCQQuestion(
        "Which OOP concept allows one parent reference to refer to different child objects?",
        5, options2, "B");


String[] options3 = {
        "extends",
        "implements",
        "inherits",
        "instanceof"
};

MCQQuestion q3 = new MCQQuestion(
        "Which keyword is used to inherit a class in Java?",
        5, options3, "A");


String[] options4 = {
        "private",
        "public",
        "static",
        "final"
};

MCQQuestion q4 = new MCQQuestion(
        "Which access modifier provides the highest level of restriction?",
        5, options4, "A");


String[] options5 = {
        "new",
        "this",
        "super",
        "static"
};

MCQQuestion q5 = new MCQQuestion(
        "Which keyword is used to call the parent class constructor?",
        5, options5, "C");


String[] options6 = {
        "class",
        "interface",
        "package",
        "import"
};

MCQQuestion q6 = new MCQQuestion(
        "Which keyword is used to declare an interface in Java?",
        5, options6, "B");


String[] options7 = {
        "int",
        "String",
        "boolean",
        "double"
};

MCQQuestion q7 = new MCQQuestion(
        "Which data type is used to store true or false values?",
        5, options7, "C");


String[] options8 = {
        "ArrayList",
        "Scanner",
        "String",
        "System"
};

MCQQuestion q8 = new MCQQuestion(
        "Which Java class is commonly used to take input from the user?",
        5, options8, "B");


String[] options9 = {
        "static",
        "final",
        "private",
        "abstract"
};

MCQQuestion q9 = new MCQQuestion(
        "Which keyword is used to declare a constant variable?",
        5, options9, "B");


String[] options10 = {
        "try-catch",
        "if-else",
        "switch",
        "for"
};

MCQQuestion q10 = new MCQQuestion(
        "Which construct is commonly used to handle exceptions in Java?",
        5, options10, "A");


javaExam.addQuestion(q1);
javaExam.addQuestion(q2);
javaExam.addQuestion(q3);
javaExam.addQuestion(q4);
javaExam.addQuestion(q5);
javaExam.addQuestion(q6);
javaExam.addQuestion(q7);
javaExam.addQuestion(q8);
javaExam.addQuestion(q9);
javaExam.addQuestion(q10);

javaExam.setStatus(ExamStatus.ACTIVE);

service.addExam(javaExam);
    }


    }

