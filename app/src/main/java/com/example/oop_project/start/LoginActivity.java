/**
 * Clase para iniciar sesión.
 * @author Renato Burgos Hidalgo
 */
package com.example.oop_project.start;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {
    /**
     * Clase inicial si es que no hay una sesión iniciada.
     * @param mAuth se conecta a Firebase para ver si hay una conexión actual.
     * @param homeActivity para comenzar la actividad con los 3 fragmentos.
     */
    private FirebaseAuth mAuth;
    private Intent homeActivity;

    /**
     * Método inicial cuando se abre la actividad.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();
        homeActivity = new Intent(LoginActivity.this, HomeActivity.class);
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
        } else Log.i("chao", "no hay sesion");
    }

    /**
     * Ocurre cuando se aprieta el botón "Iniciar sesión".
     * @param view
     */
    public void login(View view){
        EditText email = (EditText) findViewById(R.id.loginEmail);
        EditText password = (EditText) findViewById(R.id.loginPassword);
        TextInputLayout emailLayout = findViewById(R.id.login_emailLayout);
        TextInputLayout passLayout = findViewById(R.id.login_passlayout);

        if (email.getText().toString().isEmpty()){
            emailLayout.setError("Ingrese un correo");
            return;
        }
        if (password.getText().toString().isEmpty()){
            passLayout.setError("Ingrese una contraseña");
            return;
        }
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("si", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(homeActivity);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast toast = Toast.makeText(LoginActivity.this, "Correo o contraseña inválidas", Toast.LENGTH_SHORT);
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

}
