package com.app.readfi.Models;

public class Post {
    private String postID;
    private String postText, postDescription, img;

    public Post() {
    }

    public Post(String postID, String postText, String postDescription, String img) {
        this.postID = postID;
        this.postText = postText;
        this.postDescription = postDescription;
        this.img = img;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
