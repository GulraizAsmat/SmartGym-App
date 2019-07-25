package com.solo9.smartgym;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
public class WorkoutScreen extends AppCompatActivity {
    TextView textView, statusTxt;
    Button btnStart, btnPause, btnBack;
    Toolbar mToolbar;
    private Spinner dropDownMenu;
    long millisecondTime, startTime, timeBuff, updateTime = 0L;
    int seconds, minutes, milliSeconds;
    Handler handler;
    private boolean iniSpinner;
    String exerciseId, exName, workoutTime, workout_id;
    ArrayAdapter<String> mArrayAdapter;
    List<UserModel> labels;
    final Context context = this;
    DataBaseHelper smartGymDb;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_screen);
        handler         = new Handler();
        dropDownMenu.setAdapter(mArrayAdapter);
        setSupportActionBar(mToolbar);
        smartGymDb = new DataBaseHelper(this);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Workout Page");
        btnPause.setEnabled(false);
        registerViews();
        SpinnerLoad();
        dropDownMenu();
        workoutStartButtonPressed();
        StopWorkoutButtonPressed();
        backButtonPressed();
    }

    private void registerViews(){
        textView        = findViewById(R.id.timerView);
        btnStart        = findViewById(R.id.start);
        btnBack         = findViewById(R.id.Back);
        btnPause        = findViewById(R.id.stop);
        statusTxt       = findViewById(R.id.statusValue);
        mToolbar        = findViewById(R.id.workoutBar);
        dropDownMenu    = findViewById(R.id.dropDownWorkout);
    }
    public void setWorkoutTime(String wt) { workoutTime = wt; }
    public String getWorkoutTime() { return workoutTime; }
