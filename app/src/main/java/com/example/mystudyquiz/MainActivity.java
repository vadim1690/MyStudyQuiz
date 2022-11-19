package com.example.mystudyquiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NavController navController = Navigation.findNavController(this, R.id.activity_main_nav_host_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.answeringQuizFragment)
                    bottomNavigationView.setVisibility(View.GONE);
                else if (navDestination.getId() == R.id.finishedQuizFragment)
                    bottomNavigationView.setVisibility(View.GONE);
                else if (navDestination.getId() == R.id.answersReportBottomDialog)
                    bottomNavigationView.setVisibility(View.GONE);
                else
                    bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}