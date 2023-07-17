package com.example.imdp.Data;

import com.example.imdp.Account.Person;
import com.example.imdp.Movie.MovieInfo;

public class Data {
    private static final DynamicArray<Person> people =new DynamicArray<>();

    private static final DynamicArray<MovieInfo> movies =new DynamicArray<>();

    public static DynamicArray<Person> getPeople(){
        return people;
    }

    public static DynamicArray<MovieInfo> getMovies(){
        return movies;
    }

    public static void addPerson(Person p){
        people.add(p);
    }

    public static void addMovie(MovieInfo m){
        movies.add(m);
    }
}
