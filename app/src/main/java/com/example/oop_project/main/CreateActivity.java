package com.example.oop_project.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.oop_project.HomeActivity;
import com.example.oop_project.R;
import com.example.oop_project.model.Confession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateActivity extends Fragment {
    private String email;
    private CheckBox checkBox;
    private View mView;
    private FirebaseUser user;
    private FirebaseFirestore db;
    public CreateActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (user != null) {
            email = user.getEmail();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.create_activity, container, false);
        checkBox = (CheckBox) mView.findViewById(R.id.anonCheckbox);

        Button button = (Button) mView.findViewById(R.id.publish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishConfession();
            }
        });

        return mView;
    }

    private void publishConfession() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        EditText text = (EditText) mView.findViewById(R.id.editText);
        Confession confession = new Confession(email, text.getText().toString(), checkBox.isChecked(),
                Timestamp.valueOf(dateFormat.format(calendar.getTime())));
        db.collection("confessions")
                .add(confession)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        startActivity(new Intent(getActivity(), HomeActivity.class));
                        Toast.makeText(getActivity(), "¡Confesión publicada!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
