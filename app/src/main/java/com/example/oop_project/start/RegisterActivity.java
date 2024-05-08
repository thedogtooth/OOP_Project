/**
 * @author Renato Burgos Hidalgo
 */
package com.example.oop_project.start;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

/**
 * Clase para crear un nuevo usuario.
 */
public class RegisterActivity extends AppCompatActivity {
    /**
     * Para seleccionar entre varias opciones predeterminadas. La opción inicial es "Casa Central".
     */
    private Spinner dropdown;
    /**
     * Se conecta a la base de datos de Firestore.
     */
    private FirebaseFirestore db;
    /**
     * Para inicializar una nueva sesión en la aplicación, además de guardar los datos del usuario en la sección Authentication.
     */
    private FirebaseAuth mAuth;

    /**
     * Método inicial cuando se abre la actividad.
     * @param savedInstanceState Si la actividad es creada nuevamente desde un estado previo, este es el estado.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        dropdown = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.headquarters, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Ocurre cuando se presiona el botón para registrarse.
     * @param view Vista actual de la actividad.
     */
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
            String campus = dropdown.getSelectedItem().toString();

            mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),"¡Cuenta creada correctamente!",Toast.LENGTH_SHORT).show();
                        User user = new User(mail, phone, campus, username_str);
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

    /**
     * Se verifica si lo que el usuario ingresa como correo es válido.
     * @param email correo ingresado.
     * @return verdadero si es válido o falso si no.
     */
    public boolean isValidEmailAddress(String email) {
        String ePattern = "^.+@.+\\..+$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
