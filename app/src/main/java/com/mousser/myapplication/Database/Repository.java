package com.mousser.myapplication.Database;

import android.app.Application;
import android.content.Context;

import com.mousser.myapplication.Dao.ExcursionDao;
import com.mousser.myapplication.Dao.VacationDao;
import com.mousser.myapplication.Entites.Excursion;
import com.mousser.myapplication.Entites.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {

    private static Repository repository;
    private VacationDao vacationDao;
    private ExcursionDao excursionDao;
    private Vacation vacation;
    private List<Vacation> vacations;
    private List<Vacation> vacationsInRange;

    private List<Excursion> excursions;
    private static int NUMBER_OF_THREADS = 4;
    private final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public Repository(Application application){
        VacationDatabase db = VacationDatabase.getDatabase(application);
        vacationDao = db.vacationDao();
        excursionDao = db.excursionDao();
    }


    public void addVacation(Vacation vacation){
        databaseExecutor.execute(()-> vacationDao.addVacation(vacation));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public Vacation getVacation(int vacationId){
        databaseExecutor.execute(()-> vacation = vacationDao.getVacation(vacationId));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return vacation;
    }


    public List<Vacation> getVacations(){
        databaseExecutor.execute(()-> vacations = vacationDao.getVacations());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return vacations;
    }

    public List<Vacation> getVacationsInRange(String start, String end){
        databaseExecutor.execute(()-> vacationsInRange = vacationDao.getVacationsInRange(start,end));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return vacationsInRange;

    }

    public void updateVacation(Vacation vacation){
        databaseExecutor.execute(()-> vacationDao.updateVacation(vacation));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteVacation(Vacation vacation){
        databaseExecutor.execute(()-> vacationDao.deleteVacation(vacation));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    public List<Excursion> getAssociatedExcursions(int vacationId){
        databaseExecutor.execute(()-> excursions = excursionDao.getAssociatedExcursions(vacationId));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return excursions;
    }

    public List<Excursion> getAllExcursions(){
        databaseExecutor.execute(()-> excursions = excursionDao.getAllExcursions());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return excursions;

    }

    public void addExcursion(Excursion excursion){
        databaseExecutor.execute(()-> excursionDao.addExcursion(excursion));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateExcursion(Excursion excursion){
        databaseExecutor.execute(()-> excursionDao.updateExcursion(excursion));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteExcursion(Excursion excursion){
        databaseExecutor.execute(()-> excursionDao.deleteExcursion(excursion));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}
