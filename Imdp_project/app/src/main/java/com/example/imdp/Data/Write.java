package com.example.imdp.Data;

import android.content.Context;

import androidx.room.Room;

import com.example.imdp.Account.Comment;
import com.example.imdp.Account.Person;
import com.example.imdp.Account.Rating;
import com.example.imdp.Movie.MovieInfo;
import com.example.imdp.RoomClasses.AppDAO;
import com.example.imdp.RoomClasses.CommentDAO;
import com.example.imdp.RoomClasses.PersonDAO;
import com.example.imdp.RoomClasses.RatingDAO;

import java.util.List;

public class Write {
    private final Context context;
    public static int person=1;
    public static int comment=2;
    public static int rating=3;

    public Write(Context context) {
        this.context = context;
    }

    public void insertValues(int code,Object object){
        AppDAO appDAO;

        if(code==person){
            appDAO=Room.databaseBuilder(context,AppDAO.class,"person_db").allowMainThreadQueries().build();
            PersonDAO personDAO=appDAO.getPersonDAO();
            personDAO.insertPerson((Person) object);
        }
        else if(code==comment){
            appDAO=Room.databaseBuilder(context,AppDAO.class,"comment_db").allowMainThreadQueries().build();
            CommentDAO commentDAO=appDAO.getCommentDAO();
            commentDAO.insertComment((Comment) object);
        }
        else if(code==rating){
            appDAO=Room.databaseBuilder(context,AppDAO.class,"rating_db").allowMainThreadQueries().build();
            RatingDAO ratingDAO=appDAO.getRatingDAO();

            Rating rating=(Rating) object;
            boolean exist=false;
            List<Rating> ratings=ratingDAO.getRatings();

            for(Rating r:ratings){
                if(r.getMovieId()==rating.getMovieId()){
                    exist=true;
                    r.setNumber(rating.getNumber());
                    r.setVoteNumber(rating.getVoteNumber());
                    ratingDAO.updateRating(r);
                    break;
                }
            }

            if(!exist) {
                ratingDAO.insertRating(rating);
            }
        }
    }

    public void deleteValues(int code,Object object){
        AppDAO appDAO;

        if(code==person){
            appDAO=Room.databaseBuilder(context,AppDAO.class,"person_db").allowMainThreadQueries().build();
            PersonDAO personDAO=appDAO.getPersonDAO();
            personDAO.deletePerson((Person) object);
        }
        else if(code==comment){
            appDAO=Room.databaseBuilder(context,AppDAO.class,"comment_db").allowMainThreadQueries().build();
            CommentDAO commentDAO=appDAO.getCommentDAO();
            commentDAO.deleteComment((Comment) object);
        }
        else if(code==rating){
            appDAO=Room.databaseBuilder(context,AppDAO.class,"rating_db").allowMainThreadQueries().build();
            RatingDAO ratingDAO=appDAO.getRatingDAO();
            ratingDAO.deleteRating((Rating) object);
        }
    }

    public void updatePerson(Person p){
        AppDAO appDAO=Room.databaseBuilder(context,AppDAO.class,"person_db").allowMainThreadQueries().build();
        appDAO.getPersonDAO().updatePerson(p);
    }

    public void readPeople(){
        AppDAO appDAO=Room.databaseBuilder(context,AppDAO.class,"person_db").allowMainThreadQueries().build();
        List<Person> people=appDAO.getPersonDAO().getPeople();

        for(Person p:people){
            Data.addPerson(p);
        }
    }

    public DynamicArray<Comment> readComments(MovieInfo movie){
        AppDAO appDAO=Room.databaseBuilder(context,AppDAO.class,"comment_db").allowMainThreadQueries().build();
        List<Comment> comments=appDAO.getCommentDAO().getComments(movie.getId());

        DynamicArray<Comment> commentArray=new DynamicArray<>();

        for(int i=0;i<comments.size();i++){
            commentArray.add(comments.get(i));
        }

        return commentArray;
    }

    public void readRating(MovieInfo movie){
        AppDAO appDAO=Room.databaseBuilder(context,AppDAO.class,"rating_db").allowMainThreadQueries().build();
        List<Rating> ratings=appDAO.getRatingDAO().getRatings();

        for(Rating rating:ratings){
            if(rating.getMovieId()==movie.getId()){
                movie.setRating(rating);
                break;
            }
        }
    }
}
