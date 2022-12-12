package com.practice.wastemanagment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.practice.wastemanagment.Post;
import com.practice.wastemanagment.PostDetailedActivity;
import com.practice.wastemanagment.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mData;

    public PostAdapter(FragmentActivity activity, List<Post> list) {
        mContext=activity;
        mData=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int ps=mData.size()-position-1;
        holder.homeUserName.setText(mData.get(ps).getUserName());
        holder.homeUserCity.setText(mData.get(ps).getCity());
        holder.homePostTittle.setText(mData.get(ps).getTittle());
        String disc=mData.get(ps).getDescription().toString();
        disc=disc.substring(0, Math.min(disc.length(), 200))+"...";
        holder.homePostDis.setText(disc);

        Glide.with(mContext).load(mData.get(ps).getPostPicture()).into(holder.homePostImage);
        if(mData.get(ps).getUserDP()!=null)
                Glide.with(mContext).load(mData.get(ps).getUserDP()).into(holder.homeUserDP);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView homeUserName,homeUserCity,homePostTittle,homePostDis;
        ImageView homePostImage,homeUserDP;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            homeUserName=itemView.findViewById(R.id.home_user_name);
            homeUserCity=itemView.findViewById(R.id.home_post_city);
            homePostTittle=itemView.findViewById(R.id.home_post_tittle);
            homePostDis=itemView.findViewById(R.id.home_post_dis);
            homePostImage=itemView.findViewById(R.id.home_post_image);
            homeUserDP=itemView.findViewById(R.id.home_user_profile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent postDetailed=new Intent(mContext, PostDetailedActivity.class);
                    int position=mData.size()-getAdapterPosition()-1;
                    postDetailed.putExtra("tittle",mData.get(position).getTittle());
                    postDetailed.putExtra("postUser",mData.get(position).getUserName());
                    postDetailed.putExtra("desc",mData.get(position).getDescription());
                    postDetailed.putExtra("city",mData.get(position).getCity());
                    postDetailed.putExtra("postImage",mData.get(position).getPostPicture());
                    postDetailed.putExtra("postUserPic",mData.get(position).getUserDP());
                    postDetailed.putExtra("postKey",mData.get(position).getPostId());
                    long timeStamp=(long)mData.get(position).getTimestamp();
                    postDetailed.putExtra("timeStamp",timeStamp);

                    mContext.startActivity(postDetailed);
                }
            });

        }
    }
}
