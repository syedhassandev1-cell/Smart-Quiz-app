package com.example.smartquiz;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * MainActivity hosting BottomNavigationView and modular Fragments.
 * Demonstrates: Activity, FragmentManager, FragmentTransaction, BottomNavigationView.
 */
public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TARGET_TAB = "extra_target_tab";
    public static final int TAB_HOME = 1;
    public static final int TAB_CATEGORIES = 2;
    public static final int TAB_HISTORY = 3;
    public static final int TAB_SETTINGS = 4;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment = null;

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_categories) {
                selectedFragment = new CategoriesFragment();
            } else if (itemId == R.id.nav_history) {
                selectedFragment = new HistoryFragment();
            } else if (itemId == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });

        // Check if an Intent specified a particular tab to open (e.g. from Result screen)
        int targetTab = getIntent().getIntExtra(EXTRA_TARGET_TAB, TAB_HOME);
        if (targetTab == TAB_HISTORY) {
            bottomNavigationView.setSelectedItemId(R.id.nav_history);
        } else if (targetTab == TAB_CATEGORIES) {
            bottomNavigationView.setSelectedItemId(R.id.nav_categories);
        } else {
            // Load Home Fragment as default if starting fresh
            if (savedInstanceState == null) {
                loadFragment(new HomeFragment());
            }
        }
    }

    /**
     * Replaces container view with the selected Fragment.
     */
    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    /**
     * Navigates to a specific tab via bottom navigation.
     */
    public void navigateToTab(int menuItemId) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(menuItemId);
        }
    }
}