/**
 * <p>Functionality :when click on the Start button so time automatically  start of stop-watch and after start button will be disable. </p>
 * @setter   SetWorkoutTime that is use for  store the value of current-Time.
 **/
    public void workoutStartButtonPressed() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceAsColor", "SetTextI18n"})
            @Override
            public void onClick(View view) {
                stopWatchRestingMode();
                stopWatchStartingMode();
                btnStart.setEnabled(false);
                btnPause.setEnabled(true);
                statusTxt.setText("Workout");
                Calendar c = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                setWorkoutTime(df.format(c.getTime()));
                btnStart.setEnabled(false);
                btnPause.setEnabled(true);
            }
        });
    }
    public Runnable runnable = new Runnable() {
        @SuppressLint({"DefaultLocale", "SetTextI18n"})
        public void run() {
            millisecondTime = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuff + millisecondTime;
            seconds = (int) (updateTime / 1000);
            minutes = seconds / 60;
            seconds = seconds % 60;
            milliSeconds = (int) (updateTime % 1000);
            textView.setText("" + minutes + ":" + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliSeconds));
            handler.postDelayed(this, 0);
        }
    };
    /**
      When click on the dropDown menu option so data will be fetch to the database and showing  the dropdown menu list.
     * <p>Functionality:
     * 1) Data will be show in alphabetically  ascending order
     * 2) Top value of the list will be disable and can't be use with the help of isEnable method Through.
     * </p>
     * @see    list3  all data store in list .
     */
     public void SpinnerLoad() {
        labels = smartGymDb.getExerciseInformation();
        List<String> name = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        for (int i = 0; i < labels.size(); i++) {
            name.add(labels.get(i).getExeName());
            list1.add(labels.get(i).getExeId());
            list2.add(labels.get(i).getExeName());
        }
                Collections.sort(name);
                smartGymDb.temporaryExerciseInformation();
                smartGymDb.exercise3(name, list1);
                Collections.sort(list3);
        for (int i = 0; i < labels.size(); i++) {
            DataBaseHelper db1 = new DataBaseHelper(getApplicationContext());
            labels = db1.getExerciseInformation();
            Log.e("ExerciseName", "loop");
            list3.add(labels.get(i).getExeName());
        }
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list3) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the disable item text color
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        dropDownMenu.setAdapter(mArrayAdapter);
        mArrayAdapter.notifyDataSetChanged();
    }
    public void dropDownMenu() {
        dropDownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!iniSpinner) {
                    iniSpinner = true;
                    return;
                }
                labels.get(position);
                String value = labels.get(position).getExeId();
                String value1 = labels.get(position).getExeName();
                Log.e("ExerciseName", "click" + value1);
                setExerciseId(value);
                setExName(value1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
            public void setExerciseId(String exId) { exerciseId = exId; }
            public String getExerciseId() { return exerciseId; }
            public void setExName(String exname) { exName = exname; }
            public String getExName() { return exName; }
    /**
     * When click on stopButton so In case  data  will be store in database  and then popUp dialog box will be open .
     * If fill the requirements of popup Dialog box .So in  case workout table of database will be update otherwise without any changing data saved in dataBase .
     * <p>Functionality:
     * 1)  Status in resting mode when click the stop button.
     * 2)  All data Store in dataBase when click the Stop button.
     * 3) when click the ok button of dialog box  so in case only rap, comment ,weight data store in database otherwise not .
     * </p>
     * @param  sessionEnd that is use for store the value  of session end Time .
     * @param  date that is use for store current date.
     */
    public void StopWorkoutButtonPressed() {
        btnPause.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceAsColor", "SetTextI18n"})
            @Override
            public void onClick(View arg0) {
                // get prompts.xml view
                stopWatchRestingMode();
                timeBuff += millisecondTime;
                //   handler.postDelayed((Runnable) WorkoutScreen.this, 0);
                handler.removeCallbacks(runnable);
                LayoutInflater li = LayoutInflater.from(context);
                @SuppressLint("InflateParams") View promptsView = li.inflate(R.layout.workoutpompts, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                final EditText weight = promptsView
                        .findViewById(R.id.editTextDialogUserInput1);
                final EditText raps = promptsView
                        .findViewById(R.id.editTextDialogUserInput2);
                final EditText comments = promptsView
                        .findViewById(R.id.editTextDialogUserInput4);
                stopWatchStartingMode();
                statusTxt.setText("Resting");
                final String sessionId = getIntent().getStringExtra("session_id");
                Log.d("result", String.valueOf(sessionId));
                Date c = Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String date = df.format(c);
                Calendar c1 = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
                String workoutEndTime = df1.format(c1.getTime());
                String UserID = getIntent().getStringExtra("UserId");
                String SessionStart = getIntent().getStringExtra("sessionStart");
                double workout_id1 = Math.random();
                final String workout_id = String.valueOf(workout_id1);
                final Boolean result = smartGymDb.setWorkoutInformation(workout_id, sessionId, getExerciseId(), getWorkoutTime(), workoutEndTime, UserID, SessionStart, getExName(), date);
                if (result == Boolean.TRUE) {
                    Toast.makeText(WorkoutScreen.this, "Finish Exercise... ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WorkoutScreen.this, "error", Toast.LENGTH_LONG).show();
                }
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
                dropDownMenu.setEnabled(true);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        final String weightValue = weight.getText().toString();
                                        final String rapsValue = raps.getText().toString();
                                        final String commentValue = comments.getText().toString();
                                        if (commentValue.length() < 40) {
                                            smartGymDb.updateWorkOutTableForAddPopUpInforamtion(workout_id, rapsValue, weightValue, commentValue);
                                        } else {
                                            Toast.makeText(WorkoutScreen.this, "maximum 50 characters write in comment section ", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        btnStart.setEnabled(true);
                                        btnPause.setEnabled(true);
                                        statusTxt.setText("Resting");
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });
    }
    /**
     * when click the button of back so in this case Session activity will be open .
     */
    public void backButtonPressed() {
        final String sessionId = getIntent().getStringExtra("session_id");
        final String userName = getIntent().getStringExtra("UserName");
        final String userId = getIntent().getStringExtra("UserId");
        final String sessionStart = getIntent().getStringExtra("sessionStart");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(WorkoutScreen.this, Session.class);
                back.putExtra("session_id", sessionId);
                back.putExtra("UserId", userId);
                back.putExtra("sessionStart", sessionStart);
                back.putExtra("UserName", userName);
                back.putExtra("workout_id", workout_id);
                startActivity(back);
                finish();
            }
        });
    }
    /**
     * Basically StopWatchRestingMode use for reset the time of stopWatch.
     */
    @SuppressLint("SetTextI18n")
    public void stopWatchRestingMode() {
        millisecondTime = 0L;
        startTime = 0L;
        timeBuff = 0L;
        updateTime = 0L;
        seconds = 0;
        minutes = 0;
        milliSeconds = 0;
        textView.setText("00:00:00");
    }
    /**
     * Basically StopWatchStartingMode use for start  the time of stopWatch.
     */
    public void stopWatchStartingMode() {
        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
    }
}
