package com.example.imdp.Account;

import com.example.imdp.Data.DynamicArray;

public class FavoriteList {
    private final DynamicArray<Integer> movies;
    private String personUsername;
    private String name;

    public FavoriteList(String person,String name) {
        this.movies = new DynamicArray<>();
        this.personUsername = person;
        this.name=name;
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
        this.personUsername=person;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
