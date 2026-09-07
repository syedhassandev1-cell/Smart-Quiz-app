package com.example.smartquiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * CategoryActivity providing an explicit Activity navigation target for category selection.
 * Demonstrates: Activity navigation via Intent, RecyclerView, and Intent Extras.
 */
public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {

    private RecyclerView rvCategoryActivity;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        ImageButton btnBack = findViewById(R.id.btnBackCategory);
        btnBack.setOnClickListener(v -> finish());

        rvCategoryActivity = findViewById(R.id.rvCategoryActivity);
        rvCategoryActivity.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        List<Category> categories = databaseHelper.getAllCategories();

        CategoryAdapter adapter = new CategoryAdapter(this, categories, this);
        rvCategoryActivity.setAdapter(adapter);
    }

    @Override
    public void onCategorySelected(Category category) {
        Intent intent = new Intent(CategoryActivity.this, QuizActivity.class);
        intent.putExtra(QuizActivity.EXTRA_CATEGORY_ID, category.getId());
        intent.putExtra(QuizActivity.EXTRA_CATEGORY_NAME, category.getName());
        startActivity(intent);
    }
}
