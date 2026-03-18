package com.mousser.myapplication.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.mousser.myapplication.Database.Repository;
import com.mousser.myapplication.Entites.Excursion;
import com.mousser.myapplication.Entites.Vacation;
import com.mousser.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetailActivity extends AppCompatActivity {
    int excursionId;
    String title;
    String sDate;
    int vacationId;
    Repository repository;
    EditText editTitle;
    TextView startDate;
    DatePickerDialog.OnDateSetListener setStartDate;
    final Calendar myCalender = Calendar.getInstance();
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_detail);
        repository = new Repository(getApplication());
        excursionId = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        sDate = getIntent().getStringExtra("date");
        vacationId = getIntent().getIntExtra("vacationId", -1);

        editTitle = findViewById(R.id.excursion_title_view);
        editTitle.setText(title);
        startDate = findViewById(R.id.excursion_date_view);
        startDate.setText(sDate);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = startDate.getText().toString();

                try {
                    myCalender.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                new DatePickerDialog(
                        ExcursionDetailActivity.this,
                        setStartDate,                        // now it's not null
                        myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH),
                        myCalender.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        setStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalender.set(Calendar.YEAR, year);
                myCalender.set(Calendar.MONTH, month);
                myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Vacation vacation = repository.getVacation(vacationId);

                Date vStartTime;
                Date vEndTime;

                try {
                    vStartTime = sdf.parse(vacation.getStartDate());
                    vEndTime = sdf.parse(vacation.getEndDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                    return;
                }

                // Validation: must be within vacation window
                Date chosen = myCalender.getTime();
                if (chosen.before(vStartTime) || chosen.after(vEndTime)) {
                    Toast.makeText(ExcursionDetailActivity.this,
                            "Excursion date must fall within the vacation dates.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                startDate.setText(sdf.format(chosen));
            }
        };





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }
        if (item.getItemId()==R.id.excursion_save){
            title = editTitle.getText().toString();
            sDate = startDate.getText().toString();
            Excursion excursion;
            if (excursionId==-1){
                if (repository.getAllExcursions().isEmpty()) excursionId = 1;
                else {excursionId = repository.getAllExcursions().get(repository.getAllExcursions().size() -1).getId() +1;}
                excursion = new Excursion(excursionId,title,sDate,vacationId);
                repository.addExcursion(excursion);
                this.finish();
            }
            else {
                excursion = new Excursion(excursionId,title,sDate,vacationId);
                repository.updateExcursion(excursion);
                this.finish();
            }

            return true;
        }
        if (item.getItemId()==R.id.excursion_delete){
            for (Excursion excursion : repository.getAllExcursions())
                if (excursion.getId() == excursionId){
                    repository.deleteExcursion(excursion);
                    Toast.makeText(ExcursionDetailActivity.this,excursion.getTitle() + " was deleted.", Toast.LENGTH_LONG).show();
                    this.finish();
                }
            return true;
        }
        if (item.getItemId()==R.id.excursion_notify_start){
            String chosenStart = startDate.getText().toString();

            Date myDate = null;
            try {
                myDate = sdf.parse(chosenStart);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            try {
                long trigger = myDate.getTime();
                if (trigger < System.currentTimeMillis()) {
                    trigger = System.currentTimeMillis() + 2000; // 2-second delay
                }
                Intent intent = new Intent(ExcursionDetailActivity.this, MyReceiver.class);
                intent.putExtra("key", title + " starts today!");
                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetailActivity.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return true;

        }

        return true;
    }
}

