package com.example.oop_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oop_project.model.Confession;
import com.google.type.DateTime;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Posts extends RecyclerView.Adapter<com.example.oop_project.Posts.MyHolder>{
    Context context;
    List<Confession> confessions;

    public Posts(Context context, List<Confession> confessions) {
        this.context = context;
        this.confessions = confessions;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_posts, parent, false);
        if (confessions.size() != 0) {
            CardView cardView = view.findViewById(R.id.cardView);
            cardView.setVisibility(View.VISIBLE);
        }
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
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
    }

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

    @Override
    public int getItemCount() {
        return confessions.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView name, time, confession, like;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.usernameTextView);
            time = (TextView) itemView.findViewById(R.id.timeTextView);
            confession = (TextView) itemView.findViewById(R.id.confessionTextView);
        }
    }
}
