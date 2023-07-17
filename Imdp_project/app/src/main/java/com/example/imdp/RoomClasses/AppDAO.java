package com.example.imdp.RoomClasses;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.imdp.Account.Comment;
import com.example.imdp.Account.Person;
import com.example.imdp.Account.Rating;

@Database(entities = {Person.class, Rating.class, Comment.class},version = 1)
@TypeConverters({com.example.imdp.RoomClasses.DataTypeConverter.class})
public abstract class AppDAO extends RoomDatabase {
    public abstract PersonDAO getPersonDAO();
    public abstract CommentDAO getCommentDAO();
    public abstract RatingDAO getRatingDAO();
}
