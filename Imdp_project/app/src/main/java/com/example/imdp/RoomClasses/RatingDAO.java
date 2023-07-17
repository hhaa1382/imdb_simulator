package com.example.imdp.RoomClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.imdp.Account.Rating;

import java.util.List;

@Dao
public interface RatingDAO {
    @Insert
    void insertRating(Rating r);

    @Delete
    void deleteRating(Rating r);

    @Update
    void updateRating(Rating r);

    @Query("select * from rating")
    List<Rating> getRatings();
}
