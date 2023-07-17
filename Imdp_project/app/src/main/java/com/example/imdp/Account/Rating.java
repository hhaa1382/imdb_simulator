package com.example.imdp.Account;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Rating {
    private double number;

    @PrimaryKey
    private int movieId;
    private int voteNumber;

    public Rating(int voteNumber, double number, int movieId) {
        this.number = number;
        this.movieId = movieId;
        this.voteNumber=voteNumber;
    }

    public Rating(){}

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getVoteNumber() {
        return voteNumber;
    }

    public void setVoteNumber(int voteNumber) {
        this.voteNumber = voteNumber;
    }
}