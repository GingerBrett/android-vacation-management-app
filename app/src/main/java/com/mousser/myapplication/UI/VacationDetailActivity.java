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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mousser.myapplication.Database.Repository;
import com.mousser.myapplication.Entites.Excursion;
import com.mousser.myapplication.Entites.Vacation;
import com.mousser.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetailActivity extends AppCompatActivity {

    String title;
    String hotel;
    String sDate;
    String eDate;
    int vacationId;
    Repository repository;
    EditText editTitle;
    EditText editHotel;
    TextView startDate;
    TextView endDate;
    RecyclerView recyclerView;
    DatePickerDialog.OnDateSetListener setStartDate;
    DatePickerDialog.OnDateSetListener setEndDate;
    final Calendar myCalender = Calendar.getInstance();
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    String today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_detail);

        editTitle = findViewById(R.id.vacation_title_view);
        editHotel = findViewById(R.id.hotel_name_view);
        startDate = findViewById(R.id.start_date_view);
        endDate = findViewById(R.id.end_date_view);

        title = getIntent().getStringExtra("title");
        hotel = getIntent().getStringExtra("hotel");
        sDate = getIntent().getStringExtra("startDate");
        eDate = getIntent().getStringExtra("endDate");
        vacationId = getIntent().getIntExtra("id", -1);

        editTitle.setText(title);
        editHotel.setText(hotel);
        startDate.setText(sDate);
        endDate.setText(eDate);

        today = sdf.format(new Date());

        if (startDate.getText().toString().isEmpty()) {
            startDate.setText(today);
        }
        if (endDate.getText().toString().isEmpty()) {
            endDate.setText(today);
        }


        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = startDate.getText().toString();
                try {
                    if (!info.isEmpty()) {
                        myCalender.setTime(sdf.parse(info));
                    } else {
                        myCalender.setTimeInMillis(System.currentTimeMillis());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    myCalender.setTimeInMillis(System.currentTimeMillis());
                }
                new DatePickerDialog(VacationDetailActivity.this, setStartDate, myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        setStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalender.set(Calendar.YEAR, year);
                myCalender.set(Calendar.MONTH, month);
                myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Date endTime;
                try {
                    endTime = sdf.parse(endDate.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (myCalender.getTime().after(endTime)) {
                    Toast.makeText(VacationDetailActivity.this, "Start Date cannot be after End Date.", Toast.LENGTH_LONG).show();
                    return;
                }


                startDate.setText(sdf.format(myCalender.getTime()));
            }
        };

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = endDate.getText().toString();
                try {
                    if (!info.isEmpty()) {
                        myCalender.setTime(sdf.parse(info));
                    } else {
                        myCalender.setTimeInMillis(System.currentTimeMillis());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    myCalender.setTimeInMillis(System.currentTimeMillis());
                }
                new DatePickerDialog(VacationDetailActivity.this, setEndDate, myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        setEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar chosenEnd = Calendar.getInstance();
                chosenEnd.set(Calendar.YEAR, year);
                chosenEnd.set(Calendar.MONTH, month);
                chosenEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Date startTime;
                try {
                    startTime = sdf.parse(startDate.getText().toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (chosenEnd.getTime().before(startTime)) {
                    Toast.makeText(VacationDetailActivity.this, "End Date cannot be before Start Date.", Toast.LENGTH_LONG).show();
                    return;
                }

                endDate.setText(sdf.format(chosenEnd.getTime()));
            }
        };


        recyclerView = findViewById(R.id.excursion_recyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> excursions = repository.getAssociatedExcursions(vacationId);
        excursionAdapter.setExcursions(excursions);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VacationDetailActivity.this, ExcursionDetailActivity.class);
                intent.putExtra("vacationId", vacationId);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.vacation_save) {
            Vacation vacation;
            if (vacationId == -1) {
                if (repository.getVacations().isEmpty()) vacationId = 1;
                else {
                    vacationId = repository.getVacations().get(repository.getVacations().size() - 1).getId() + 1;
                }
                vacation = new Vacation(vacationId, editTitle.getText().toString(), editHotel.getText().toString(), startDate.getText().toString(), endDate.getText().toString());
                repository.addVacation(vacation);
                this.finish();
            } else {
                vacation = new Vacation(vacationId, editTitle.getText().toString(), editHotel.getText().toString(), startDate.getText().toString(), endDate.getText().toString());
                repository.updateVacation(vacation);
                this.finish();
            }

            return true;
        }
        if (item.getItemId() == R.id.vacation_delete) {
            List<Excursion> excursions = repository.getAssociatedExcursions(vacationId);
            Vacation vacation = repository.getVacation(vacationId);

            if (!excursions.isEmpty()) {
                Toast.makeText(VacationDetailActivity.this, "Cannot delete a vacation with excursions.", Toast.LENGTH_LONG).show();
            } else {
                repository.deleteVacation(vacation);
                this.finish();
            }

            return true;
        }
        if (item.getItemId() == R.id.vacation_notify_start) {
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
                Intent intent = new Intent(VacationDetailActivity.this, MyReceiver.class);
                intent.putExtra("key", title + " starts today!");
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetailActivity.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return true;

        }
        if (item.getItemId() == R.id.vacation_notify_end) {
            String chosenEnd = endDate.getText().toString();

            Date myDate = null;
            try {
                myDate = sdf.parse(chosenEnd);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            try {
                long trigger = myDate.getTime();
                if (trigger < System.currentTimeMillis()) {
                    trigger = System.currentTimeMillis() + 2000; // 2-second delay
                }
                Intent intent = new Intent(VacationDetailActivity.this, MyReceiver.class);
                intent.putExtra("key", title + " ends today!");
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetailActivity.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return true;

        }
        if (item.getItemId() == R.id.vacation_Share) {
            StringBuffer builder = new StringBuffer("Vacation Title: " + title +
                    "\nHotel: " + hotel +
                    "\nStart Date: " + sDate +
                    "\nEnd Date: " + eDate);

            List<Excursion> excursions = repository.getAssociatedExcursions(vacationId);
            if (!excursions.isEmpty()) {
                builder.append("\nExcursions: ");
                for (Excursion excursion : excursions) {
                    builder.append("\n").append(excursion.getTitle()).append(" is on ").append(excursion.getDate());
                }
            }
            String extraString = builder.toString();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TITLE, "Vacation: " + title);
            intent.putExtra(Intent.EXTRA_TEXT, extraString);
            intent.setType("text/plain");
            Intent chooserIntent = Intent.createChooser(intent, "Share Vacation");
            startActivity(chooserIntent);


        }


        return true;
    }

    @Override
    public void onResume() {

        super.onResume();
        recyclerView = findViewById(R.id.excursion_recyclerView);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> excursions = repository.getAssociatedExcursions(vacationId);
        excursionAdapter.setExcursions(excursions);

    }

}