package com.practice.wastemanagment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.practice.wastemanagment.ModelClassNews;
import com.practice.wastemanagment.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    @NonNull
    Context context;
    ArrayList<ModelClassNews> arrayList;

    public NewsAdapter(@NonNull Context context, ArrayList<ModelClassNews> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.news_item,null,false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.heading.setText(arrayList.get(position).getTitle());
        holder.description.setText(arrayList.get(position).getDescription());

        if(arrayList.get(position).getImage_url()!=null)
            Glide.with(context).load(arrayList.get(position).getImage_url()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView heading,description,author;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            heading=itemView.findViewById(R.id.heading);
            description=itemView.findViewById(R.id.news_description);
            author=itemView.findViewById(R.id.news_author);
            img=itemView.findViewById(R.id.news_image);
        }
    }
}
