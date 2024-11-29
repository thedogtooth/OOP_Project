/**
 * @author Renato Burgos Hidalgo
 */
package com.example.oop_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.oop_project.main.CreateFragment;
import com.example.oop_project.main.MainFragment;
import com.example.oop_project.main.ProfileFragment;
import com.example.oop_project.main.SearchFragment;
import com.example.oop_project.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

/**
 * Clase que muestra la barra inferior de la aplicación
 */
public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    /**
     * Actividad con las confesiones. Feed de la aplicación.
     */
    MainFragment mainFragment = new MainFragment();
    /**
     * Segunda actividad donde se escriben las confesiones.
     */
    SearchFragment searchFragment = new SearchFragment();
    CreateFragment createFragment = new CreateFragment();
    /**
     * Última actividad para cerrar sesión.
     */
    ProfileFragment profileFragment = new ProfileFragment();
    /**
     * Barra inferior.
     */
    BottomNavigationView bottomNavigationView;
    /**
     * Variable booleana para cerrar la aplicación. Se debe apretar dos veces el botón de retroceso para cerrar la aplicación.
     */
    boolean doubleBackToExitPressedOnce = false;
    public static FirebaseUser user;
    public static FirebaseFirestore db;
    public static String email;
    public static User profile;

    /**
     * Método inicial cuando se crea la aplicación.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        email = user.getEmail();
        getUsersInfo();

        bottomNavigationView = findViewById(R.id.home_navigator);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        loadFragment(mainFragment);
    }

    /**
     * Para seleccionar alguna de las vistas del menú
     * @param item El ítem seleccionado.
     * @return true
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (R.id.firstActivity == item.getItemId()) {
            loadFragment(mainFragment);
        }
        if (R.id.searchActivity == item.getItemId()) {
            loadFragment(searchFragment);
        }
        if (R.id.secondActivity == item.getItemId()) {
            loadFragment(createFragment);
        }
        if (R.id.thirdActivity == item.getItemId()) {
            loadFragment(profileFragment);
        }
        return true;
    }

    /**
     * Se carga el método seleccionado.
     * @param fragment fragmento seleccionado en el método anterior.
     */
    public void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_home, fragment, null).setReorderingAllowed(true).addToBackStack("").commit();
    }

    /**
     * La aplicación espera 2 segundos cuando se presiona atrás para que el usuario presione nuevamente, y así cerrar la aplicación.
     */
    public void onBackPressed() {
        Fragment fragment = getVisibleFragment();
        if (fragment instanceof MainFragment) {
            if (doubleBackToExitPressedOnce) {
                Intent intent = new Intent(Intent.ACTION_MAIN); // Esta seccion es para cerrar la app sin doble verificación
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.backConfirm, Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.firstActivity);
            loadFragment(mainFragment);
        }
    }

    /**
     * Método para obtener el fragmento visible.
     * @return el fragmento visible o null.
     */
    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    public static FirebaseUser getUser() {
        return user;
    }

    public static FirebaseFirestore getDb() {
        return db;
    }

    public static String getEmail() {
        return email;
    }

    public static User getProfile() {
        return profile;
    }

    private void getUsersInfo() {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (email.equals(document.getData().get("email"))) {
                                    Map<String, Object> data = document.getData();
                                    profile = new User(document.getId(),email, Integer.parseInt(data.get("phone").toString()),
                                            (String) data.get("campus"), (String) data.get("username"));
                                }
                            }
                        } else {
                            Log.d("error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}