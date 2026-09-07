package com.example.smartquiz;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * RecyclerView Adapter for displaying quiz history records.
 * Demonstrates Adapter pattern, ViewHolder pattern, and click callbacks.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    public interface OnHistoryItemListener {
        void onDeleteClick(QuizHistory history, int position);
    }

    private Context context;
    private List<QuizHistory> historyList;
    private OnHistoryItemListener listener;

    public HistoryAdapter(Context context, List<QuizHistory> historyList, OnHistoryItemListener listener) {
        this.context = context;
        this.historyList = historyList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        QuizHistory item = historyList.get(position);

        holder.tvCategory.setText(item.getCategory());
        holder.tvScore.setText(item.getScore() + " / " + item.getTotalQuestions());
        holder.tvPercentage.setText(String.format("%.0f%%", item.getPercentage()));
        holder.tvPerformance.setText(item.getPerformance());
        holder.tvDate.setText(item.getDate());

        // Dynamic badge color based on performance
        String perf = item.getPerformance();
        if (perf.equalsIgnoreCase("Excellent")) {
            holder.tvPerformance.setTextColor(Color.parseColor("#16A34A"));
        } else if (perf.equalsIgnoreCase("Good")) {
            holder.tvPerformance.setTextColor(Color.parseColor("#2563EB"));
        } else if (perf.equalsIgnoreCase("Average")) {
            holder.tvPerformance.setTextColor(Color.parseColor("#D97706"));
        } else {
            holder.tvPerformance.setTextColor(Color.parseColor("#DC2626"));
        }

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(item, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void updateData(List<QuizHistory> newList) {
        this.historyList = newList;
        notifyDataSetChanged();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvScore, tvPercentage, tvPerformance, tvDate;
        ImageButton btnDelete;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvHistoryCategory);
            tvScore = itemView.findViewById(R.id.tvHistoryScore);
            tvPercentage = itemView.findViewById(R.id.tvHistoryPercentage);
            tvPerformance = itemView.findViewById(R.id.tvHistoryPerformance);
            tvDate = itemView.findViewById(R.id.tvHistoryDate);
            btnDelete = itemView.findViewById(R.id.btnDeleteHistoryItem);
        }
    }
}
