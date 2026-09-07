package com.example.smartquiz;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * ManageQuestionsActivity provides an interface for editing quiz questions in SQLite.
 * Demonstrates: SQLite UPDATE operation, AlertDialog with custom view, form validation,
 * and RecyclerView dataset updates.
 */
public class ManageQuestionsActivity extends AppCompatActivity implements QuestionAdapter.OnQuestionEditListener {

    private RecyclerView rvManageQuestions;
    private DatabaseHelper databaseHelper;
    private QuestionAdapter adapter;
    private List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_questions);

        ImageButton btnBack = findViewById(R.id.btnBackManageQuestions);
        btnBack.setOnClickListener(v -> finish());

        rvManageQuestions = findViewById(R.id.rvManageQuestions);
        rvManageQuestions.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        loadQuestions();
    }

    private void loadQuestions() {
        questionList = databaseHelper.getAllQuestions();
        if (adapter == null) {
            adapter = new QuestionAdapter(this, questionList, this);
            rvManageQuestions.setAdapter(adapter);
        } else {
            adapter.updateList(questionList);
        }
    }

    @Override
    public void onEditQuestion(Question question) {
        // Show edit dialog to perform SQLite UPDATE
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_question, null);

        EditText etQuestionText = view.findViewById(R.id.etEditQuestionText);
        EditText etOpt1 = view.findViewById(R.id.etEditOption1);
        EditText etOpt2 = view.findViewById(R.id.etEditOption2);
        EditText etOpt3 = view.findViewById(R.id.etEditOption3);
        EditText etOpt4 = view.findViewById(R.id.etEditOption4);
        RadioGroup rgCorrect = view.findViewById(R.id.rgEditCorrectOption);
        RadioButton rb1 = view.findViewById(R.id.rbEditOpt1);
        RadioButton rb2 = view.findViewById(R.id.rbEditOpt2);
        RadioButton rb3 = view.findViewById(R.id.rbEditOpt3);
        RadioButton rb4 = view.findViewById(R.id.rbEditOpt4);

        // Pre-fill existing data
        etQuestionText.setText(question.getQuestion());
        etOpt1.setText(question.getOption1());
        etOpt2.setText(question.getOption2());
        etOpt3.setText(question.getOption3());
        etOpt4.setText(question.getOption4());

        int currentCorrect = question.getCorrectAnswer();
        if (currentCorrect == 1) rb1.setChecked(true);
        else if (currentCorrect == 2) rb2.setChecked(true);
        else if (currentCorrect == 3) rb3.setChecked(true);
        else if (currentCorrect == 4) rb4.setChecked(true);

        builder.setView(view)
                .setTitle("Edit Question #" + question.getId())
                .setPositiveButton("Save Update", (dialog, which) -> {
                    String qText = etQuestionText.getText().toString().trim();
                    String o1 = etOpt1.getText().toString().trim();
                    String o2 = etOpt2.getText().toString().trim();
                    String o3 = etOpt3.getText().toString().trim();
                    String o4 = etOpt4.getText().toString().trim();

                    if (TextUtils.isEmpty(qText) || TextUtils.isEmpty(o1) || TextUtils.isEmpty(o2) ||
                            TextUtils.isEmpty(o3) || TextUtils.isEmpty(o4)) {
                        Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int chosenCorrect = 1;
                    int checkedId = rgCorrect.getCheckedRadioButtonId();
                    if (checkedId == R.id.rbEditOpt2) chosenCorrect = 2;
                    else if (checkedId == R.id.rbEditOpt3) chosenCorrect = 3;
                    else if (checkedId == R.id.rbEditOpt4) chosenCorrect = 4;

                    // Update question model fields
                    question.setQuestion(qText);
                    question.setOption1(o1);
                    question.setOption2(o2);
                    question.setOption3(o3);
                    question.setOption4(o4);
                    question.setCorrectAnswer(chosenCorrect);

                    // Execute SQLite UPDATE via DatabaseHelper
                    int rowsUpdated = databaseHelper.updateQuestion(question);
                    if (rowsUpdated > 0) {
                        Toast.makeText(this, "Question updated successfully! (SQLite UPDATE)", Toast.LENGTH_SHORT).show();
                        loadQuestions();
                    } else {
                        Toast.makeText(this, "Failed to update question", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null);

        builder.create().show();
    }
}
