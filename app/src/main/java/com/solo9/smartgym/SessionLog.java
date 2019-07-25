package com.solo9.smartgym;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.R.layout;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class SessionLog extends AppCompatActivity {
    List<UserModel>List;
    ListView listView;
    Toolbar toolbar;
    DataBaseHelper smartGymDb;
    String TAG = "sessionLogButtonPressed";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_log);
        listView                    = findViewById(R.id.listView1);
        toolbar                     = findViewById(R.id.LogBar);
        smartGymDb                  = new DataBaseHelper(this);
        List                        = smartGymDb.getWorkoutInformtion();
        final String mUserId        = getIntent().getStringExtra("UserId");
        String Username             =getIntent().getStringExtra("Username");
        Objects.requireNonNull(getSupportActionBar()).setTitle(Username);
        setSupportActionBar(toolbar);
        int counter =1;
        for (int i= 0; i < List.size(); i++) {
            final String userId = List.get(i).getUserid();
            if (mUserId.equalsIgnoreCase(userId)) {
                String ExerciseName = List.get(i).getExeName();
                String time = List.get(i).getSessionStartTime();
                String wStart = List.get(i).getWorkoutStart();
                String weight = List.get(i).getWeight();
                String raps = List.get(i).getRaps();
                String comments = List.get(i).getComments();
                String wEnd = List.get(i).getWorkoutEnd();
                String sessionEndTime = List.get(i).getSessionsEndTime();
                String workoutId =List.get(i).getWorkoutID();
                String sessionID=List.get(i).getSessionID();
                String exId =List.get(i).exeId;
                String date=List.get(i).getDate();
                smartGymDb.setTemporaryInformationForUser(workoutId,sessionID, exId,wStart,wEnd, weight,raps,comments, mUserId,time,ExerciseName, sessionEndTime,date);
            }
        }
        List=smartGymDb.getTemporaryInformationForUser();
        final Cursor cu = smartGymDb.getDistinctSessions();
        final List<UserModel> userModelList = new ArrayList<>();
        final List<String> userModelList2   = new ArrayList<>();
        while(cu.moveToNext()){
            UserModel userModel = new UserModel();
            userModel.setSessionID(cu.getString(5));
           userModel.setSession(cu.getString(13));
            userModelList2.add(cu.getString(12)+ counter);
            userModelList.add(userModel);
            counter++;
        }
        ArrayAdapter<String> Adapter;
        Adapter = new ArrayAdapter<>(this, layout.simple_list_item_1,userModelList2);
        listView.setAdapter(Adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG, "Position: "+position);
                String sessionId = userModelList.get(position).getSessionID();
                Cursor cs = smartGymDb.getSessionFromSessionId(sessionId);
             int counter1 =1;
                    StringBuilder messageShow = new StringBuilder();
                while (cs.moveToNext()) {
                        if (counter1 == 1) {
                            messageShow.append("-------------------------------------------------------\n \n");
                            messageShow.append("Date                  :").append(cs.getString(14)).append("\n");
                            messageShow.append("Session Start :").append(cs.getString(10)).append("\n");
                            messageShow.append("Session End   :").append(cs.getString(11)).append("\n").append("\n-------------------------------------------------------\n");
                            counter1 = 2;
                        }
                            messageShow.append("Exercise Name    : " + " ").append(cs.getString(13)).append("\n");
                            messageShow.append("Workout  Start     :" + " ").append(cs.getString(2)).append("\n");
                            messageShow.append("Weight                    : " + " ").append(cs.getString(7)).append("\n");
                            messageShow.append("Raps                         : " + " ").append(cs.getString(8)).append("\n");
                            messageShow.append("Comment              : " + " ").append(cs.getString(9)).append("\n");
                            messageShow.append("Workout  End       : " + " ").append(cs.getString(3)).append("\n").append("\n------------------------------------------------------\n");

                    } sessionDetailsShow(messageShow.toString());
            }
        });
    }
    /**
     *
     * @param message0 that is use for show all data in dialog box.
     */
    public void sessionDetailsShow(String message0) {
        new AlertDialog.Builder(SessionLog.this)
                .setTitle("Session")
                .setMessage(message0)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
    @Override
    protected void onStop() {
        super.onStop();
            userRecordDel();
            finish();
    }
    public void userRecordDel(){
        final String userId = getIntent().getStringExtra("UserId");
         smartGymDb.temporaryDeleteUserInformation(userId);
    }
}