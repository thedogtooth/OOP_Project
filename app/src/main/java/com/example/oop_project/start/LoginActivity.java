/**
 * @author Renato Burgos Hidalgo
 */
package com.example.oop_project.start;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oop_project.HomeActivity;
import com.example.oop_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Clase para iniciar sesión.
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Se conecta a Firebase para ver si hay una conexión actual.
     */
    private FirebaseAuth mAuth;
    /**
     * Para comenzar la actividad con los 3 fragmentos.
     */
    private Intent homeActivity;

    /**
     * Método inicial cuando se abre la actividad.
     * @param savedInstanceState Si la actividad es creada nuevamente desde un estado previo, este es el estado.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();
        homeActivity = new Intent(LoginActivity.this, HomeActivity.class);

        EditText passwordText = findViewById(R.id.loginPassword);
        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                login(null);
                return false;
            }
        });
    }

    /**
     * Método que ocurre después de onCreate(). Si se sale de la aplicación pero no se cierra ocurre este método.
     */
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(homeActivity);
        } else {
            Log.i("Signed Out", "Haven't logged in");
        }
    }

    /**
     * Ocurre cuando se aprieta el botón "Iniciar sesión".
     * @param view
     */
    public void login(View view){
        EditText email = findViewById(R.id.loginEmail);
        EditText password = findViewById(R.id.loginPassword);
        TextInputLayout emailLayout = findViewById(R.id.login_emailLayout);
        TextInputLayout passLayout = findViewById(R.id.login_passlayout);

        if (email.getText().toString().isEmpty()) {
            emailLayout.setError(getString(R.string.noEmail));
            return;
        } else {
            emailLayout.setError(null);
        }
        if (password.getText().toString().isEmpty()) {
            passLayout.setError(getString(R.string.noPassword));
            return;
        } else {
            emailLayout.setError(null);
        }
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Success", "signInWithEmail:success");
                            mAuth.getCurrentUser();
                            startActivity(homeActivity);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast toast = Toast.makeText(LoginActivity.this, R.string.wrongLogin, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }

    public void toForgotPassword(View view){
        Toast.makeText(getApplicationContext(),"Próximamente",Toast.LENGTH_SHORT).show();
    }

    /**
     * Para registrar un nuevo usuario.
     * @param view
     */
    public void register(View view) {
        Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(register);
    }

    /**
     * Este método ocurre cuando alguien quiere volver atrás en esta actividad. Se cierra la aplicación en este caso.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^.+@.+\\..+$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

}
