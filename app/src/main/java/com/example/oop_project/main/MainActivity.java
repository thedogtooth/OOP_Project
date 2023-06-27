package com.example.oop_project.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oop_project.Post;
import com.example.oop_project.R;
import com.example.oop_project.model.Confession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Fragment {
    private View mView;
    private FirebaseFirestore db;
    private List<Confession> confessions;
    private Post posts;
    private RecyclerView recyclerView;
    public MainActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.main_activity, container, false);
        recyclerView = mView.findViewById(R.id.postrecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        confessions = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        loadPosts();
        return mView;
    }

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
                                        ((Long) document.getData().get("likes")).intValue(), ((Long) document.getData().get("dislikes")).intValue(),
                                        new java.sql.Timestamp((document.getTimestamp("time").toDate()).getTime()),
                                        document.getId());
                                confessions.add(confession);
                                posts = new Post(getActivity(), confessions);
                                recyclerView.setAdapter(posts);
                            }
                        } else {
                            Log.d("error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}