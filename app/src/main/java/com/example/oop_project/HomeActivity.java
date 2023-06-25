package com.example.oop_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.oop_project.main.CreateActivity;
import com.example.oop_project.main.MainActivity;
import com.example.oop_project.main.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    MainActivity firstActivity = new MainActivity();
    CreateActivity createActivity = new CreateActivity();
    ProfileActivity profileActivity = new ProfileActivity();
    BottomNavigationView bottomNavigationView;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        bottomNavigationView = findViewById(R.id.home_navigator);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(firstActivity);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (R.id.firstActivity == item.getItemId()) {
            loadFragment(firstActivity);
        }
        if (R.id.secondActivity == item.getItemId()) {
            loadFragment(createActivity);
        }
        if (R.id.thirdActivity == item.getItemId()) {
            loadFragment(profileActivity);
        }
        return true;
    }
    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_home, fragment);
        transaction.commit();
    }

    public void onBackPressed() {
        Fragment fragment = getVisibleFragment();
        if (fragment instanceof MainActivity) {
            if (doubleBackToExitPressedOnce) {
                Intent intent = new Intent(Intent.ACTION_MAIN); // Esta seccion es para cerrar la app sin doble verificación
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Presiona atrás de nuevo para salir", Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        } else {
            loadFragment(firstActivity);
        }
    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }
}