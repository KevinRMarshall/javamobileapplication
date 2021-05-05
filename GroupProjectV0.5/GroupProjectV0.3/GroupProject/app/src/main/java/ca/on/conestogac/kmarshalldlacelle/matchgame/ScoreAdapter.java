package ca.on.conestogac.kmarshalldlacelle.matchgame;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private ArrayList<MatchObject> scores;

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewScore;
        private TextView textViewDate;

        public ScoreViewHolder(View v) {
            super(v);
            textViewName = v.findViewById(R.id.lblName);
            textViewDate = v.findViewById(R.id.lblDate);
            textViewScore = v.findViewById(R.id.inputScore);
        }
    }

    public ScoreAdapter(ArrayList<MatchObject> matches) {
        scores = matches;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.score_card_view, parent, false);
        ScoreViewHolder vh = new ScoreViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        holder.textViewDate.setText(scores.get(position).getDate());
        holder.textViewName.setText(scores.get(position).getName());
        holder.textViewScore.setText(Integer.toString(scores.get(position).getScore()));
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }



}
