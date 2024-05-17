/**
 * @author Renato Burgos Hidalgo
 */

package com.example.oop_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oop_project.model.Confession;
import com.example.oop_project.model.Dislike;
import com.example.oop_project.model.Like;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * Clase para mostrar cada confesión en el feed.
 */
public class Post extends RecyclerView.Adapter<com.example.oop_project.Post.MyHolder> {
    /**
     * El contexto corresponde en este caso a la actividad que llama al objeto, es decir, MainActivity.
     */
    private Context context;
    /**
     * Lista de confesiones.
     */
    private List<Confession> confessions;
    /**
     * Vista actual.
     */
    private View view;
    private FirebaseFirestore db;

    /**
     * Constructor de la clase. Se establecen las variables context y confessions.
     * @param context MainActivity en este caso.
     * @param confessions la lista con confesiones.
     */
    public Post(Context context, List<Confession> confessions) {
        this.context = context;
        this.confessions = confessions;
        db = HomeActivity.getDb();
    }

    /**
     * @param parent
     * @param viewType
     * @return una instancia de MyHolder.
     */
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_posts, parent, false);
        if (!confessions.isEmpty()) {
            CardView cardView = view.findViewById(R.id.cardView);
            cardView.setVisibility(View.VISIBLE);
        }
        return new MyHolder(view);
    }

    /**
     * @param holder el holder con los campos de texto a cambiar.
     * @param position la posición del item en la vista.
     */
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        ToggleButton likeButton = view.findViewById(R.id.like);
        ToggleButton dislikeButton = view.findViewById(R.id.dislike);
        if (!confessions.get(position).isAnon()) {
            String nameHolder = confessions.get(position).getEmail();
            holder.name.setText(nameHolder);
        } else {
            holder.name.setText(R.string.anonConfession);
        }
        String confessionHolder = confessions.get(position).getConfession();
        Timestamp timeHolder = confessions.get(position).getTime();

        String timeOnScreen = differenceTime(timeHolder);

        holder.time.setText(timeOnScreen);
        holder.confession.setText(confessionHolder);

        db.collection("likes").whereEqualTo("post", confessions.get(position).getDocument())
            .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (Objects.equals(document.getData().get("email"), HomeActivity.getEmail())) {
                            holder.likeDocument = document.getId();
                            if (Objects.equals(document.getData().get("liked"), true)) {
                                likeButton.setChecked(true);
                                break;
                            }
                        }
                    }
                }
            });

        db.collection("dislikes").whereEqualTo("post", confessions.get(position).getDocument())
            .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (Objects.equals(document.getData().get("email"), HomeActivity.getEmail())) {
                            holder.dislikeDocument = document.getId();
                            if (Objects.equals(document.getData().get("disliked"), true)) {
                                dislikeButton.setChecked(true);
                                break;
                            }
                        }
                    }
                    //holder.likeDislike.setText("Likes: " + likesVsDislikes);
                }
            });

        likeButton.setOnClickListener(view -> {
            if (dislikeButton.isChecked()) {
                dislikeButton.setChecked(false);
                db.collection("dislikes").document(holder.dislikeDocument).update("disliked", false);
            } if (likeButton.isChecked()) {
                if (holder.likeDocument != null) {
                    db.collection("likes").document(holder.likeDocument).update("liked", true);
                    return;
                }
                Timestamp time = new Timestamp(System.currentTimeMillis());
                Like like = new Like(HomeActivity.getEmail(), confessions.get(position).getDocument(), time);
                db.collection("likes").add(like)
                        .addOnCompleteListener(task -> holder.likeDocument = task.getResult().getId());
            } else {
                db.collection("likes").document(holder.likeDocument).update("liked", false);
            }
        });
        dislikeButton.setOnClickListener(view -> {
            if (likeButton.isChecked()) {
                likeButton.setChecked(false);
                db.collection("likes").document(holder.likeDocument).update("liked", false);
            } if (dislikeButton.isChecked()) {
                if (holder.dislikeDocument != null) {
                    db.collection("dislikes").document(holder.dislikeDocument).update("disliked", true);
                    return;
                }
                Timestamp time = new Timestamp(System.currentTimeMillis());
                Dislike dislike = new Dislike(HomeActivity.getEmail(), confessions.get(position).getDocument(), time);
                db.collection("dislikes").add(dislike)
                        .addOnCompleteListener(task -> holder.dislikeDocument = task.getResult().getId());
            } else {
                db.collection("dislikes").document(holder.dislikeDocument).update("disliked", false);
            }
        });
    }

    /**
     * Para tener la fecha o diferencia de tiempo entre el tiempo actual y uno anterior.
     * @param then tiempo a comparar con el actual.
     * @return un string que se desplegará en el post.
     */
    private String differenceTime(Timestamp then) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp now = Timestamp.valueOf(dateFormat.format(calendar.getTime()));

        long diff = (now.getTime() - then.getTime())/1000;
        if (diff < 60) {
            return "Hace " + diff + " s.";
        } if (diff/60 < 60) {
            return "Hace " + diff/60 + " m.";
        } if (diff/3600 < 24) {
            return "Hace " + diff/3600 + " h.";
        } if ((int) (diff / 86400) == 1) {
            return context.getString(R.string.yesterday);
        } if ((int) (diff /86400) >= 2 && (int) (diff/86400) < 7) {
            return "Hace " + (int) (diff/86400) + " d.";
        }
        else {
            String[] nowCalendar = then.toString().split(" ");
            int year = Integer.parseInt(nowCalendar[0].split("-")[0]);
            if (year >= calendar.get(Calendar.YEAR)) {
                return nowCalendar[0].split("-")[1] + "-" + nowCalendar[0].split("-")[2];
            } else {
                return nowCalendar[0];
            }
        }
    }

    /**
     * Este método es necesario aunque no se ocupa.
     * @return la cantidad de confesiones.
     */
    @Override
    public int getItemCount() {
        return confessions.size();
    }

    /**
     * Nueva clase que guarda los campos de texto a cambiar de acuerdo a cada post.
     */
    static class MyHolder extends RecyclerView.ViewHolder {
        TextView name, time, confession, likeDislike;
        String likeDocument, dislikeDocument;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.usernameTextView);
            time = itemView.findViewById(R.id.timeTextView);
            confession = itemView.findViewById(R.id.confessionTextView);
            likeDislike = itemView.findViewById(R.id.likes_dislikes);
        }
    }
}
