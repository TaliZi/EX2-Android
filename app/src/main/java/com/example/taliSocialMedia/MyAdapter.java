package com.example.taliSocialMedia;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;


    Context context;
    List<Item> items;

    public MyAdapter(Context context, List<Item> items,RecyclerViewInterface recyclerViewInterface) {
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
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull  MyViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.messageView.setText(items.get(position).getMessage());
        if(items.get(position).getImage()==0)
            holder.imageView.setImageURI(items.get(position).getUriImage());
        else
            holder.imageView.setImageResource(items.get(position).getImage());

        if(items.get(position).getPostPhoto()!=null) {
            holder.postPhoto.setImageURI(items.get(position).getPostPhoto());
        }


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
                boolean liked = recyclerViewInterface.onLikeClick(holder.getAdapterPosition());
                if(liked){
                    holder.like.setImageResource(R.drawable.baseline_thumb_up_off_alt_24);
                }
                else{
                    holder.like.setImageResource(R.drawable.baseline_thumb_up_pressed);
                }
            }
        });


        boolean PostByUser=recyclerViewInterface.checkIfPostIsMadeByCurrentUser(holder.getAdapterPosition());
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
                recyclerViewInterface.onDeleteClick(holder.getAdapterPosition());
            }
        });

        holder.addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.onAddCommentClick(holder.getAdapterPosition());
            }
        });


        holder.editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.onEditPostClick(holder.getAdapterPosition());
            }
        });


        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewInterface.onShareClick(holder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
