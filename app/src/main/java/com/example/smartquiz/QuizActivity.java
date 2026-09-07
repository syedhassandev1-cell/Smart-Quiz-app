package com.example.smartquiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

/**
 * QuizActivity - The primary quiz engine.
 * Demonstrates: Activity lifecycle, Intent extras, SQLite query retrieval,
 * UI input validation, RadioGroup state management, ProgressBars, and Intent data passing.
 */
public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY_ID = "extra_category_id";
    public static final String EXTRA_CATEGORY_NAME = "extra_category_name";

    private TextView tvQuizCategoryTitle, tvLiveScore, tvQuestionCount, tvQuestionText;
    private ProgressBar progressBarQuiz;
    private RadioGroup radioGroupOptions;
    private RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    private Button btnNextQuestion;

    private DatabaseHelper databaseHelper;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int currentScore = 0;
    private int totalQuestions = 0;
    private int categoryId = 1;
    private String categoryName = "General Knowledge";
    private boolean isSoundEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Retrieve Intent extras
        categoryId = getIntent().getIntExtra(EXTRA_CATEGORY_ID, 1);
        String nameExtra = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        if (nameExtra != null && !nameExtra.isEmpty()) {
            categoryName = nameExtra;
        }

        // Check sound preference from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(SplashActivity.PREFS_NAME, Context.MODE_PRIVATE);
        isSoundEnabled = prefs.getBoolean(SplashActivity.KEY_SOUND_ENABLED, true);

        // Bind XML views
        tvQuizCategoryTitle = findViewById(R.id.tvQuizCategoryTitle);
        tvLiveScore = findViewById(R.id.tvLiveScore);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        tvQuestionText = findViewById(R.id.tvQuestionText);
        progressBarQuiz = findViewById(R.id.progressBarQuiz);
        radioGroupOptions = findViewById(R.id.radioGroupOptions);
        rbOption1 = findViewById(R.id.rbOption1);
        rbOption2 = findViewById(R.id.rbOption2);
        rbOption3 = findViewById(R.id.rbOption3);
        rbOption4 = findViewById(R.id.rbOption4);
        btnNextQuestion = findViewById(R.id.btnNextQuestion);

        tvQuizCategoryTitle.setText(categoryName);

        // Fetch questions from SQLite database
        databaseHelper = new DatabaseHelper(this);
        questionList = databaseHelper.getQuestionsByCategory(categoryId);

        if (questionList == null || questionList.isEmpty()) {
            Toast.makeText(this, R.string.no_questions, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        totalQuestions = questionList.size();
        progressBarQuiz.setMax(totalQuestions);

        // Display first question
        displayCurrentQuestion();

        btnNextQuestion.setOnClickListener(v -> handleNextButtonClick());
    }

    /**
     * Binds the current Question object to the UI components.
     */
    private void displayCurrentQuestion() {
        Question q = questionList.get(currentQuestionIndex);

        tvQuestionCount.setText("Question " + (currentQuestionIndex + 1) + " of " + totalQuestions);
        tvLiveScore.setText("Score: " + currentScore);
        progressBarQuiz.setProgress(currentQuestionIndex + 1);

        tvQuestionText.setText(q.getQuestion());
        rbOption1.setText(q.getOption1());
        rbOption2.setText(q.getOption2());
        rbOption3.setText(q.getOption3());
        rbOption4.setText(q.getOption4());

        // Clear previous RadioGroup selection
        radioGroupOptions.clearCheck();

        // If this is the last question, change button text to "Finish Quiz"
        if (currentQuestionIndex == totalQuestions - 1) {
            btnNextQuestion.setText(R.string.finish);
        } else {
            btnNextQuestion.setText(R.string.next);
        }
    }

    /**
     * Handles Next button click, performs validation and score calculation.
     */
    private void handleNextButtonClick() {
        int selectedRadioButtonId = radioGroupOptions.getCheckedRadioButtonId();

        // 1. VALIDATION: Ensure user selected an option before proceeding
        if (selectedRadioButtonId == -1) {
            Toast.makeText(this, R.string.select_answer_warning, Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Identify selected option (1, 2, 3, or 4)
        int selectedOptionNumber = 0;
        if (selectedRadioButtonId == R.id.rbOption1) {
            selectedOptionNumber = 1;
        } else if (selectedRadioButtonId == R.id.rbOption2) {
            selectedOptionNumber = 2;
        } else if (selectedRadioButtonId == R.id.rbOption3) {
            selectedOptionNumber = 3;
        } else if (selectedRadioButtonId == R.id.rbOption4) {
            selectedOptionNumber = 4;
        }

        // 3. Compare selected option with the correct answer
        Question currentQuestion = questionList.get(currentQuestionIndex);
        if (selectedOptionNumber == currentQuestion.getCorrectAnswer()) {
            currentScore++;
            playFeedbackTone(true);
        } else {
            playFeedbackTone(false);
        }

        // 4. Navigate to next question or complete quiz
        currentQuestionIndex++;

        if (currentQuestionIndex < totalQuestions) {
            displayCurrentQuestion();
        } else {
            finishAndOpenResultScreen();
        }
    }

    /**
     * Simple audio feedback demonstration using ToneGenerator.
     */
    private void playFeedbackTone(boolean isCorrect) {
        if (!isSoundEnabled) return;
        try {
            ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 60);
            if (isCorrect) {
                toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 150);
            } else {
                toneGen.startTone(ToneGenerator.TONE_PROP_NACK, 180);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Opens ResultActivity via explicit Intent with calculated quiz summary.
     */
    private void finishAndOpenResultScreen() {
        double percentage = (totalQuestions > 0) ? ((double) currentScore / totalQuestions) * 100.0 : 0.0;

        Intent resultIntent = new Intent(QuizActivity.this, ResultActivity.class);
        resultIntent.putExtra(ResultActivity.EXTRA_SCORE, currentScore);
        resultIntent.putExtra(ResultActivity.EXTRA_TOTAL, totalQuestions);
        resultIntent.putExtra(ResultActivity.EXTRA_CATEGORY, categoryName);
        resultIntent.putExtra(ResultActivity.EXTRA_CATEGORY_ID, categoryId);
        resultIntent.putExtra(ResultActivity.EXTRA_PERCENTAGE, percentage);

        startActivity(resultIntent);
        finish(); // Remove QuizActivity from back stack so back button doesn't reload finished quiz
    }

    @Override
    public void onBackPressed() {
        // Confirm before quitting active quiz
        new AlertDialog.Builder(this)
                .setTitle("Exit Quiz?")
                .setMessage("Are you sure you want to exit? Your current progress will not be saved.")
                .setPositiveButton("Exit", (dialog, which) -> finish())
                .setNegativeButton("Continue", null)
                .show();
    }
}
