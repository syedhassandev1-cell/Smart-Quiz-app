package com.example.smartquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ResultActivity
 *
 * Displays:
 * - Quiz category
 * - Score
 * - Percentage
 * - Performance level
 * - Motivation message
 *
 * Also saves the completed quiz attempt into SQLite.
 */
public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extra_score";
    public static final String EXTRA_TOTAL = "extra_total";
    public static final String EXTRA_CATEGORY = "extra_category";
    public static final String EXTRA_CATEGORY_ID = "extra_category_id";
    public static final String EXTRA_PERCENTAGE = "extra_percentage";

    private TextView tvResultCategory;
    private TextView tvResultScore;
    private TextView tvResultPercentage;
    private TextView tvResultPerformanceBadge;
    private TextView tvResultMotivationMessage;

    private Button btnRestartQuiz;
    private Button btnViewHistory;
    private Button btnBackHome;

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

        // ---------------------------------------------------------
        // 1. GET DATA FROM QUIZ ACTIVITY
        // ---------------------------------------------------------

        score = getIntent().getIntExtra(EXTRA_SCORE, 0);

        totalQuestions = getIntent().getIntExtra(
                EXTRA_TOTAL,
                10
        );

        categoryName = getIntent().getStringExtra(
                EXTRA_CATEGORY
        );

        if (categoryName == null || categoryName.trim().isEmpty()) {
            categoryName = "General Knowledge";
        }

        categoryId = getIntent().getIntExtra(
                EXTRA_CATEGORY_ID,
                1
        );

        percentage = getIntent().getDoubleExtra(
                EXTRA_PERCENTAGE,
                0.0
        );

        // ---------------------------------------------------------
        // 2. SAFETY CHECK
        // ---------------------------------------------------------

        if (totalQuestions <= 0) {
            totalQuestions = 1;
        }

        // If percentage was not correctly passed from QuizActivity,
        // calculate it again here.
        if (percentage <= 0 && score > 0) {
            percentage = ((double) score / totalQuestions) * 100.0;
        }

        // ---------------------------------------------------------
        // 3. FIND VIEWS
        // ---------------------------------------------------------

        tvResultCategory = findViewById(R.id.tvResultCategory);
        tvResultScore = findViewById(R.id.tvResultScore);
        tvResultPercentage = findViewById(R.id.tvResultPercentage);
        tvResultPerformanceBadge =
                findViewById(R.id.tvResultPerformanceBadge);
        tvResultMotivationMessage =
                findViewById(R.id.tvResultMotivationMessage);

        btnRestartQuiz = findViewById(R.id.btnRestartQuiz);
        btnViewHistory = findViewById(R.id.btnViewHistory);
        btnBackHome = findViewById(R.id.btnBackHome);

        // ---------------------------------------------------------
        // 4. DISPLAY RESULT
        // ---------------------------------------------------------

        tvResultCategory.setText(
                "Category: " + categoryName
        );

        tvResultScore.setText(
                score + " / " + totalQuestions
        );

        tvResultPercentage.setText(
                String.format(
                        Locale.getDefault(),
                        "%.0f%%",
                        percentage
                )
        );

        // ---------------------------------------------------------
        // 5. PERFORMANCE
        // ---------------------------------------------------------

        String performanceTier;
        String motivationText;

        if (percentage >= 80.0) {

            performanceTier = "Excellent";
            motivationText =
                    getString(R.string.excellent_message);

            tvResultPerformanceBadge.setTextColor(
                    Color.parseColor("#16A34A")
            );

        } else if (percentage >= 60.0) {

            performanceTier = "Good";
            motivationText =
                    getString(R.string.good_message);

            tvResultPerformanceBadge.setTextColor(
                    Color.parseColor("#2563EB")
            );

        } else if (percentage >= 40.0) {

            performanceTier = "Average";
            motivationText =
                    getString(R.string.average_message);

            tvResultPerformanceBadge.setTextColor(
                    Color.parseColor("#D97706")
            );

        } else {

            performanceTier = "Needs Improvement";
            motivationText =
                    getString(R.string.needs_improvement_message);

            tvResultPerformanceBadge.setTextColor(
                    Color.parseColor("#DC2626")
            );
        }

        tvResultPerformanceBadge.setText(
                performanceTier
        );

        tvResultMotivationMessage.setText(
                motivationText
        );

        // ---------------------------------------------------------
        // 6. INITIALIZE DATABASE
        // ---------------------------------------------------------

        databaseHelper = new DatabaseHelper(this);

        // ---------------------------------------------------------
        // 7. SAVE RESULT TO SQLITE
        // ---------------------------------------------------------

        saveAttemptToDatabase(performanceTier);

        // ---------------------------------------------------------
        // 8. BUTTONS
        // ---------------------------------------------------------

        btnRestartQuiz.setOnClickListener(
                v -> restartQuiz()
        );

        btnViewHistory.setOnClickListener(
                v -> openHistoryScreen()
        );

        btnBackHome.setOnClickListener(
                v -> returnToHome()
        );
    }

    /**
     * Saves quiz attempt into SQLite database.
     */
    private void saveAttemptToDatabase(String performance) {

        try {

            // Get username
            SharedPreferences prefs =
                    getSharedPreferences(
                            SplashActivity.PREFS_NAME,
                            Context.MODE_PRIVATE
                    );

            String userName =
                    prefs.getString(
                            SplashActivity.KEY_USER_NAME,
                            "Student"
                    );

            // Current date/time
            String formattedDate =
                    new SimpleDateFormat(
                            "dd MMM yyyy, HH:mm",
                            Locale.getDefault()
                    ).format(new Date());

            // Create QuizHistory object
            QuizHistory attempt =
                    new QuizHistory(
                            userName,
                            categoryName,
                            score,
                            totalQuestions,
                            percentage,
                            performance,
                            formattedDate
                    );

            // INSERT into SQLite
            long insertedId =
                    databaseHelper.insertQuizHistory(attempt);

            // Check whether insert was successful
            if (insertedId == -1) {

                Toast.makeText(
                        this,
                        "Unable to save quiz history",
                        Toast.LENGTH_LONG
                ).show();

            } else {

                Toast.makeText(
                        this,
                        "Quiz result saved",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } catch (Exception e) {

            // Prevent the application from crashing
            // because of a database error.

            Toast.makeText(
                    this,
                    "History error: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

            e.printStackTrace();
        }
    }

    /**
     * Restart the same quiz.
     */
    private void restartQuiz() {

        Intent intent =
                new Intent(
                        ResultActivity.this,
                        QuizActivity.class
                );

        intent.putExtra(
                QuizActivity.EXTRA_CATEGORY_ID,
                categoryId
        );

        intent.putExtra(
                QuizActivity.EXTRA_CATEGORY_NAME,
                categoryName
        );

        startActivity(intent);

        finish();
    }

    /**
     * Open History tab.
     */
    private void openHistoryScreen() {

        Intent intent =
                new Intent(
                        ResultActivity.this,
                        MainActivity.class
                );

        intent.putExtra(
                MainActivity.EXTRA_TARGET_TAB,
                MainActivity.TAB_HISTORY
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);

        finish();
    }

    /**
     * Return to Home tab.
     */
    private void returnToHome() {

        Intent intent =
                new Intent(
                        ResultActivity.this,
                        MainActivity.class
                );

        intent.putExtra(
                MainActivity.EXTRA_TARGET_TAB,
                MainActivity.TAB_HOME
        );

        intent.setFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
        );

        startActivity(intent);

        finish();
    }

    /**
     * Android back button.
     */
    @Override
    public void onBackPressed() {
        returnToHome();
    }
}