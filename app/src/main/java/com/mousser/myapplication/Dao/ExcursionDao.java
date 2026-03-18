package com.mousser.myapplication.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.mousser.myapplication.Entites.Excursion;

import java.util.List;

@Dao
public interface ExcursionDao {

    @Query("SELECT * FROM excursions WHERE id = :id")
    Excursion getExcursion(long id);

    @Query("SELECT * FROM excursions WHERE vacation_id = :vacationId ORDER BY id ASC")
    List<Excursion> getAssociatedExcursions(long vacationId);

    @Query("SELECT * FROM excursions ORDER BY id ASC")
    List<Excursion> getAllExcursions();



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addExcursion(Excursion excursion);

    @Update
    void updateExcursion(Excursion excursion);

    @Delete
    void deleteExcursion(Excursion excursion);
}
