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
 * RecyclerView Adapter for Quiz Categories.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryClickListener {
        void onCategorySelected(Category category);
    }

    private Context context;
    private List<Category> categoryList;
    private OnCategoryClickListener listener;

    public CategoryAdapter(Context context, List<Category> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.tvName.setText(category.getName());
        holder.tvDescription.setText(category.getDescription());

        holder.btnStartQuiz.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategorySelected(category);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategorySelected(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription;
        Button btnStartQuiz;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCategoryName);
            tvDescription = itemView.findViewById(R.id.tvCategoryDescription);
            btnStartQuiz = itemView.findViewById(R.id.btnStartCategoryQuiz);
        }
    }
}
