package com.exam.model;

// REQUIREMENT 14: First subclass of Question.
public class MCQQuestion extends Question {

    private String[] options;      // REQUIREMENT 7: array field (array of objects/primitives)
    private String correctOption;

    public MCQQuestion() {
        super(); // REQUIREMENT 15
        this.options = new String[]{"Option A", "Option B", "Option C", "Option D"};
        this.correctOption = "A";
    }

    public MCQQuestion(String text, int marks, String[] options, String correctOption) {
        super(text, marks, QuestionType.MCQ); // REQUIREMENT 15: 'super' parameterized call
        this.options = options;
        this.correctOption = correctOption;
    }

    public String[] getOptions() { return options; }
    public String getCorrectOption() { return correctOption; }

    // REQUIREMENT 16: overriding + dynamic binding demonstrated in ExamService.conductExam(),
    // where a Question reference pointing to an MCQQuestion calls THIS version.
    @Override
    public void display() {
        System.out.println(this); // uses the inherited/overridden toString()
        for (int i = 0; i < options.length; i++) { // REQUIREMENT 6: for loop
            // REQUIREMENT 3: casting 'A' + i (int arithmetic) back to char for labelling options
            char label = (char) ('A' + i); // REQUIREMENT 4: type conversion/casting (int -> char)
            System.out.printf("     %c) %s%n", label, options[i]);
        }
    }

    // REQUIREMENT 18: Evaluatable implementation for MCQ - exact match against the correct option.
    @Override
    public double evaluate(String submittedAnswer) {
        if (submittedAnswer == null) return 0;
        // REQUIREMENT 13: String methods - trim() and equalsIgnoreCase()
        boolean isCorrect = submittedAnswer.trim().equalsIgnoreCase(correctOption.trim());
        return isCorrect ? getMarks() : 0;
    }
}
