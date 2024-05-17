/**
 * @author Renato Burgos Hidalgo
 */
package com.example.oop_project.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Fragmento para crear confesiones.
 */
public class CreateFragment extends Fragment {
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
    private EditText text;
    private TextInputLayout textLayout;
    /**
     * Constructor del fragmento.
     */
    public CreateFragment() {

    }

    /**
     * Método que se llama cada vez que empieza el fragmento.
     * @param savedInstanceState Si el fragmento es creado nuevamente desde un estado previo, este es el estado.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = HomeActivity.getUser();
        db = HomeActivity.getDb();
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
        checkBox = mView.findViewById(R.id.anonCheckbox);

        text = mView.findViewById(R.id.editText);
        textLayout = mView.findViewById(R.id.textInputLayout);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (text.getText().toString().trim().isEmpty()) {
                    textLayout.setError(getString(R.string.emptyConfession));
                } else {
                    textLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Button button = mView.findViewById(R.id.publish);
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

        textLayout = mView.findViewById(R.id.textInputLayout);

        if (text.getText().toString().trim().isEmpty()) {
            return;
        }

        Confession confession = new Confession(email, text.getText().toString(), checkBox.isChecked(),
                Timestamp.valueOf(dateFormat.format(calendar.getTime())));
        db.collection("confessions")
                .add(confession)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        startActivity(new Intent(getActivity(), HomeActivity.class));
                        Toast.makeText(getActivity(), R.string.publishedConfession, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}