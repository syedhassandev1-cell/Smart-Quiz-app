package com.example.smartquiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

/**
 * SettingsFragment managing persistent preferences via SharedPreferences
 * and database maintenance actions (CRUD UPDATE & DELETE).
 * Demonstrates: SharedPreferences reading and editing, SwitchCompat toggles,
 * Dialog alerts, and SQLite management.
 */
public class SettingsFragment extends Fragment {

    private EditText etSettingsUserName;
    private Button btnSaveName, btnManageQuestions, btnClearHistoryFromSettings;
    private SwitchCompat switchDarkMode, switchSound;

    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        etSettingsUserName = view.findViewById(R.id.etSettingsUserName);
        btnSaveName = view.findViewById(R.id.btnSaveName);
        switchDarkMode = view.findViewById(R.id.switchDarkMode);
        switchSound = view.findViewById(R.id.switchSound);
        btnManageQuestions = view.findViewById(R.id.btnManageQuestions);
        btnClearHistoryFromSettings = view.findViewById(R.id.btnClearHistoryFromSettings);

        sharedPreferences = requireActivity().getSharedPreferences(SplashActivity.PREFS_NAME, Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(requireContext());

        loadCurrentSettings();

        // Save User Name
        btnSaveName.setOnClickListener(v -> saveUserName());

        // Dark Mode Toggle
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(SplashActivity.KEY_DARK_MODE, isChecked).apply();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // Sound Toggle
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(SplashActivity.KEY_SOUND_ENABLED, isChecked).apply();
            Toast.makeText(getContext(), isChecked ? "Sound effects enabled" : "Sound effects muted", Toast.LENGTH_SHORT).show();
        });

        // Manage Questions (CRUD: UPDATE)
        btnManageQuestions.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ManageQuestionsActivity.class);
            startActivity(intent);
        });

        // Clear History
        btnClearHistoryFromSettings.setOnClickListener(v -> showClearHistoryDialog());

        return view;
    }

    private void loadCurrentSettings() {
        String currentName = sharedPreferences.getString(SplashActivity.KEY_USER_NAME, "Student");
        etSettingsUserName.setText(currentName);

        boolean isDarkMode = sharedPreferences.getBoolean(SplashActivity.KEY_DARK_MODE, false);
        switchDarkMode.setChecked(isDarkMode);

        boolean isSound = sharedPreferences.getBoolean(SplashActivity.KEY_SOUND_ENABLED, true);
        switchSound.setChecked(isSound);
    }

    private void saveUserName() {
        String newName = etSettingsUserName.getText().toString().trim();
        if (TextUtils.isEmpty(newName)) {
            Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to SharedPreferences
        sharedPreferences.edit().putString(SplashActivity.KEY_USER_NAME, newName).apply();
        Toast.makeText(getContext(), "Name updated successfully!", Toast.LENGTH_SHORT).show();
    }

    private void showClearHistoryDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.clear_all_history)
                .setMessage(R.string.delete_history_confirm)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    databaseHelper.clearAllHistory();
                    Toast.makeText(getContext(), "All quiz history cleared!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
