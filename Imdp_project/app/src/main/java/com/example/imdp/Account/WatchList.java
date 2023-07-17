package com.example.imdp.Account;


import com.example.imdp.Data.DynamicArray;

public class WatchList {
    private final DynamicArray<Integer> movies;
    private String personUsername;

    public WatchList(String person) {
        this.movies = new DynamicArray<>();
        this.personUsername = person;
    }

    public DynamicArray<Integer> getMovies() {
        return movies;
    }

    public void addMovie(int movieId) {
        this.movies.add(movieId);
    }

    public String getPerson() {
        return personUsername;
    }

    public void setPerson(String person) {
        this.personUsername = person;
    }
}
