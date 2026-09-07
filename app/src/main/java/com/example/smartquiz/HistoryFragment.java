package com.example.smartquiz;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * HistoryFragment displaying past quiz attempts from SQLite.
 * Demonstrates: RecyclerView, Cursor-based SELECT queries, Category filtering,
 * Single-item DELETE, and Clear-All DELETE with AlertDialog confirmation.
 */
public class HistoryFragment extends Fragment implements HistoryAdapter.OnHistoryItemListener {

    private RecyclerView rvHistory;
    private TextView tvEmptyHistory;
    private Spinner spinnerFilterCategory;
    private Button btnClearAllHistory;

    private DatabaseHelper databaseHelper;
    private HistoryAdapter adapter;
    private List<QuizHistory> historyList = new ArrayList<>();
    private String currentSelectedCategory = "All";

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        rvHistory = view.findViewById(R.id.rvHistory);
        tvEmptyHistory = view.findViewById(R.id.tvEmptyHistory);
        spinnerFilterCategory = view.findViewById(R.id.spinnerFilterCategory);
        btnClearAllHistory = view.findViewById(R.id.btnClearAllHistory);

        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseHelper = new DatabaseHelper(requireContext());

        setupCategoryFilterSpinner();

        btnClearAllHistory.setOnClickListener(v -> showClearAllConfirmationDialog());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHistoryData(currentSelectedCategory);
    }

    private void setupCategoryFilterSpinner() {
        String[] categories = new String[]{"All", "General Knowledge", "Computer Science", "Mixed Quiz"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );
        spinnerFilterCategory.setAdapter(spinnerAdapter);

        spinnerFilterCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSelectedCategory = categories[position];
                loadHistoryData(currentSelectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadHistoryData(String categoryFilter) {
        if (categoryFilter.equalsIgnoreCase("All")) {
            historyList = databaseHelper.getAllQuizHistory();
        } else {
            historyList = databaseHelper.getQuizHistoryByCategory(categoryFilter);
        }

        if (historyList == null || historyList.isEmpty()) {
            tvEmptyHistory.setVisibility(View.VISIBLE);
            rvHistory.setVisibility(View.GONE);
        } else {
            tvEmptyHistory.setVisibility(View.GONE);
            rvHistory.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = new HistoryAdapter(getContext(), historyList, this);
                rvHistory.setAdapter(adapter);
            } else {
                adapter.updateData(historyList);
            }
        }
    }

    @Override
    public void onDeleteClick(QuizHistory history, int position) {
        // Individual item deletion (CRUD: DELETE)
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Record")
                .setMessage("Delete this attempt from " + history.getDate() + "?")
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    databaseHelper.deleteHistoryById(history.getId());
                    Toast.makeText(getContext(), "Record deleted", Toast.LENGTH_SHORT).show();
                    loadHistoryData(currentSelectedCategory);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showClearAllConfirmationDialog() {
        // Bulk deletion (CRUD: DELETE all)
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.clear_all_history)
                .setMessage(R.string.delete_history_confirm)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    databaseHelper.clearAllHistory();
                    Toast.makeText(getContext(), "All history deleted", Toast.LENGTH_SHORT).show();
                    loadHistoryData(currentSelectedCategory);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
