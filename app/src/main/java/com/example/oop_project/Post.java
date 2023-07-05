/**
 * @author Renato Burgos Hidalgo
 */

package com.example.oop_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oop_project.model.Confession;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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

    /**
     * Constructor de la clase. Se establecen las variables context y confessions.
     * @param context MainActivity en este caso.
     * @param confessions la lista con confesiones.
     */
    public Post(Context context, List<Confession> confessions) {
        this.context = context;
        this.confessions = confessions;
    }

    /**
     *
     * @param parent
     * @param viewType
     *
     * @return una instancia de MyHolder.
     */
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.row_posts, parent, false);
        if (confessions.size() != 0) {
            CardView cardView = view.findViewById(R.id.cardView);
            cardView.setVisibility(View.VISIBLE);
        }
        return new MyHolder(view);
    }

    /**
     *
     * @param holder el holder con los campos de texto a cambiar.
     * @param position la posición del item en la vista.
     */
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        ToggleButton likeButton = (ToggleButton) view.findViewById(R.id.like);
        ToggleButton dislikeButton = (ToggleButton) view.findViewById(R.id.dislike);
        if (!confessions.get(position).isAnon()) {
            String nameHolder = confessions.get(position).getEmail();
            holder.name.setText(nameHolder);
        } else {
            holder.name.setText("Confesión anónima");
        }
        String confessionHolder = confessions.get(position).getConfession();
        Timestamp timeHolder = confessions.get(position).getTime();

        String timeOnScreen = differenceTime(timeHolder);

        holder.time.setText(timeOnScreen);
        holder.confession.setText(confessionHolder);

        int likesVsDislikes = confessions.get(position).getLikes() - confessions.get(position).getDislikes();
        holder.likeDislike.setText("Likes: " + likesVsDislikes);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dislikeButton.isChecked()) {
                    dislikeButton.setChecked(false);
                    confessions.get(position).setDislikes(confessions.get(position).getDislikes() - 1);
                } if (likeButton.isChecked()) {
                    confessions.get(position).setLikes(confessions.get(position).getLikes() + 1);
                } else {
                    confessions.get(position).setLikes(confessions.get(position).getLikes() - 1);
                }
                int likesVsDislikes = confessions.get(position).getLikes() - confessions.get(position).getDislikes();
                holder.likeDislike.setText("Likes: " + likesVsDislikes);
            }
        });
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (likeButton.isChecked()) {
                    likeButton.setChecked(false);
                    confessions.get(position).setLikes(confessions.get(position).getLikes() - 1);
                } if (dislikeButton.isChecked()) {
                    confessions.get(position).setDislikes(confessions.get(position).getDislikes() + 1);
                } else {
                    confessions.get(position).setDislikes(confessions.get(position).getDislikes() - 1);
                }
                int likesVsDislikes = confessions.get(position).getLikes() - confessions.get(position).getDislikes();
                holder.likeDislike.setText("Likes: " + likesVsDislikes);
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
            return "Ayer";
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
    class MyHolder extends RecyclerView.ViewHolder {
        TextView name, time, confession, likeDislike;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.usernameTextView);
            time = (TextView) itemView.findViewById(R.id.timeTextView);
            confession = (TextView) itemView.findViewById(R.id.confessionTextView);
            likeDislike = (TextView) itemView.findViewById(R.id.likes_dislikes);
        }
    }
}
