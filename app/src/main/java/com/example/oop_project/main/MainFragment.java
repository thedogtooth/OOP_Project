/**
 * @author Renato Burgos Hidalgo
 */
package com.example.oop_project.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.oop_project.HomeActivity;
import com.example.oop_project.Post;
import com.example.oop_project.R;
import com.example.oop_project.model.Confession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento del feed.
 */
public class MainFragment extends Fragment {
    /**
     * Vista actual del fragmento.
     */
    private View mView;
    /**
     * Instancia de la base de datos.
     */
    private FirebaseFirestore db;
    /**
     * Lista de las confesiones a mostrar.
     */
    private List<Confession> confessions;
    /**
     * Para crear y asignar posts.
     */
    private Post post;
    /**
     * Para mostrar los posts.
     */
    private RecyclerView recyclerView;
    private boolean loaded;
    private LinearLayout loadingIcon;
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * Constructor de la clase.
     */
    public MainFragment() {

    }

    /**
     * Método que se llama cada vez que empieza el fragmento.
     * @param savedInstanceState Si el fragmento es creado nuevamente desde un estado previo, este es el estado.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        confessions = new ArrayList<>();

        db = HomeActivity.getDb();
        loadPosts();
    }

    /**
     * Similar a onCreate pero para fragmentos.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return la vista del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.main_activity, container, false);
        loadingIcon = mView.findViewById(R.id.loadingIcon);

        recyclerView = mView.findViewById(R.id.postrecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = mView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this::loadPosts);
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!confessions.isEmpty()) {
            post = new Post(getActivity(), confessions);
            recyclerView.setAdapter(post);
        } else {

        }
        if (loaded) {
            loadingIcon.setVisibility(View.INVISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Para cargar los posts.
     */
    private void loadPosts() {
        db.collection("confessions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            confessions.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Confession confession = new Confession((String) document.getData().get("email"),
                                        (String) document.getData().get("confession"), (boolean) document.getData().get("anon"),
                                        new java.sql.Timestamp((document.getTimestamp("time").toDate()).getTime()),
                                        document.getId());
                                confessions.add(confession);
                                post = new Post(getActivity(), confessions);
                                recyclerView.setAdapter(post);
                            }
                            if (confessions.isEmpty()) {
                                Log.i("Empty", "No hay confesiones :(");
                            }
                        } else {
                            Log.d("error", "Error getting documents: ", task.getException());
                        }
                        loadingIcon.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                        loaded = true;
                    }
                });
    }
}