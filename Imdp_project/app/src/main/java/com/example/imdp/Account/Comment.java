package com.example.imdp.Account;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Comment {
    private String comment;
    private int movieId;
    private String person;

    @NonNull
    @PrimaryKey
    private String primaryKeyCheck;

    public Comment(String comment, String person,int movieId) {
        this.comment = comment;
        this.person = person;
        this.movieId=movieId;
        primaryKeyCheck=person+"-"+movieId+"-"+comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getPrimaryKeyCheck() {
        return primaryKeyCheck;
    }

    public void setPrimaryKeyCheck(String primaryKeyCheck) {
        this.primaryKeyCheck = primaryKeyCheck;
    }
}
