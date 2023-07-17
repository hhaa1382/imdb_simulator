package com.example.imdp.RoomClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.imdp.Account.Person;

import java.util.List;

@Dao
public interface PersonDAO {
    @Insert
    void insertPerson(Person p);

    @Delete
    void deletePerson(Person p);

    @Update
    void updatePerson(Person p);

    @Query("select * from account")
    List<Person> getPeople();
}
