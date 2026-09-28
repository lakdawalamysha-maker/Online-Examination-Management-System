package com.exam.model;

// REQUIREMENT 1: Encapsulation - a simple, fully encapsulated data-holder class.
public class Result {

    private Student student;
    private Exam exam;
    private double scoredMarks;
    private double totalMarks;
    private double percentage;
    private int finalScore; // whole-number score used for reports/leaderboards

    // REQUIREMENT 9: Constructor overloading - default constructor
    public Result() {
    }

    // REQUIREMENT 9: Constructor overloading - parameterized constructor
    public Result(Student student, Exam exam, double scoredMarks, double totalMarks) {
        this.student = student;
        this.exam = exam;
        this.scoredMarks = scoredMarks;
        this.totalMarks = totalMarks;
        // REQUIREMENT 3: operator precedence - division is evaluated before the multiplication by 100
        // because of standard operator precedence (and the parentheses make the intent explicit).
        this.percentage = (totalMarks == 0) ? 0 : (scoredMarks / totalMarks) * 100;
        // REQUIREMENT 4: explicit type casting - converting a double percentage into an int final score.
        this.finalScore = (int) this.percentage;
    }

    public Student getStudent() { return student; }
    public Exam getExam() { return exam; }
    public double getScoredMarks() { return scoredMarks; }
    public double getTotalMarks() { return totalMarks; }
    public double getPercentage() { return percentage; }
    public int getFinalScore() { return finalScore; }

    public boolean isPass() {
        return percentage >= Exam.PASS_PERCENTAGE;
    }

    // REQUIREMENT 19: Overriding toString()
    @Override
    public String toString() {
        // REQUIREMENT 8: formatted output
        return String.format("Result[student=%s, exam=%s, scored=%.2f/%.2f, percentage=%.2f%%, finalScore=%d, status=%s]",
                student.getName(), exam.getTitle(), scoredMarks, totalMarks, percentage, finalScore,
                isPass() ? "PASS" : "FAIL");
    }

    // REQUIREMENT 19: Overriding equals() - two results are equal if same student + same exam.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Result)) return false;
        Result other = (Result) obj;
        return this.student.equals(other.student) && this.exam.getExamId() == other.exam.getExamId();
    }

    @Override
    public int hashCode() {
        return student.hashCode() * 31 + exam.getExamId();
    }
}
