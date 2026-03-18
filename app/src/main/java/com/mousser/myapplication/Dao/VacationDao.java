package com.mousser.myapplication.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mousser.myapplication.Entites.Vacation;

import java.util.List;

@Dao
public interface VacationDao {

    @Query("SELECT * FROM vacations WHERE id = :id")
    Vacation getVacation(long id);

    @Query("SELECT * FROM vacations ORDER BY id ASC")
    List<Vacation> getVacations();

    @Query("SELECT * FROM vacations WHERE start_date BETWEEN :start AND :end")
    List<Vacation> getVacationsInRange(String start, String end);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addVacation(Vacation vacation);
    @Update
    void updateVacation(Vacation vacation);

    @Delete
    void deleteVacation(Vacation vacation);

}
