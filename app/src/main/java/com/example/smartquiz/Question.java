package com.example.smartquiz;

/**
 * Question model representing a multiple-choice question.
 * Demonstrates OOP concepts: Encapsulation, Constructors, Data Types.
 */
public class Question {
    private int id;
    private int categoryId;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int correctAnswer; // 1, 2, 3, or 4

    // Default constructor
    public Question() {
    }

    // Complete constructor with ID
    public Question(int id, int categoryId, String question, String option1, String option2, String option3, String option4, int correctAnswer) {
        this.id = id;
        this.categoryId = categoryId;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAnswer = correctAnswer;
    }

    // Constructor without ID (used for database insertion)
    public Question(int categoryId, String question, String option1, String option2, String option3, String option4, int correctAnswer) {
        this.categoryId = categoryId;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAnswer = correctAnswer;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Helper method to get the correct answer string value
     */
    public String getCorrectAnswerText() {
        switch (correctAnswer) {
            case 1:
                return option1;
            case 2:
                return option2;
            case 3:
                return option3;
            case 4:
                return option4;
            default:
                return "";
        }
    }
}
