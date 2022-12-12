package com.practice.wastemanagment.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.practice.wastemanagment.OrganicWaste;
import com.practice.wastemanagment.R;
import com.practice.wastemanagment.organicHistory;

import java.util.ArrayList;

public class organicRequestAdapter extends RecyclerView.Adapter<organicRequestAdapter.MyViewHolder> {
    Context context;
    ArrayList<OrganicWaste> list;

    public organicRequestAdapter(Context context, ArrayList<OrganicWaste> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public organicRequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_organic_history,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull organicRequestAdapter.MyViewHolder holder, int position) {
        int ps=list.size()-position-1;
        Glide.with(context).load(list.get(ps).getPostImg()).into(holder.img);

        String s1="Request Send";
        if(list.get(ps).getStatus().equals(s1)) {
            holder.status.setTextColor(Color.parseColor("#800000"));
            holder.status.setText(list.get(ps).getStatus());
        }
        else{
            holder.status.setTextColor(Color.parseColor("#008000"));
            holder.status.setText(list.get(ps).getStatus());
        }

        holder.topic.setText(list.get(ps).getType());
        holder.address.setText(list.get(ps).getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView status,topic,address;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.display_img_hist);
            status=itemView.findViewById(R.id.display_status);
            topic=itemView.findViewById(R.id.display_topic);
            address=itemView.findViewById(R.id.display_address);
        }
    }
}
