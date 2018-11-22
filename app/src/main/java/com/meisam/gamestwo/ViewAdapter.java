package com.meisam.gamestwo;

import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.temporal.Temporal;
import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    List<User> items;

    public ViewAdapter(List items){
        this.items=items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scores_users,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.player_name.setText(items.get(position).getName());
        holder.player_score.setText(String.valueOf(items.get(position).getScore()));
        holder.rank.setText(String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView player_name;
        public TextView player_score;
        public TextView rank;


        public ViewHolder(View itemView) {
            super(itemView);
            player_score=itemView.findViewById(R.id.player_score);
            player_name=itemView.findViewById(R.id.player_name);
            rank=itemView.findViewById(R.id.rank);
        }
    }
}
