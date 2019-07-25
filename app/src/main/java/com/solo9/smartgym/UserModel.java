package com.solo9.smartgym;
public class UserModel {
    private String userid;
    private String userName;
            String exeId;
    private String exeName;
    private String SessionID;
    private String WorkoutID;
    private String Weight;
    private String raps;
    private String comments;
    private String sessionStartTime;
    private String sessionsEndTime;
    private String session;
    private String exercise;
    private String workoutStart;
    private String workoutEnd;
    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    String getDate() {
        return date;
    }
    void setDate(String date) {
        this.date = date;
    }
    private String date;
    String getWorkoutStart() {
        return workoutStart;
    }
    void setWorkoutStart(String workoutStart) {
        this.workoutStart = workoutStart;
    }
    String getWorkoutEnd() {
        return workoutEnd;
    }
    void setWorkoutEnd(String workoutEnd) {
        this.workoutEnd = workoutEnd;
    }
    void setSession(String session) {
        this.session = session;
    }
    String getSessionStartTime() {
        return sessionStartTime;
    }
    void setSessionStartTime(String sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }
    String getSessionsEndTime() {
        return sessionsEndTime;
    }
    void setSessionsEndTime(String sessionsEndTime) {
        this.sessionsEndTime = sessionsEndTime;
    }
    String getUserid() {
        return userid;
    }
    void setUserid(String userid) {
        this.userid = userid;
    }
    String getUserName() {
        return userName;
    }
    void setUserName(String userName) {
        this.userName = userName;
    }
    String getExeId() {
        return exeId;
    }
    void setExeId(String exeId) {
        this.exeId = exeId;
    }
    String getExeName() {
        return exeName;
    }
    void setExeName(String exeName) {
        this.exeName = exeName;
    }
    String getSessionID() {
        return SessionID;
    }
    void setSessionID(String sessionID) {
        SessionID = sessionID;
    }
    String getWorkoutID() {
        return WorkoutID;
    }
    void setWorkoutID(String workoutID) {
        WorkoutID = workoutID;
    }
    String getWeight() {
        return Weight;
    }
    void setWeight(String weight) {
        Weight = weight;
    }
    String getRaps() {
        return raps;
    }
    void setRaps(String raps) {
        this.raps = raps;
    }
    String getComments() {
        return comments;
    }
    void setComments(String comments) {
        this.comments = comments;
    }
}
