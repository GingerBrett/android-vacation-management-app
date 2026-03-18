package com.mousser.myapplication.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mousser.myapplication.Dao.ExcursionDao;
import com.mousser.myapplication.Dao.UserDao;
import com.mousser.myapplication.Dao.VacationDao;
import com.mousser.myapplication.Entites.Excursion;
import com.mousser.myapplication.Entites.User;
import com.mousser.myapplication.Entites.Vacation;

import java.util.concurrent.Executors;

import com.mousser.myapplication.Security.PasswordUtils;

@Database(entities = {Vacation.class, Excursion.class, User.class}, version=4, exportSchema = false)
public abstract class VacationDatabase extends RoomDatabase {

    public abstract VacationDao vacationDao();
    public abstract ExcursionDao excursionDao();
    public abstract UserDao userDao();

    private static volatile VacationDatabase INSTANCE;

    public static VacationDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (VacationDatabase.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(), VacationDatabase.class, "MyVacationDatabase")
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);

                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        String username = "admin";
                                        String passwordHash =
                                                PasswordUtils.hashPassword("password123");
                                        User user = new User(username,passwordHash);
                                        getDatabase(context).userDao().insert(user);
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
