package com.example.imdp.RoomClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.imdp.Account.Comment;

import java.util.List;

@Dao
public interface CommentDAO {
    @Insert
    void insertComment(Comment c);

    @Delete
    void deleteComment(Comment c);

    @Update
    void updateComment(Comment c);

    @Query("select * from comment")
    List<Comment> getComments();

    @Query("select * from comment where movieId= :movie")
    List<Comment> getComments(int movie);
}
