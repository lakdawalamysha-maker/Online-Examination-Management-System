package com.exam.model;

// REQUIREMENT 14: Second subclass of Question - shows a genuinely different evaluate() strategy,
// which is the whole point of polymorphism/overriding.
public class DescriptiveQuestion extends Question {

    private String[] keywords;        // REQUIREMENT 7: array of expected keywords
    private int minKeywordsRequired;

    public DescriptiveQuestion() {
        super(); // REQUIREMENT 15
        this.keywords = new String[]{"keyword"};
        this.minKeywordsRequired = 1;
    }

    public DescriptiveQuestion(String text, int marks, String[] keywords, int minKeywordsRequired) {
        super(text, marks, QuestionType.DESCRIPTIVE); // REQUIREMENT 15
        this.keywords = keywords;
        this.minKeywordsRequired = minKeywordsRequired;
    }

    @Override
    public void display() {
        System.out.println(this);
        System.out.println("     (Type a short written answer)");
    }

    // REQUIREMENT 18: Evaluatable implementation for Descriptive - keyword-based partial credit.
    @Override
    public double evaluate(String submittedAnswer) {
        if (submittedAnswer == null || submittedAnswer.trim().isEmpty()) return 0;

        String lowerAnswer = submittedAnswer.toLowerCase(); // REQUIREMENT 13: String method
        int matched = 0;

        for (String keyword : keywords) { // REQUIREMENT 6: enhanced for loop
            if (lowerAnswer.contains(keyword.toLowerCase())) { // REQUIREMENT 13: contains()
                matched++;
            }
        }

        if (matched == 0) {
            return 0; // REQUIREMENT 6: return as a jump statement, exits early
        }

        // REQUIREMENT 3: operator precedence - division happens before multiplication is applied,
        // and the ternary below depends on that computed ratio.
        double ratio = (double) matched / keywords.length; // REQUIREMENT 4: int -> double cast context
        boolean passedThreshold = matched >= minKeywordsRequired;

        return passedThreshold ? getMarks() * ratio : 0; // partial credit proportional to keywords matched
    }
}
