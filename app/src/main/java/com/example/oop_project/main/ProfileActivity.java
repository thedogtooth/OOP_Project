/**
 * @author Renato Burgos Hidalgo
 */
package com.example.oop_project.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.oop_project.R;
import com.example.oop_project.start.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Fragmento que muestra el perfil del usuario.
 */
public class ProfileActivity extends Fragment {
    /**
     * Vista del fragmento.
     */
    private View mView;
    /**
     * Nombre de usuario.
     */
    private String username;
    /**
     * Se despliega el nombre del usuario.
     */
    private TextView userTextView;
    /**
     * Usuario logueado actualmente.
     */
    private FirebaseUser user;
    /**
     * Instancia de la base de datos.
     */
    private FirebaseFirestore db;

    /**
     * Constructor del fragmento
     */
    public ProfileActivity() {

    }

    /**
     * Método que se llama cada vez que empieza el fragmento.
     * @param savedInstanceState Si el fragmento es creado nuevamente desde un estado previo, este es el estado.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (user != null) {
            String email = user.getEmail();
            // Create a reference to the cities collection
            db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (email.equals(document.getData().get("email"))) {
                                        username = (String) document.getData().get("username");
                                        userTextView.setText(username);
                                    }
                                }
                            } else {
                                Log.d("error", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (username != null) {
            userTextView.setText(username);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
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
        setHasOptionsMenu(true);
        mView = inflater.inflate(R.layout.profile_activity, container, false);
        userTextView = (TextView) mView.findViewById(R.id.nameTextView);

        Button button = (Button) mView.findViewById(R.id.signOut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Hola", "ho");
        inflater.inflate(R.menu.profile_menu, menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            //do your function here
            Toast.makeText(getActivity(), "Settings", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_second) {
            //do your function here
            Toast.makeText(getActivity(), "Sort", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Método para cerrar sesión.
     */
    private void signOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle(R.string.dialogTitle).setMessage(R.string.dialogMessage);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getActivity(), "¡Sesión cerrada exitosamente!", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent signOut = new Intent(getActivity(), LoginActivity.class);
                startActivity(signOut);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}