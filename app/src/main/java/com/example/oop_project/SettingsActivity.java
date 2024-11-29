package com.example.oop_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oop_project.model.User;
import com.example.oop_project.start.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private User user;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        user = HomeActivity.getProfile();
        db = HomeActivity.getDb();
    }

    public void deleteAccount(View view){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder
                .setTitle(R.string.deleteAccount)
                .setMessage(R.string.dialogMessageDeleteAccount)
                .setPositiveButton(R.string.ok, (dialog, id) -> {
                    // User clicked OK button
                    db.collection("users")
                            .document(user.getId())
                            .update("deleted", true)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            FirebaseAuth.getInstance().getCurrentUser()
                                                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("Success", "User account deleted.");
                                                                FirebaseAuth.getInstance();
                                                                Toast.makeText(getApplicationContext(), R.string.accountDeleted, Toast.LENGTH_SHORT).show();
                                                                Intent delete = new Intent(SettingsActivity.this, LoginActivity.class);
                                                                startActivity(delete);
                                                            } else {
                                                                Log.d("Error", "User account not deleted.");
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    // User cancelled the dialog
                }).show();

    }
}
