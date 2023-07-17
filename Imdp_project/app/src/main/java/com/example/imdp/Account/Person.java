package com.example.imdp.Account;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.imdp.Data.DynamicArray;
import com.example.imdp.Movie.MovieInfo;

@Entity(tableName = "account")
public class Person {
    private String name;
    private String phone;
    private String email;

   @PrimaryKey
   @NonNull
    private String username;
    private String password;
    private boolean isRated;

    private WatchList watchList;
    private DynamicArray<FavoriteList> favoriteLists;

    public Person(String name, String phone, String email, String username, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
        isRated=false;

        watchList=new WatchList(this.getUsername());
        favoriteLists=new DynamicArray<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean isRated){
        this.isRated=isRated;
    }

    public void setRated() {
        isRated = true;
    }

    public void addToWatchList(MovieInfo movie){
        watchList.addMovie(movie.getId());
    }

    public void addFavorite(FavoriteList list){
        favoriteLists.add(list);
    }

    public void removeFavoriteList(FavoriteList list){
        favoriteLists.remove(list);
    }

    public WatchList getWatchList(){
        return watchList;
    }

    public DynamicArray<FavoriteList> getFavoriteLists(){
        return favoriteLists;
    }

    public void setWatchList(WatchList watchList){
        this.watchList=watchList;
    }

    public void setFavoriteLists(DynamicArray<FavoriteList> favoriteLists){
        this.favoriteLists=favoriteLists;
    }
}
