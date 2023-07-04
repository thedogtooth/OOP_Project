/**
 * Fragmento para crear confesiones.
 * @author Renato Burgos Hidalgo
 */
package com.example.oop_project.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.oop_project.HomeActivity;
import com.example.oop_project.R;
import com.example.oop_project.model.Confession;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateActivity extends Fragment {
    /**
     * Email a obtener del usuario actual.
     */
    private String email;
    /**
     * Checkbock para definir si una confesión es anónima o no.
     */
    private CheckBox checkBox;
    /**
     * Vista del fragmento.
     */
    private View mView;
    /**
     * Usuario logueado actualmente.
     */
    private FirebaseUser user;
    /**
     * Instancia de la base de datos.
     */
    private FirebaseFirestore db;

    /**
     * Constructor del fragmento.
     */
    public CreateActivity() {

    }

    /**
     * Método que se llama cada vez que empieza el fragmento.
     * @param savedInstanceState Si el fragmento es creado nuevamente desde un estado previo, este es el estado.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (user != null) {
            email = user.getEmail();
        }
    }

    /**
     * Similar a onCreate pero para fragmentos.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return la vista del fragmento.
     */
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

    /**
     * Para publicar lo escrito.
     */
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
