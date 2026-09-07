package com.example.smartquiz;

/**
 * QuizHistory model representing past quiz attempts.
 * Demonstrates OOP concepts: Encapsulation, Constructors, Data formatting.
 */
public class QuizHistory {
    private int id;
    private String userName;
    private String category;
    private int score;
    private int totalQuestions;
    private double percentage;
    private String performance;
    private String date;

    // Default constructor
    public QuizHistory() {
    }

    // Full constructor
    public QuizHistory(int id, String userName, String category, int score, int totalQuestions, double percentage, String performance, String date) {
        this.id = id;
        this.userName = userName;
        this.category = category;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.performance = performance;
        this.date = date;
    }

    // Constructor without ID (used when saving new attempt)
    public QuizHistory(String userName, String category, int score, int totalQuestions, double percentage, String performance, String date) {
        this.userName = userName;
        this.category = category;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
        this.performance = performance;
        this.date = date;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
