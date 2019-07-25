package com.solo9.smartgym;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    final Context context = this;
    private Button addNewUser, addNewExercise, btnExit;
    private Spinner dropDownMenu;
    private boolean iniSpinner;
    DataBaseHelper smartGymDb;
    List<UserModel> labels;
    ArrayAdapter<String> arrayAdapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addNewUser     =  findViewById(R.id.AddNewUser);
        addNewExercise =  findViewById(R.id.AddNewExercise);
        btnExit        =  findViewById(R.id.Exit);
        dropDownMenu   =  findViewById(R.id.DropDown);
        dropDownMenu.setAdapter(arrayAdapter);
        smartGymDb = new DataBaseHelper(this);
        emptyExerciseList();
        addNewUserButtonPress();
        addNewExerciseButtonPressed();
        exitButtonPressed();
        SpinnerLoad();
        DropDownList();
    }
    /**
     * addNewUserButtonPress function basically use for add new user in Database.
     * @param userName
     * @param userId
     */
    public void addNewUserButtonPress() {
        addNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                LayoutInflater li = LayoutInflater.from(context);
                final View promptsView = li.inflate(R.layout.prompts,null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                alertDialogBuilder.setView(promptsView);
                final EditText userInput =  promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String userName = userInput.getText().toString();
                                        if (userName.equals("")) {
                                            Toast.makeText(MainActivity.this, "empty field", Toast.LENGTH_LONG).show();
                                        } else {
                                            userName = userName.toLowerCase();
                                            boolean nameChecker = smartGymDb.checkForUniqueUsername(userName);
                                            if (nameChecker) {
                                                Toast.makeText(MainActivity.this, "user already Register ", Toast.LENGTH_SHORT).show();
                                            } else {
                                                double user_id1 = Math.random();
                                                String user_id = String.valueOf(user_id1);
                                                boolean addUser = smartGymDb.setUserInformation(userName, user_id);
                                                if (addUser) {
                                                    Log.d("ok", "working");
                                                    Toast.makeText(MainActivity.this, "User Add Successfully ...", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    Log.d("ok", "not working");
                                                    Toast.makeText(MainActivity.this, "Database Issue", Toast.LENGTH_SHORT).show(); }
                                            }
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
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
     * addNewExerciseButtonPress function basically use for add new user in Database.
     * @param exerciseName
     * @param exerciseId
     */
    public void addNewExerciseButtonPressed() {

        addNewExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                LayoutInflater li = LayoutInflater.from(context);
                @SuppressLint("InflateParams") View promptsView = li.inflate(R.layout.promptsexercise, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                alertDialogBuilder.setView(promptsView);
                final EditText userInput =  promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String exercise = userInput.getText().toString();

                                        if (exercise.equals("")) {
                                            Toast.makeText(MainActivity.this, "empty field", Toast.LENGTH_LONG).show();
                                        } else {
                                            exercise = exercise.toLowerCase();

                                            // exercise = exercise.replace(" ", "");
                                            // exercise = exercise.trim();

                                            boolean result = smartGymDb.checkForUniqueExerciseName(exercise);
                                            if (result) {
                                                Toast.makeText(MainActivity.this, "exercise  already Register ", Toast.LENGTH_SHORT).show();
                                            } else {
                                                double exercise_id = Math.random();
                                                String exercise_id1 = String.valueOf(exercise_id);
                                                boolean mExerciseTable = smartGymDb.setExerciseInformation(exercise, exercise_id1, "exercise2");

                                                if (mExerciseTable ) {
                                                    Log.d("ok", "working");
                                                    Toast.makeText(MainActivity.this, "exercise add successfully ", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Log.d("ok", "not working");
                                                    Toast.makeText(MainActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
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
    public void exitButtonPressed() {
        btnExit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
    } /**
     When click on the dropDown menu option so data will be fetch to the database and showing  the dropdown menu list.
     * <p>Functionality:
     * 1) Top value of the list will be disable and can't be use with the help of isEnable method Through.
     * </p>
     * @see    list1   store the all data in list .
     */
    public void SpinnerLoad() {
        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        labels = db.getUserInformation();
        List<String> list1 = new ArrayList<>();
        if (labels.isEmpty()) {
            Boolean result = db.setUserInformation("Select the user", "userID");
            if (result ) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "working not", Toast.LENGTH_SHORT).show();
            }

        } else {
            for (int i = 0; i < labels.size(); i++) {
                list1.add(labels.get(i).getUserName());
            }
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list1) {
                @Override
                public boolean isEnabled(int position) {
                    return position != 0;
                }
                @Override
                public View getDropDownView(int position, View convertView,  ViewGroup parent) {
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
            dropDownMenu.setAdapter(arrayAdapter);
        }
    }
    public void DropDownList() {
        final DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        dropDownMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!iniSpinner) {
                    iniSpinner = true;
                    return;
                }
                String label = parent.getItemAtPosition(position).toString();
                boolean data = db.setUsername(label);
                if (data) {
                    labels.get(position);
                    Intent intent = new Intent(MainActivity.this, Session.class);
                    // intent.putExtra("UserName",labels.get(position).getName());
                    intent.putExtra("UserId", labels.get(position).getUserid());
                    intent.putExtra("UserName", label);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    /**
     *  when exercise table empty  of database .So by default one Entity store in database .
     */
    public void emptyExerciseList() {
        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        labels = db.getExerciseInformation();
        if (labels.isEmpty()) {
            db.setExerciseInformation("Select the Exercise", "exerciseid", "exercise2");
        }
    }
}