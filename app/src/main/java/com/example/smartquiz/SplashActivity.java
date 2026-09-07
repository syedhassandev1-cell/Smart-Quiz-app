package com.example.smartquiz;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * Splash and Welcome Activity.
 * Demonstrates: Activity lifecycle, SharedPreferences for persistent settings,
 * Intent navigation, Custom Dialogs, and theme setup.
 */
public class SplashActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "SmartQuizPrefs";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_DARK_MODE = "dark_mode";
    public static final String KEY_SOUND_ENABLED = "sound_enabled";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load preferences before rendering to apply theme if dark mode is active
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean(KEY_DARK_MODE, false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button btnGetStarted = findViewById(R.id.btnGetStarted);

        btnGetStarted.setOnClickListener(v -> handleGetStarted());
    }

    private void handleGetStarted() {
        String savedName = sharedPreferences.getString(KEY_USER_NAME, "");

        // If user name has never been set, show dialog to ask for name
        if (TextUtils.isEmpty(savedName)) {
            showNameInputDialog();
        } else {
            navigateToMain();
        }
    }

    private void showNameInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_name, null);
        EditText etName = dialogView.findViewById(R.id.etDialogUserName);

        builder.setView(dialogView)
                .setTitle(R.string.app_name)
                .setCancelable(false)
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    String inputName = etName.getText().toString().trim();
                    if (TextUtils.isEmpty(inputName)) {
                        inputName = "Student";
                    }
                    // Save to SharedPreferences
                    sharedPreferences.edit().putString(KEY_USER_NAME, inputName).apply();
                    Toast.makeText(SplashActivity.this, "Welcome, " + inputName + "!", Toast.LENGTH_SHORT).show();
                    navigateToMain();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    // Use default name if canceled
                    sharedPreferences.edit().putString(KEY_USER_NAME, "Student").apply();
                    navigateToMain();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void navigateToMain() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Remove SplashActivity from back stack
    }
}
