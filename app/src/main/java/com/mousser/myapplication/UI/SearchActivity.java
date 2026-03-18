package com.mousser.myapplication.UI;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mousser.myapplication.Database.Repository;
import com.mousser.myapplication.Entites.Vacation;
import com.mousser.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    TextView startDate;
    TextView endDate;
    TextView reportHeader;
    TextView reportCount;
    Button searchButton;
    RecyclerView recyclerView;
    Repository repository;
    String sDate;
    String eDate;
    List<Vacation> vacationsInRange;
    DatePickerDialog.OnDateSetListener setStartDate;
    DatePickerDialog.OnDateSetListener setEndDate;
    final Calendar myCalender = Calendar.getInstance();
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    String today;
    SearchAdapter searchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        startDate = findViewById(R.id.earliest_search_date);
        endDate = findViewById(R.id.latest_search_date);
        reportHeader = findViewById(R.id.report_header);
        reportCount = findViewById(R.id.item_count);
        searchButton = findViewById(R.id.search_button);
        recyclerView = findViewById(R.id.search_result_recyclerView);
        repository = new Repository(getApplication());
        searchAdapter = new SearchAdapter(this);
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (savedInstanceState != null) {
            String savedStart = savedInstanceState.getString("START_DATE");
            String savedEnd = savedInstanceState.getString("END_DATE");

            if (savedStart != null) {
                startDate.setText(savedStart);
            }

            if (savedEnd != null) {
                endDate.setText(savedEnd);
            }
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDate = startDate.getText().toString();
                eDate = endDate.getText().toString();
                if (sDate.isEmpty() || eDate.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Please select both dates.", Toast.LENGTH_SHORT).show();
                    return;
                }
                reportHeader.setText("Vacation Report from " + sDate + " to " + eDate + ".");
                vacationsInRange = repository.getVacationsInRange(sDate, eDate);
                int itemCount = vacationsInRange.size();
                reportCount.setText("Total Vacations: " + itemCount);
                searchAdapter.setVacations(vacationsInRange);
            }
        });

        if (savedInstanceState == null) {
            today = sdf.format(new Date());
            startDate.setText(today);
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
                new DatePickerDialog(SearchActivity.this, setStartDate, myCalender.get(Calendar.YEAR),
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
                    Toast.makeText(SearchActivity.this, "Start Date cannot be after End Date.", Toast.LENGTH_LONG).show();
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
                new DatePickerDialog(SearchActivity.this, setEndDate, myCalender.get(Calendar.YEAR),
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
                    Toast.makeText(SearchActivity.this, "End Date cannot be before Start Date.", Toast.LENGTH_LONG).show();
                    return;
                }

                endDate.setText(sdf.format(chosenEnd.getTime()));
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==android.R.id.home){
            this.finish();
            return true;
        }
        return true;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("START_DATE", startDate.getText().toString());
        outState.putString("END_DATE", endDate.getText().toString());
    }


    @Override
    public void onResume(){
        super.onResume();
        sDate = startDate.getText().toString();
        eDate = endDate.getText().toString();
        if (!eDate.isEmpty() && !sDate.isEmpty()) {
            reportHeader.setText("Vacation Report from " + sDate + " to " + eDate + ".");
            vacationsInRange = repository.getVacationsInRange(sDate, eDate);
            int itemCount = vacationsInRange.size();
            reportCount.setText("Total Vacations: " + itemCount);
            searchAdapter.setVacations(vacationsInRange);
        }
    }


}