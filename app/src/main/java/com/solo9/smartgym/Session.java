package com.solo9.smartgym;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Session extends AppCompatActivity {

    Button btnSessionStart;
    Button btnSessionStop, btnBack, btnSessionLog;
    Toolbar toolBar;
    DataBaseHelper smartGymDb;
    boolean True = true;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        toolBar = findViewById(R.id.sessionBar);
        setSupportActionBar(toolBar);
        btnBack = findViewById(R.id.backscreen);
        String userName = getIntent().getStringExtra("UserName");
        Objects.requireNonNull(getSupportActionBar()).setTitle("Welcome " + userName);
        smartGymDb = new DataBaseHelper(this);
        sessionStartButtonPressed();
        backButtonPressed();
        sessionEndButtonPressed();
        sessionLogButtonPressed();

    }

    public void sessionStartButtonPressed() {
        btnSessionStart = findViewById(R.id.sessionStart);
        btnSessionStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = getIntent().getStringExtra("UserId");
                Calendar c = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                String sessionStartTime = df.format(c.getTime());
                double sessionId1 = Math.random();
                String sessionId = String.valueOf(sessionId1);
                Log.d("sessionEnd", sessionId);
                Boolean result = smartGymDb.setSessionTable(sessionStartTime, userId, sessionId);
                if (result) {
                    String userName = getIntent().getStringExtra("UserName");
                    Toast.makeText(Session.this, "SessionStart", Toast.LENGTH_LONG).show();
                    Intent sessionToWorkout = new Intent(Session.this, WorkoutScreen.class);
                    sessionToWorkout.putExtra("session_id", sessionId);
                    sessionToWorkout.putExtra("UserId", userId);
                    sessionToWorkout.putExtra("sessionStart", sessionStartTime);
                    sessionToWorkout.putExtra("UserName", userName);
                    startActivity(sessionToWorkout);
                } else {
                    Toast.makeText(Session.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void sessionEndButtonPressed() {
        final String SessionId = getIntent().getStringExtra("session_id");
        final String userId = getIntent().getStringExtra("UserId");
        final String SessionStart = getIntent().getStringExtra("sessionStart");
        btnSessionStop = findViewById(R.id.sessionStop);
        btnSessionStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                String SessionEndTime = df.format(c.getTime());
                String SessionId1 = String.valueOf(SessionId);
                Boolean result = smartGymDb.updateSessionTable(SessionId1, SessionEndTime, SessionStart, userId);
                if (result == True) {
                    List<UserModel> List;
                    List = smartGymDb.getWorkoutInformtion();
                    for (int i = 0; i < List.size(); i++) {
                        String name12 = List.get(i).getUserid();
                        String name123 = List.get(i).getSessionID();
                        if (userId.equalsIgnoreCase(name12)) {
                            if (SessionId1.equalsIgnoreCase(name123)) {
                                Log.d("working1", name12);
                                Log.d("working1", userId);
                                String sessionId = List.get(i).getSessionID();
                                Boolean result1 = smartGymDb.updateWorkoutTableForSessionEndTime(sessionId, SessionEndTime);
                                if (result1 == True) {
                                    Log.d("working1", "end");
                                } else {
                                    Log.d("working1", "error");
                                }

                            }
                        }
                    }
                    Toast.makeText(Session.this, "Session End", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Session.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void backButtonPressed() {

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Session.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void sessionLogButtonPressed() {
        btnSessionLog = findViewById(R.id.SessionDownload);
        btnSessionLog.setOnClickListener(new View.OnClickListener() {
            String userId = getIntent().getStringExtra("UserId");
            String userName = getIntent().getStringExtra("userName");

            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Session.this, SessionLog.class);
                intent1.putExtra("Username", userName);
                intent1.putExtra("UserId", userId);

                startActivity(intent1);

            }
        });

    }


}


