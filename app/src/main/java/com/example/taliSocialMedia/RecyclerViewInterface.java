package com.example.taliSocialMedia;

public interface RecyclerViewInterface {
    void onItemClick(int position);

    boolean onLikeClick(int position);

    boolean checkIfPostIsMadeByCurrentUser(int position);

    void onDeleteClick(int position);

    void onAddCommentClick(int position);

    void onEditPostClick(int position);

    void onShareClick(int position);


}
