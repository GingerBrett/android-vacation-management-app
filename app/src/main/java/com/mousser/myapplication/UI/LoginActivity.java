package com.mousser.myapplication.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mousser.myapplication.Dao.UserDao;
import com.mousser.myapplication.Database.VacationDatabase;
import com.mousser.myapplication.Entites.User;
import com.mousser.myapplication.R;

import java.util.concurrent.Executors;

import com.mousser.myapplication.Security.PasswordUtils;

public class LoginActivity extends AppCompatActivity {

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        VacationDatabase db = VacationDatabase.getDatabase(getApplicationContext());
        userDao = db.userDao();

        EditText usernameText = findViewById(R.id.usernameText);
        EditText passwordText = findViewById(R.id.passwordText);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();
                Executors.newSingleThreadExecutor().execute(() -> {
                    User user = userDao.getUser(username);

                    runOnUiThread(() -> {
                        if (user != null && PasswordUtils.checkPassword(password, user.getPasswordHash())) {

                            // Login successful
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // Login failed
                            Toast.makeText(
                                    LoginActivity.this,
                                    "Invalid credentials\nTry again",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });
                });
            }
        })
    ;}
}