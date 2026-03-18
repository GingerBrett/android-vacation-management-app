package com.mousser.myapplication.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mousser.myapplication.Entites.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user WHERE username = :username LIMIT 1")
    User getUser(String username);

    @Insert
    void insert(User user);
}

