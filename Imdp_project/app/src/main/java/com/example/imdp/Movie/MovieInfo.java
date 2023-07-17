package com.example.imdp.Movie;

import android.widget.ImageView;

import com.example.imdp.Account.Rating;
import com.example.imdp.Data.DynamicArray;

public class MovieInfo implements Comparable<MovieInfo>{
    private int id;
    private String title;
    private String originalTitle;
    private String region;
    private String language;
    private boolean isAdult;
    private String year;
    private int time;
    private String overView;
    private String genre;
    private DynamicArray<CastsCrews> directors=new DynamicArray<>();
    private DynamicArray<CastsCrews> writers=new DynamicArray<>();
    private DynamicArray<CastsCrews> actors=new DynamicArray<>();
    private Rating rating=new Rating();
    private ImageView image;
    private String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public DynamicArray<CastsCrews> getDirectors() {
        return directors;
    }

    public String getDirectorsToString(){
        StringBuilder temp=new StringBuilder();
        int size;
        if(directors.size()>3){
            size=3;
        }
        else{
            size= directors.size();
        }
        for(int i=0;i<size;i++){
            temp.append(directors.get(i).getName());
            if(i!=size-1){
                temp.append(" , ");
            }
        }
        return temp.toString();
    }

    public String getDirectorsValues(){
        StringBuilder temp=new StringBuilder();
        for(int i=0;i<directors.size();i++){
            temp.append(directors.get(i).getName());
            if(i!=directors.size()-1){
                temp.append(" , ");
            }
        }
        return temp.toString();
    }

    public void addDirectors(CastsCrews director) {
        this.directors.add(director);
    }

    public DynamicArray<CastsCrews> getWriters() {
        return writers;
    }

    public String getWritersValues(){
        StringBuilder temp=new StringBuilder();
        for(int i=0;i<writers.size();i++){
            temp.append(writers.get(i).getName());
            if(i!=writers.size()-1){
                temp.append(" , ");
            }
        }
        return temp.toString();
    }

    public void addWriters(CastsCrews writer) {
        this.writers.add(writer);
    }

    public DynamicArray<CastsCrews> getActors() {
        return actors;
    }

    public String getActorsToString(){
        StringBuilder temp=new StringBuilder();
        int size;
        if(actors.size()>3){
            size=3;
        }
        else{
            size= actors.size();
        }
        for(int i=0;i<size;i++){
            temp.append(actors.get(i).getName());
            if(i!=size-1){
                temp.append(" , ");
            }
        }
        return temp.toString();
    }

    public String getActorsValues(){
        StringBuilder temp=new StringBuilder();
        for(int i=0;i<actors.size();i++){
            temp.append(actors.get(i).getName());
            if(i!=actors.size()-1){
                temp.append(" , ");
            }
        }
        return temp.toString();
    }

    public void addActors(CastsCrews actor) {
        this.actors.add(actor);
    }

    public double getRatingNumber() {
        return rating.getNumber();
    }

    public void setRatingNumber(double rating) {
        this.rating.setMovieId(this.getId());
        this.rating.setNumber(rating);
    }

    public int getVoteNumber() {
        return rating.getVoteNumber();
    }

    public void setVoteNumber(int voteNumber) {
        this.rating.setVoteNumber(voteNumber);
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean checkFilter(int year,String genre,String starName,String country){
        boolean check=true;

        if(year !=0 && year!=getYearValue()){
            check=false;
        }

        if(genre!=null && !this.genre.contains(genre)){
            check=false;
        }

        if(starName!=null){
            boolean temp=false;
            for(int i=0;i<actors.size();i++){
                if(actors.get(i).getName().contains(starName)){
                    temp=true;
                    break;
                }
            }
            if(!temp) {
                check = false;
            }
        }

        if(country!=null && !this.region.contains(country)){
            check=false;
        }

        return check;
    }

    private int getYearValue(){
        String[] data =year.split("-");
        return Integer.parseInt(data[0]);
    }

    @Override
    public int compareTo(MovieInfo movieInfo) {
        if(this.rating.getNumber()>movieInfo.rating.getNumber()){
            return 1;
        }
        else if(this.rating.getNumber()<movieInfo.rating.getNumber()){
            return -1;
        }
        else{
            if(this.getYearValue()> movieInfo.getYearValue()){
                return 1;
            }
            else if(this.getYearValue()<movieInfo.getYearValue()){
                return -1;
            }
            else return 0;
        }
    }

    public void addRating(int rate){
        double sum=(double) rating.getVoteNumber()*rating.getNumber();
        sum+= rate;
        rating.setVoteNumber(rating.getVoteNumber()+1);
        rating.setNumber(sum/rating.getVoteNumber());
    }

    public Rating getRating(){
        return rating;
    }

    public void setRating(Rating rating){
        this.rating=rating;
    }
}
