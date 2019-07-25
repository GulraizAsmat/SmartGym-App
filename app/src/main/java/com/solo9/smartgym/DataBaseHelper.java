package com.solo9.smartgym;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
public class DataBaseHelper extends SQLiteOpenHelper {
    DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    /**
     * create all tables of Database .
     * @param userTable  Store all information of user
     * @param exerciseTable Store all information of exercise
     * @param workoutTable Store all information  related to workout period.
     * @param sessionTable Store all information of session period.
     * @param temporaryUserInformationTable Store all information  related to user and information store for temporary.
     */
    private static final String DATABASE_NAME = "Gym";
    private static final int DATABASE_VERSION = 81;
    private static final String userTable = ("create table  IF NOT EXISTS user(number  INTEGER PRIMARY KEY,User_id text,username text)");
    private static final String exerciseTable = ("create table IF NOT EXISTS exercise(number INTEGER PRIMARY KEY ,exercise_id text, exercise_title Text,exerciseName Text)");
    private static final String sessionTable = ("create table IF NOT EXISTS session(number INTEGER PRIMARY KEY,sessionName text ,session_id text ,sessionStartTime Text,sessionEndTime Text,User_id Text)");
    private static final String workOutTable = ("create table IF NOT EXISTS workout(number INTEGER PRIMARY KEY,workout_id Text,workout_startTime text,workout_endTime,User_id Text,session_id text,exercise_id text,weight text,raps Text,comments text,sessionStartTime text, sessionEndTime text,sessionName text,exName Text,date text)");
    private static final String temporaryUserInformationTable = ("create table IF NOT EXISTS templ(number INTEGER PRIMARY KEY,workout_id Text,workout_startTime text,workout_endTime,User_id Text,session_id text,exercise_id text,weight text,raps Text,comments text,sessionStartTime text, sessionEndTime text,sessionName text,exName Text,date text)");
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("yes", "call Oncreate");
        db.execSQL(userTable);
        db.execSQL(exerciseTable);
        db.execSQL(sessionTable);
        db.execSQL(workOutTable);
        db.execSQL(temporaryUserInformationTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("yes", "OnUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + "user");
        db.execSQL("DROP TABLE IF EXISTS " + "exercise");
        db.execSQL("DROP TABLE IF EXISTS " + "session");
        db.execSQL("DROP TABLE IF EXISTS " + "workout");
        db.execSQL("DROP TABLE IF EXISTS " + "templ");
        onCreate(db);
    }

    /**
     * @param userName
     * @param userId
     * @return result
     */
    Boolean setUserInformation(String userName, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", userName);
        contentValues.put("User_id", userId);
        long result = db.insert("user", null, contentValues);
        return result != -1;
    }

    /**
     * @param exName
     * @param exId
     * @param exercise
     * @return
     */
    Boolean setExerciseInformation(String exName, String exId, String exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("exercise_id", exId);
        contentValues.put("exercise_title", exName);
        contentValues.put("exerciseName", exercise);
        long result = db.insert("exercise", null, contentValues);
        return result != -1;
    }
    /**
     * @return list
     */
    List<UserModel> getUserInformation() {
        List<UserModel> list = new ArrayList<>();
        String selectQuery = "select * from user";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            UserModel userModel = new UserModel();
            userModel.setUserid(res.getString(res.getColumnIndex("User_id")));
            userModel.setUserName(res.getString(res.getColumnIndex("username")));
            list.add(userModel);
            res.moveToNext();
        }return list;
    }
    /**
     * @return list
     */
    List<UserModel> getExerciseInformation() {
        List<UserModel> list = new ArrayList<>();
        String selectQuery = "select * from exercise";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            UserModel userModel = new UserModel();
            userModel.setExeId(res.getString(res.getColumnIndex("exercise_id")));
            userModel.setExeName(res.getString(res.getColumnIndex("exercise_title")));
            userModel.setExercise(res.getString(res.getColumnIndex("exerciseName")));
            list.add(userModel);
            res.moveToNext();
        }
        return list;
    }

    /**
     * @return list
     */
    List<UserModel> getWorkoutInformtion() {
        List<UserModel> list = new ArrayList<>();
        String selectQuery = "select * from workout";
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            UserModel userModel = new UserModel();
            userModel.setWorkoutID(res.getString(res.getColumnIndex("workout_id")));
            userModel.setUserid(res.getString(res.getColumnIndex("User_id")));
            userModel.setSessionID(res.getString(res.getColumnIndex("session_id")));
            userModel.setExeId(res.getString(res.getColumnIndex("exercise_id")));
            userModel.setWeight(res.getString(res.getColumnIndex("weight")));
            userModel.setRaps(res.getString(res.getColumnIndex("raps")));
            userModel.setComments(res.getString(res.getColumnIndex("comments")));
            userModel.setWorkoutStart(res.getString(res.getColumnIndex("workout_startTime")));
            userModel.setWorkoutEnd(res.getString(res.getColumnIndex("workout_endTime")));
            userModel.setSessionStartTime(res.getString(res.getColumnIndex("sessionStartTime")));
            userModel.setSessionsEndTime(res.getString(res.getColumnIndex("sessionEndTime")));
            userModel.setSession(res.getString(res.getColumnIndex("sessionName")));
            userModel.setExeName(res.getString(res.getColumnIndex("exName")));
            userModel.setDate(res.getString(res.getColumnIndex("date")));
            list.add(userModel);
            res.moveToNext();
        }
        return list;
    }
    /**
     * @return
     */
    List<UserModel> getTemporaryInformationForUser() {
        List<UserModel> list = new ArrayList<>();
        String selectQuery = "select * from templ";
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            UserModel userModel = new UserModel();
            userModel.setWorkoutID(res.getString(res.getColumnIndex("workout_id")));
            userModel.setUserid(res.getString(res.getColumnIndex("User_id")));
            userModel.setSessionID(res.getString(res.getColumnIndex("session_id")));
            userModel.setExeId(res.getString(res.getColumnIndex("exercise_id")));
            userModel.setWeight(res.getString(res.getColumnIndex("weight")));
            userModel.setRaps(res.getString(res.getColumnIndex("raps")));
            userModel.setComments(res.getString(res.getColumnIndex("comments")));
            userModel.setWorkoutStart(res.getString(res.getColumnIndex("workout_startTime")));
            userModel.setWorkoutEnd(res.getString(res.getColumnIndex("workout_endTime")));
            userModel.setSessionStartTime(res.getString(res.getColumnIndex("sessionStartTime")));
            userModel.setSessionsEndTime(res.getString(res.getColumnIndex("sessionEndTime")));
            userModel.setSession(res.getString(res.getColumnIndex("sessionName")));
            userModel.setExeName(res.getString(res.getColumnIndex("exName")));
            list.add(userModel);
            res.moveToNext();
        }
        return list;
    }

    /**
     * @param workoutId
     * @param sessionId
     * @param exercise_id
     * @param workoutStartTime
     * @param workoutEndTime
     * @param userId
     * @param sessionStart
     * @param ExName
     * @param date
     * @return
     */

    boolean setWorkoutInformation(String workoutId, String sessionId, String exercise_id, String workoutStartTime, String workoutEndTime, String userId, String sessionStart, String ExName, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("workout_id", workoutId);
        contentValues.put("session_id", sessionId);
        contentValues.put("exercise_id", exercise_id);
        contentValues.put("workout_startTime", workoutStartTime);
        contentValues.put("workout_endTime", workoutEndTime);
        contentValues.put("User_id", userId);
        contentValues.put("sessionStartTime", sessionStart);
        contentValues.put("sessionName", "Session :");
        contentValues.put("exName", ExName);
        contentValues.put("date", date);
        long result = db.insert("workout", null, contentValues);
        return result != -1;
    }

    /**
     * @param workoutId
     * @param sessionId
     * @param exerciseId
     * @param workoutStartTime
     * @param workoutEndTime
     * @param weight
     * @param raps
     * @param comments
     * @param userid
     * @param sessionStart
     * @param ExName
     * @param endTime
     * @param date
     */
    void setTemporaryInformationForUser(String workoutId, String sessionId, String exerciseId, String workoutStartTime, String workoutEndTime, String weight, String raps, String comments, String userid, String sessionStart, String ExName, String endTime, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("workout_id", workoutId);
        contentValues.put("session_id", sessionId);
        contentValues.put("exercise_id", exerciseId);
        contentValues.put("workout_startTime", workoutStartTime);
        contentValues.put("workout_endTime", workoutEndTime);
        contentValues.put("weight", weight);
        contentValues.put("raps", raps);
        contentValues.put("comments", comments);
        contentValues.put("User_id", userid);
        contentValues.put("sessionStartTime", sessionStart);
        contentValues.put("sessionName", "Session :");
        contentValues.put("exName", ExName);
        contentValues.put("sessionEndTime", endTime);
        contentValues.put("date", date);
          db.insert("templ", null, contentValues);
    }

    /**
     * @param sessionStartTime
     * @param UserId
     * @param Session_id
     * @return
     */
    Boolean setSessionTable(String sessionStartTime, String UserId, String Session_id) //
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sessionStartTime", sessionStartTime);
        contentValues.put("User_id", UserId);
        contentValues.put("session_id", Session_id);
        contentValues.put("sessionName", "Session ");
        long result = sqLiteDatabase.insert("session", null, contentValues);
        return result != -1;
    }

    /**
     * @param SessionId
     * @param EndTime
     * @param StartTime
     * @param userId
     * @return
     */
    Boolean updateSessionTable(String SessionId, String EndTime, String StartTime, String userId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sessionEndTime", EndTime);
        contentValues.put("sessionStartTime", StartTime);
        contentValues.put("sessionName", "Session ");
        contentValues.put("User_id", userId);
        sqLiteDatabase.update("session", contentValues, "session_id= ? ", new String[]{SessionId});
        return true;
    }
    /**
     * @param sessionId
     * @param sessionEnd
     * @return
     */
    Boolean updateWorkoutTableForSessionEndTime(String sessionId, String sessionEnd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sessionEndTime", sessionEnd);
        contentValues.put("sessionName", "Session :");
        db.update("workout", contentValues, "session_id= ?", new String[]{sessionId});
        return true;
    }
    /**
     * @param sessionId
     * @param weight
     * @param raps
     * @param comment
     */
    void updateWorkOutTableForAddPopUpInforamtion(String sessionId, String weight, String raps, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("weight", weight);
        contentValues.put("raps", raps);
        contentValues.put("comments", comment);
        db.update("workout", contentValues, "workout_id= ?", new String[]{sessionId});
    }
    Boolean setUsername(String username) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = sqLiteDatabase.rawQuery("select User_id from user where username=? ", new String[]{username});
        return cursor.getCount() != 0;
    }
    boolean checkForUniqueUsername(String userName) {
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from user where username=? ", new String[]{userName});
        return cursor.getCount() != 0;
    }
    /**
     * @param exerciseName
     * @return
     */
    boolean checkForUniqueExerciseName(String exerciseName) {
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from exercise where exercise_title=? ", new String[]{exerciseName});
        return cursor.getCount() != 0;
    }
    /**
     * @param id
     */
    void temporaryDeleteUserInformation(String id) {
        SQLiteDatabase sq = this.getWritableDatabase();
        sq.delete("templ", "User_id= ?", new String[]{id});
    }

    Cursor getDistinctSessions() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery("select * from templ group by session_id order by number desc", null);
    }
    /**
     * @param sessionId
     * @return
     */
    Cursor getSessionFromSessionId(String sessionId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from templ where session_id ='" + sessionId + "'", null);
        return res;

    }
    /**
     * @param contact
     * @param exerciseId
     */
    void exercise3(List<String> contact, List<String> exerciseId) {
        SQLiteDatabase DB = this.getWritableDatabase();
        for (int i = 0; i < contact.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put("exercise_title", contact.get(i));
            cv.put("exercise_id", exerciseId.get(i));
            cv.put("exerciseName", "exercise2");
            DB.insert("exercise", null, cv);
        }
    }
    void temporaryExerciseInformation() {
        SQLiteDatabase sq = this.getWritableDatabase();
        sq.delete("exercise", "exerciseName= ?", new String[]{"exercise2"});
    }
}