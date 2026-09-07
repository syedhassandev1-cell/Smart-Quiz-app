package com.example.smartquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * CategoriesFragment displaying all available quiz categories from SQLite.
 * Demonstrates: Fragments, RecyclerView, Custom Adapters, and Intent navigation.
 */
public class CategoriesFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener {

    private RecyclerView rvCategories;
    private DatabaseHelper databaseHelper;
    private CategoryAdapter adapter;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        rvCategories = view.findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(requireContext());
        loadCategories();

        return view;
    }

    private void loadCategories() {
        List<Category> categories = databaseHelper.getAllCategories();
        adapter = new CategoryAdapter(getContext(), categories, this);
        rvCategories.setAdapter(adapter);
    }

    @Override
    public void onCategorySelected(Category category) {
        // Launch QuizActivity with explicit Intent passing category details
        Intent intent = new Intent(getActivity(), QuizActivity.class);
        intent.putExtra(QuizActivity.EXTRA_CATEGORY_ID, category.getId());
        intent.putExtra(QuizActivity.EXTRA_CATEGORY_NAME, category.getName());
        startActivity(intent);
    }
}
