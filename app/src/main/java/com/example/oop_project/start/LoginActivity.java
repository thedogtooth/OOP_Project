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
    private FirebaseAuth mAuth;
    private Intent homeActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();
        homeActivity = new Intent(LoginActivity.this, HomeActivity.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(homeActivity);
        } else Log.i("chao", "no hay sesion");
    }

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

    public void register(View view) {
        Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(register);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
