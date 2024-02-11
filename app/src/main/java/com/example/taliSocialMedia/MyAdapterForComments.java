package com.example.taliSocialMedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapterForComments extends RecyclerView.Adapter<MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;


    Context context;
    List<Item> items;

    public MyAdapterForComments(Context context, List<Item> items, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.items = items;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setFilteredList(List<Item> filteredList){
        this.items = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view_comment,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.messageView.setText(items.get(position).getMessage());
        if(items.get(position).getImage()==0)
            holder.imageView.setImageURI(items.get(position).getUriImage());
        else
            holder.imageView.setImageResource(items.get(position).getImage());

        holder.relativeLayout.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.anim));



        holder.dateView.setText(items.get(position).getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recyclerViewInterface != null)
                {
                    recyclerViewInterface.onItemClick(holder.getAdapterPosition());
                }
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean liked = recyclerViewInterface.onLikeClick(holder.getAdapterPosition()+1);
                if(liked){
                    holder.like.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
                }
                else{
                    holder.like.setImageResource(R.drawable.baseline_thumb_up_pressed);
                }
            }
        });

        boolean PostByUser=recyclerViewInterface.checkIfPostIsMadeByCurrentUser(holder.getAdapterPosition()+1);
        if (PostByUser){
            holder.delete.setVisibility(View.VISIBLE);
            holder.editPost.setVisibility(View.VISIBLE);
        }
        else{
            holder.delete.setVisibility(View.GONE);
            holder.editPost.setVisibility(View.GONE);
        }


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.onDeleteClick(holder.getAdapterPosition()+1);
            }
        });



        holder.editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.onEditPostClick(holder.getAdapterPosition()+1);
            }
        });


        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.onShareClick(holder.getAdapterPosition()+1);
            }
        });






    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

