/**
 * @author Renato Burgos Hidalgo
 */
package com.example.oop_project.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oop_project.HomeActivity;
import com.example.oop_project.R;
import com.example.oop_project.SettingsActivity;
import com.example.oop_project.model.User;
import com.example.oop_project.start.LoginActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Fragmento que muestra el perfil del usuario.
 */
public class ProfileFragment extends Fragment {
    /**
     * Vista del fragmento.
     */
    private View mView;
    private androidx.appcompat.widget.Toolbar toolbar;
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
    private String email;
    private User profile;

    /**
     * Constructor del fragmento
     */
    public ProfileFragment() {

    }

    /**
     * Método que se llama cada vez que empieza el fragmento.
     * @param savedInstanceState Si el fragmento es creado nuevamente desde un estado previo, este es el estado.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        user = HomeActivity.getUser();
        db = HomeActivity.getDb();
        email = HomeActivity.getEmail();
        profile = HomeActivity.getProfile();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (profile.getUsername() != null) {
            userTextView.setText(profile.getUsername());
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
        userTextView = mView.findViewById(R.id.nameTextView);

        Button signOutButton = (Button) mView.findViewById(R.id.signOut);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar = mView.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.profile_top);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_favorite:
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    return true;
                default:
                    return false;
            }
        });
    }

    /**
     * Método para cerrar sesión.
     */
    private void signOut() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
        builder
                .setTitle(R.string.signOut)
                .setMessage(R.string.dialogMessageSignOut)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getActivity(), R.string.signedOut, Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent signOut = new Intent(getActivity(), LoginActivity.class);
                        startActivity(signOut);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).show();
    }
}