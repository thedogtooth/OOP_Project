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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.Task;

import com.example.oop_project.R;
import com.example.oop_project.model.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public void register(View view){
        EditText username = (EditText) findViewById(R.id.registerUsername);
        EditText email = (EditText) findViewById(R.id.registerEmail);
        EditText telephone = (EditText) findViewById(R.id.registerPhone);
        EditText password = (EditText) findViewById(R.id.registerPassword);
        EditText password2 = (EditText) findViewById(R.id.registerPasswordConfirm);

        if(!password.getText().toString().equals(password2.getText().toString())){
            Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
            password.setText(null);
            password2.setText(null);
        }
        else if(!isValidEmailAddress(email.getText().toString())){
            Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
        }
        else {
            String username_str = username.getText().toString();
            String mail = email.getText().toString();
            int phone = Integer.parseInt(telephone.getText().toString());
            String pass = password.getText().toString();

            mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),"¡Cuenta creada correctamente!",Toast.LENGTH_SHORT).show();
                        User user = new User(mail, phone, username_str);
                        db.collection("users")
                                .add(user)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("Success", "DocumentSnapshot written with ID: " + documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"Ya hay una cuenta con ese correo",Toast.LENGTH_SHORT).show();
                                    }
                                });
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }
                }
            });
        }
        // Intent register = new Intent(RegisterActivity.this, LoginActivity.class);
        // startActivity(register);
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^.+@.+\\..+$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
