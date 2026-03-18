package com.mousser.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mousser.myapplication.Database.Repository;
import com.mousser.myapplication.Entites.Excursion;
import com.mousser.myapplication.Entites.Vacation;
import com.mousser.myapplication.R;

import java.util.List;

public class VacationActivity extends AppCompatActivity {
    Repository repository;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation);
        recyclerView = findViewById(R.id.vacation_recyclerView);
        repository = new Repository(getApplication());
        List<Vacation> vacations = repository.getVacations();
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(vacations);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationActivity.this, VacationDetailActivity.class);
                startActivity(intent);
            }
        });
    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.menu_vacation, menu);
            return true;

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item){
            if(item.getItemId()==R.id.addSample){
                Repository repository = new Repository(getApplication());
                Vacation vacation = new Vacation(1,"Cali","Hilton","06/02/26","06/05/26");
                repository.addVacation(vacation);
                Excursion excursion = new Excursion(1,"Surfing", "06/03/26", 1);
                repository.addExcursion(excursion);
                vacation = new Vacation(2,"AZ","Hilton","06/02/26","06/05/26" );
                repository.addVacation(vacation);
                excursion = new Excursion(2,"Wakeboarding", "06/03/26", 2);
                repository.addExcursion(excursion);
                return true;
            }
            if(item.getItemId()==android.R.id.home){
                this.finish();
                return true;
            }
            return true;
        }

        @Override
        public void onResume(){

            super.onResume();
            List<Vacation> vacations = repository.getVacations();
            final VacationAdapter vacationAdapter = new VacationAdapter(this);
            recyclerView.setAdapter(vacationAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            vacationAdapter.setVacations(vacations);

        }

}