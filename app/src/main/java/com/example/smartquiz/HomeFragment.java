package com.example.smartquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * HomeFragment providing the primary user overview.
 * Demonstrates: Fragments, SharedPreferences data retrieval, SQLite aggregation queries,
 * and Intent/Tab navigation.
 */
public class HomeFragment extends Fragment {

    private TextView tvHomeUserName;
    private TextView tvHomeStatTotal, tvHomeStatBest, tvHomeStatAvg;
    private DatabaseHelper databaseHelper;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvHomeUserName = view.findViewById(R.id.tvHomeUserName);
        tvHomeStatTotal = view.findViewById(R.id.tvHomeStatTotal);
        tvHomeStatBest = view.findViewById(R.id.tvHomeStatBest);
        tvHomeStatAvg = view.findViewById(R.id.tvHomeStatAvg);

        Button btnQuickStart = view.findViewById(R.id.btnQuickStart);
        Button btnNavCategories = view.findViewById(R.id.btnNavCategories);
        Button btnNavHistory = view.findViewById(R.id.btnNavHistory);

        databaseHelper = new DatabaseHelper(requireContext());

        // Quick start triggers a quick Mixed Quiz
        btnQuickStart.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuizActivity.class);
            intent.putExtra(QuizActivity.EXTRA_CATEGORY_ID, 3); // 3 is Mixed Quiz
            intent.putExtra(QuizActivity.EXTRA_CATEGORY_NAME, "Mixed Quiz");
            startActivity(intent);
        });

        // Navigate to Categories tab
        btnNavCategories.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTab(R.id.nav_categories);
            }
        });

        // Navigate to History tab
        btnNavHistory.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTab(R.id.nav_history);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserProfileAndStats();
    }

    private void loadUserProfileAndStats() {
        // Retrieve User Name from SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences(SplashActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String name = prefs.getString(SplashActivity.KEY_USER_NAME, "Student");
        tvHomeUserName.setText(name);

        // Retrieve Statistics from SQLite
        int total = databaseHelper.getTotalQuizzesCount();
        int best = databaseHelper.getBestScore();
        double avg = databaseHelper.getAveragePercentage();

        tvHomeStatTotal.setText(String.valueOf(total));
        tvHomeStatBest.setText(String.valueOf(best));
        tvHomeStatAvg.setText(String.format("%.0f%%", avg));
    }
}
