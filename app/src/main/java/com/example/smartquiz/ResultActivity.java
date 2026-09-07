package com.example.smartquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ResultActivity displaying quiz score, percentage, performance tier,
 * and saving the attempt into SQLite database (CRUD INSERT).
 * Demonstrates: Intent data extraction, SQLite INSERT, Date formatting, and Activity navigation.
 */
public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extra_score";
    public static final String EXTRA_TOTAL = "extra_total";
    public static final String EXTRA_CATEGORY = "extra_category";
    public static final String EXTRA_CATEGORY_ID = "extra_category_id";
    public static final String EXTRA_PERCENTAGE = "extra_percentage";

    private TextView tvResultCategory, tvResultScore, tvResultPercentage;
    private TextView tvResultPerformanceBadge, tvResultMotivationMessage;
    private Button btnRestartQuiz, btnViewHistory, btnBackHome;

    private int score;
    private int totalQuestions;
    private int categoryId;
    private String categoryName;
    private double percentage;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Retrieve Intent extras
        score = getIntent().getIntExtra(EXTRA_SCORE, 0);
        totalQuestions = getIntent().getIntExtra(EXTRA_TOTAL, 10);
        categoryName = getIntent().getStringExtra(EXTRA_CATEGORY);
        if (categoryName == null) categoryName = "General Knowledge";
        categoryId = getIntent().getIntExtra(EXTRA_CATEGORY_ID, 1);
        percentage = getIntent().getDoubleExtra(EXTRA_PERCENTAGE, 0.0);

        // Bind Views
        tvResultCategory = findViewById(R.id.tvResultCategory);
        tvResultScore = findViewById(R.id.tvResultScore);
        tvResultPercentage = findViewById(R.id.tvResultPercentage);
        tvResultPerformanceBadge = findViewById(R.id.tvResultPerformanceBadge);
        tvResultMotivationMessage = findViewById(R.id.tvResultMotivationMessage);

        btnRestartQuiz = findViewById(R.id.btnRestartQuiz);
        btnViewHistory = findViewById(R.id.btnViewHistory);
        btnBackHome = findViewById(R.id.btnBackHome);

        // Bind Score and Category
        tvResultCategory.setText("Category: " + categoryName);
        tvResultScore.setText(score + " / " + totalQuestions);
        tvResultPercentage.setText(String.format(Locale.getDefault(), "%.0f%%", percentage));

        // Determine Performance Classification & Motivational Message
        String performanceTier;
        String motivationText;

        if (percentage >= 80.0) {
            performanceTier = "Excellent";
            motivationText = getString(R.string.excellent_message);
            tvResultPerformanceBadge.setTextColor(Color.parseColor("#16A34A"));
        } else if (percentage >= 60.0) {
            performanceTier = "Good";
            motivationText = getString(R.string.good_message);
            tvResultPerformanceBadge.setTextColor(Color.parseColor("#2563EB"));
        } else if (percentage >= 40.0) {
            performanceTier = "Average";
            motivationText = getString(R.string.average_message);
            tvResultPerformanceBadge.setTextColor(Color.parseColor("#D97706"));
        } else {
            performanceTier = "Needs Improvement";
            motivationText = getString(R.string.needs_improvement_message);
            tvResultPerformanceBadge.setTextColor(Color.parseColor("#DC2626"));
        }

        tvResultPerformanceBadge.setText(performanceTier);
        tvResultMotivationMessage.setText(motivationText);

        // Save Attempt to SQLite Database (CRUD: INSERT)
        databaseHelper = new DatabaseHelper(this);
        saveAttemptToDatabase(performanceTier);

        // Button Listeners
        btnRestartQuiz.setOnClickListener(v -> restartQuiz());
        btnViewHistory.setOnClickListener(v -> openHistoryScreen());
        btnBackHome.setOnClickListener(v -> returnToHome());
    }

    /**
     * Inserts the completed quiz attempt into SQLite quiz_history table.
     */
    private void saveAttemptToDatabase(String performance) {
        SharedPreferences prefs = getSharedPreferences(SplashActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String userName = prefs.getString(SplashActivity.KEY_USER_NAME, "Student");

        String formattedDate = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(new Date());

        QuizHistory attempt = new QuizHistory(
                userName,
                categoryName,
                score,
                totalQuestions,
                percentage,
                performance,
                formattedDate
        );

        databaseHelper.insertQuizHistory(attempt);
    }

    private void restartQuiz() {
        Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
        intent.putExtra(QuizActivity.EXTRA_CATEGORY_ID, categoryId);
        intent.putExtra(QuizActivity.EXTRA_CATEGORY_NAME, categoryName);
        startActivity(intent);
        finish();
    }

    private void openHistoryScreen() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_TARGET_TAB, MainActivity.TAB_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void returnToHome() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_TARGET_TAB, MainActivity.TAB_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        returnToHome();
    }
}
