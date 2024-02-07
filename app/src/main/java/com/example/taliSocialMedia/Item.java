package com.example.taliSocialMedia;

import android.net.Uri;

public class Item {

    String name;
    String email;

    String message;

    String date;
    int image=0,like;
    Uri uriImage=null,postPhoto=null;
    boolean isitliked,isPostByCurrentUser,isHeadlineInComments;


/*

Here we use different constructors for different items.
Example of different items:

Because some items use stock photos from the app
Some items use new photos from the camera/gallery of the user
Some items have a picture in them (post items have pictures, comments do not)
etc etc...



 */
    public Item(String name, String message, int image, String date, int like, boolean isitliked, String email, boolean isPostByCurrentUser, boolean isHeadlineInComments) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.message = message;
        this.date = date;
        this.like=like;
        this.isitliked=isitliked;
        this.isPostByCurrentUser=isPostByCurrentUser;
        this.isHeadlineInComments=isHeadlineInComments;
    }

    public Item(String name, String message, int image, String date, int like, boolean isitliked, String email, boolean isPostByCurrentUser, boolean isHeadlineInComments,Uri postPhoto) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.message = message;
        this.date = date;
        this.like=like;
        this.isitliked=isitliked;
        this.isPostByCurrentUser=isPostByCurrentUser;
        this.isHeadlineInComments=isHeadlineInComments;
        this.postPhoto=postPhoto;
    }



    public Item(String name, String message, Uri image, String date, int like, boolean isitliked, String email, boolean isPostByCurrentUser, boolean isHeadlineInComments, Uri postPhoto) {
        this.name = name;
        this.email = email;
        this.uriImage = image;
        this.message = message;
        this.date = date;
        this.like=like;
        this.isitliked=isitliked;
        this.isPostByCurrentUser=isPostByCurrentUser;
        this.isHeadlineInComments=isHeadlineInComments;
        this.postPhoto=postPhoto;
    }



    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", image=" + image +
                ", like=" + like +
                ", uriImage=" + uriImage +
                ", isitliked=" + isitliked +
                ", isPostByCurrentUser=" + isPostByCurrentUser +
                '}';
    }

    public Item(String name, String message, Uri image, String date, int like, boolean isitliked, String email, boolean isPostByCurrentUser,boolean isHeadlineInComments) {
        this.name = name;
        this.email = email;
        this.uriImage = image;
        this.message = message;
        this.date = date;
        this.like=like;
        this.isitliked=isitliked;
        this.isPostByCurrentUser=isPostByCurrentUser;
        this.isHeadlineInComments=isHeadlineInComments;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public boolean isIsitliked() {
        return isitliked;
    }

    public void setIsitliked(boolean isitliked) {
        this.isitliked = isitliked;
    }

    public Uri getUriImage() {
        return uriImage;
    }

    public void setUriImage(Uri uriImage) {
        this.uriImage = uriImage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isPostByCurrentUser() {
        return isPostByCurrentUser;
    }

    public void setPostByCurrentUser(boolean postByCurrentUser) {
        isPostByCurrentUser = postByCurrentUser;
    }

    public boolean isHeadlineInComments() {
        return isHeadlineInComments;
    }

    public void setHeadlineInComments(boolean headlineInComments) {
        isHeadlineInComments = headlineInComments;
    }

    public Uri getPostPhoto() {
        return postPhoto;
    }

    public void setPostPhoto(Uri postPhoto) {
        this.postPhoto = postPhoto;
    }

}
