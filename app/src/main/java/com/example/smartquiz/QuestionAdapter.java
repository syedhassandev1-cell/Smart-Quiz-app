package com.example.smartquiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter for managing and updating SQLite questions.
 * Demonstrates CRUD UPDATE presentation.
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    public interface OnQuestionEditListener {
        void onEditQuestion(Question question);
    }

    private Context context;
    private List<Question> questionList;
    private OnQuestionEditListener listener;

    public QuestionAdapter(Context context, List<Question> questionList, OnQuestionEditListener listener) {
        this.context = context;
        this.questionList = questionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question q = questionList.get(position);
        holder.tvQuestionNumber.setText("Q" + (position + 1) + " (ID: " + q.getId() + ")");
        holder.tvQuestionText.setText(q.getQuestion());
        holder.tvOptionsSummary.setText("Correct Answer: Option " + q.getCorrectAnswer() + " (" + q.getCorrectAnswerText() + ")");

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditQuestion(q);
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public void updateList(List<Question> newList) {
        this.questionList = newList;
        notifyDataSetChanged();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNumber, tvQuestionText, tvOptionsSummary;
        Button btnEdit;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNum);
            tvQuestionText = itemView.findViewById(R.id.tvQuestionTitle);
            tvOptionsSummary = itemView.findViewById(R.id.tvQuestionSummary);
            btnEdit = itemView.findViewById(R.id.btnEditQuestion);
        }
    }
}
