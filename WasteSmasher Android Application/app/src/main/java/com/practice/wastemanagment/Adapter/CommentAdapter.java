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
import com.practice.wastemanagment.Comments;
import com.practice.wastemanagment.R;

import org.w3c.dom.Comment;

import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    Context mContext;
    List<Comments> mData;

    public CommentAdapter(Context mContext, List<Comments> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_comment_item,parent,false);
        return new CommentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int ps=position;
        holder.uName.setText(mData.get(ps).getUserName());
        holder.userComt.setText(mData.get(ps).getContent());
        if(mData.get(ps).getUserImg()!=null)
            Glide.with(mContext).load(mData.get(ps).getUserImg()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView uName,userComt;
        ImageView img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            uName=itemView.findViewById(R.id.commentUName);
            userComt=itemView.findViewById(R.id.userComment);
            img=itemView.findViewById(R.id.commentDP);

        }
    }
}
