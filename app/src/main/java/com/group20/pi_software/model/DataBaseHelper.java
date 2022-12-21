package com.group20.pi_software.model;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.group20.pi_software.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {


    protected static final String DATABASE_NAME = "user_data.db";

    protected static final String USER_TABLE = "user";
    protected static final String COLUMN_ID = "id";
    protected static final String COLUMN_FIRST_NAME = "firstName";
    protected static final String COLUMN_EMAIL = "email";
    protected static final String COLUMN_DATE_JOINED = "dateJoined";
    protected static final String COLUMN_DATE_OF_BIRTH = "dateOfBirth";
    protected static final String COLUMN_PROFILE_PICTURE = "profilePicture";
    protected static final String COLUMN_WEIGHT = "weight";
    protected static final String COLUMN_HEIGHT = "height";
    protected static final String COLUMN_STEPS_GOAL = "stepsGoal";
    protected static final String COLUMN_EXERCISE_MINUTES_GOAL = "exerciseMinutesGoal";
    protected static final String COLUMN_STUDY_HOURS_GOAL = "studyHoursGoal";
    protected static final String COLUMN_SLEEP_HOURS_GOAL = "sleepHoursGoal";


    protected static final String SLEEP_ID = "id";
    protected static final String SLEEP_TABLE = "sleep_table";
    protected static final String SLEEP_START_TIME = "startTime";
    protected static final String SLEEP_END_TIME = "endTime";
    protected static final String SLEEP_DATE = "sleepDate";

    protected static final String EXERCISE_ID = "id";
    protected static final String EXERCISE_TABLE = "exercise_table";
    protected static final String EXERCISE_START_TIME = "startTime";
    protected static final String EXERCISE_END_TIME = "endTime";
    protected static final String EXERCISE_DATE = "exerciseDate";

    protected static final String STUDY_ID = "id";
    protected static final String STUDY_TABLE = "study_table";
    protected static final String STUDY_DATE = "date";
    protected static final String STUDY_START_TIME = "startTime";
    protected static final String STUDY_END_TIME = "endTime";

    protected static final String STEPS_TABLE = "steps_table";
    protected static final String STEPS_ID = "id";
    protected static final String STEPS_DATE = "date";
    protected static final String STEPS_TIME = "time";
    protected static final String STEPS_NUM = "stepsNo";




    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {



        String createUserTableStatement = "CREATE TABLE " + USER_TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_DATE_JOINED + " TEXT, " +
                COLUMN_DATE_OF_BIRTH + " TEXT, " +
                COLUMN_PROFILE_PICTURE + " TEXT, " +
                COLUMN_WEIGHT + " REAL, " +
                COLUMN_HEIGHT + " INTEGER, " +
                COLUMN_STEPS_GOAL + " INTEGER, " +
                COLUMN_EXERCISE_MINUTES_GOAL + " INTEGER," +
                COLUMN_STUDY_HOURS_GOAL + " REAL, " +
                COLUMN_SLEEP_HOURS_GOAL + " REAL" +
                ")";

        String createSleepTableStatement = "CREATE TABLE " + SLEEP_TABLE + " (" +
                SLEEP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SLEEP_DATE + " TEXT, " +
                SLEEP_START_TIME + " TEXT, " +
                SLEEP_END_TIME + " TEXT" +
                ")";

        String createExerciseTableStatement = "CREATE TABLE " + EXERCISE_TABLE + " (" +
                EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EXERCISE_DATE + " TEXT, " +
                EXERCISE_START_TIME + " TEXT, " +
                EXERCISE_END_TIME + " TEXT" +
                ")";

        String createStudyTableStatement = "CREATE TABLE " + STUDY_TABLE + " (" +
                STUDY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STUDY_DATE + " TEXT, " +
                STUDY_START_TIME + " TEXT, " +
                STUDY_END_TIME + " TEXT" +
                ")";

        String createStepsTableStatement = "CREATE TABLE " + STEPS_TABLE + " (" +
                STEPS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STEPS_DATE + " TEXT, " +
                STEPS_TIME + " TEXT, " +
                STEPS_NUM+ " INTEGER" +
                ")";

        try {
            db.execSQL(createStudyTableStatement); // just this part <----------------
        } catch (SQLiteException e) {
            try {
                throw new IOException(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        try {
            db.execSQL(createUserTableStatement);
            db.execSQL(createStepsTableStatement);
            db.execSQL(createSleepTableStatement);
            db.execSQL(createExerciseTableStatement);
            db.execSQL(createStudyTableStatement);
        } catch (SQLiteException e) {
            try {
                throw new IOException(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public void clearDatabase(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        db.execSQL(clearDBQuery);
    }

    public void clearAll(){
        clearDatabase(STUDY_TABLE);
        clearDatabase(SLEEP_TABLE);
        clearDatabase(EXERCISE_TABLE);
        clearDatabase(STEPS_TABLE);
        clearDatabase(USER_TABLE);
    }

    public boolean addStudySession(StudyModel studyModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(STUDY_DATE, studyModel.getDate());
        cv.put(STUDY_START_TIME, studyModel.getStartTime());
        cv.put(STUDY_END_TIME, studyModel.getEndTime());

        long insert;
        try {
            insert = db.insert(STUDY_TABLE, null, cv);
        } catch (SQLiteConstraintException e) {
            db.close();
            return false;
        }
        db.close();
        return insert != -1;
    }

    public boolean deleteStudySession(StudyModel studyModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String[] values = new String[] {studyModel.getDate(), studyModel.getStartTime()};

        long insert = db.delete(STUDY_TABLE, STUDY_DATE+" = ? AND "+STUDY_START_TIME+" = ?", values);
        db.close();
        return insert != -1;
    }

    public boolean loadSamples(Context context){
        ArrayList<String> queries = new ArrayList<>();
        boolean result = false;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.sample_data)))){
            String line;
            while ((line = reader.readLine()) != null){
                queries.add(line);
            }
        } catch (IOException e) {
            Log.e("DataBaseHelper", e.toString());
        }

        if (!queries.isEmpty()){
            SQLiteDatabase db = this.getWritableDatabase();
            for (String query: queries){
                db.execSQL(query);
            }
            result = true;
        }

        return result;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    public boolean addStepsEntry(StepsModel stepsModel){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(STEPS_DATE, stepsModel.getDate());
        cv.put(STEPS_TIME, stepsModel.getTime());
        cv.put(STEPS_NUM, stepsModel.getSteps());

        long insert = sqLiteDatabase.insert(STEPS_TABLE, null, cv);
        if (insert == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean addSleepEntry(SleepModel sleepModel){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(SLEEP_DATE, sleepModel.getDate());
        cv.put(SLEEP_START_TIME, sleepModel.getStartTime());
        cv.put(SLEEP_END_TIME, sleepModel.getEndTime());

        long insert = sqLiteDatabase.insert(SLEEP_TABLE, null, cv);
        if (insert == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean addExerciseEntry(ExerciseModel exerciseModel){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(EXERCISE_DATE, exerciseModel.getDate());
        cv.put(EXERCISE_START_TIME, exerciseModel.getStartTime());
        cv.put(EXERCISE_END_TIME, exerciseModel.getEndTime());

        long insert = sqLiteDatabase.insert(EXERCISE_TABLE, null, cv);
        if (insert == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public List<StepsModel> getAllSteps(){

        List<StepsModel> returnList = new ArrayList<>();

        String query = "SELECT * FROM " + STEPS_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                int entryID = cursor.getInt(0);
                String entryDate = cursor.getString(1);
                String entryTime = cursor.getString(2);
                int entrySteps = cursor.getInt(3);

                StepsModel stepsModel = new StepsModel(entryID, entryDate, entryTime, entrySteps);
                returnList.add(stepsModel);

            } while (cursor.moveToNext());
        }
        else{


        }

        cursor.close();
        sqLiteDatabase.close();
        return returnList;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getStepsToday(){
        int total = 0;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String dateToday = dtf.format(now);

        String query = "SELECT * FROM " + STEPS_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                if(cursor.getString(1).equals(dateToday)){
                    int entrySteps = cursor.getInt(3);
                    total += entrySteps;
                }


            } while (cursor.moveToNext());
        }
        else{


        }

        cursor.close();
        sqLiteDatabase.close();
        return total;
    }

    // returns list with singular UserModel object if user exists, empty list otherwise
    public List<UserModel> getUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<UserModel> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_ID + " = 1";
        Cursor c = db.rawQuery(queryString, null);

        if (c.moveToFirst()) {
            String firstName = c.getString(c.getColumnIndexOrThrow(COLUMN_FIRST_NAME));
            String lastName = c.getString(c.getColumnIndexOrThrow(COLUMN_EMAIL));
            String dateJoined = c.getString(c.getColumnIndexOrThrow(COLUMN_DATE_JOINED));
            String dateOfBirth = c.getString(c.getColumnIndexOrThrow(COLUMN_DATE_OF_BIRTH));
            String profilePicPath = c.getString(c.getColumnIndexOrThrow(COLUMN_PROFILE_PICTURE));
            double weight = c.getDouble(c.getColumnIndexOrThrow(COLUMN_WEIGHT));
            int height = c.getInt(c.getColumnIndexOrThrow(COLUMN_HEIGHT));
            int stepsGoal = c.getInt(c.getColumnIndexOrThrow(COLUMN_STEPS_GOAL));
            int exerciseGoal = c.getInt(c.getColumnIndexOrThrow(COLUMN_EXERCISE_MINUTES_GOAL));
            double studyGoal = c.getDouble(c.getColumnIndexOrThrow(COLUMN_STUDY_HOURS_GOAL));
            double sleepGoal = c.getDouble(c.getColumnIndexOrThrow(COLUMN_SLEEP_HOURS_GOAL));

            UserModel userModel = new UserModel(firstName, lastName, dateJoined, dateOfBirth, profilePicPath, weight, height, stepsGoal, exerciseGoal, studyGoal, sleepGoal);
            returnList.add(userModel);
        }
        c.close();
        db.close();
        return returnList;
    }

    public List<SleepModel> getAllSleep(){

        List<SleepModel> returnList = new ArrayList<>();

        String query = "SELECT * FROM " + SLEEP_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                int entryID = cursor.getInt(0);
                String entryDate = cursor.getString(1);
                String startTime = cursor.getString(2);
                String endTime = cursor.getString(3);

                SleepModel sleepModel = new SleepModel(entryID, entryDate, startTime, endTime);
                returnList.add(sleepModel);

            } while (cursor.moveToNext());
        }
        else{


        }

        cursor.close();
        sqLiteDatabase.close();
        return returnList;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getSleepToday(){
        String time = "";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String dateToday = dtf.format(now);

        String query = "SELECT * FROM " + SLEEP_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                if(cursor.getString(1).equals(dateToday)){
                    int entryId = cursor.getInt(0);
                    String entryDate = cursor.getString(1);
                    String startTime = cursor.getString(2);
                    String endTime = cursor.getString(3);

                    SleepModel sleepModel = new SleepModel(entryId, entryDate, startTime, endTime);

                    double doubleNumber = sleepModel.getSleepTime();
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(doubleNumber));
                    int intValue = bigDecimal.intValue();

                    time = String.format("%dh%02dmin", intValue, (int) Double.parseDouble(bigDecimal.subtract(
                            new BigDecimal(intValue)).toPlainString()) * 60);
                }

            } while (cursor.moveToNext());
            if (time.equals("")){
                time = "0h00min";
            }
        }
        else{
            time = "0h00min";
        }

        cursor.close();
        sqLiteDatabase.close();
        return time;
    }

    public List<ExerciseModel> getAllExercise(){

        List<ExerciseModel> returnList = new ArrayList<>();

        String query = "SELECT * FROM " + EXERCISE_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                int entryID = cursor.getInt(0);
                String entryDate = cursor.getString(1);
                String startTime = cursor.getString(2);
                String endTime = cursor.getString(3);

                ExerciseModel exerciseModel = new ExerciseModel(entryID, entryDate, startTime, endTime);
                returnList.add(exerciseModel);

            } while (cursor.moveToNext());
        }
        else{


        }

        cursor.close();
        sqLiteDatabase.close();
        return returnList;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getExerciseToday(){
        String time = "";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String dateToday = dtf.format(now);

        String query = "SELECT * FROM " + EXERCISE_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                if(cursor.getString(1).equals(dateToday)){
                    int entryId = cursor.getInt(0);
                    String entryDate = cursor.getString(1);
                    String startTime = cursor.getString(2);
                    String endTime = cursor.getString(3);

                    ExerciseModel exerciseModel = new ExerciseModel(entryId, entryDate, startTime, endTime);

                    double doubleNumber = exerciseModel.getExerciseTime();
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(doubleNumber));
                    int intValue = bigDecimal.intValue();
                    time = String.format("%dh%02dmin", intValue, (int) Double.parseDouble(bigDecimal.subtract(
                            new BigDecimal(intValue)).toPlainString()) * 60);
                }


            } while (cursor.moveToNext());
            if (time.equals("")){
                time = "0h00min";
            }
        }
        else{
            time = "0h00min";
        }

        cursor.close();
        sqLiteDatabase.close();
        return time;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getStudyToday(){
        String time = "";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        String dateToday = dtf.format(now);

        String query = "SELECT * FROM " + STUDY_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                if(cursor.getString(1).equals(dateToday)){
                    int entryId = cursor.getInt(0);
                    String entryDate = cursor.getString(1);
                    String startTime = cursor.getString(2);
                    String endTime = cursor.getString(3);

                    StudyModel studyModel = new StudyModel(entryId, entryDate, startTime, endTime);

                    double doubleNumber = studyModel.getStudyTime();
                    BigDecimal bigDecimal = new BigDecimal(String.valueOf(doubleNumber));
                    int intValue = bigDecimal.intValue();
                    time = String.format("%dh%02dmin", intValue, (int) Double.parseDouble(bigDecimal.subtract(
                            new BigDecimal(intValue)).toPlainString()) * 60);
                }


            } while (cursor.moveToNext());
            if (time.equals("")){
                time = "0h00min";
            }
        }
        else{
            time = "0h00min";
        }

        cursor.close();
        sqLiteDatabase.close();
        return time;
    }

    public List<StudyModel> getAllStudy(){
        List<StudyModel> returnList = new ArrayList<>();

        String query = "SELECT * FROM " + STUDY_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                int entryID = cursor.getInt(0);
                String entryDate = cursor.getString(1);
                String startTime = cursor.getString(2);
                String endTime = cursor.getString(3);

                StudyModel studyModel = new StudyModel(entryID, entryDate, startTime, endTime);
                returnList.add(studyModel);

            } while (cursor.moveToNext());
        }
        else{


        }

        cursor.close();
        sqLiteDatabase.close();
        return returnList;
    }

    public boolean isUserEmpty(){

        String query = "SELECT * FROM " + USER_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        return !cursor.moveToFirst();
    }

    // inserts user into database if user table is empty
    public boolean addUser(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ID, 1);
        cv.put(COLUMN_FIRST_NAME, userModel.getFirstName());
        cv.put(COLUMN_EMAIL, userModel.getEmail());
        cv.put(COLUMN_DATE_JOINED, userModel.getDateJoined());
        cv.put(COLUMN_DATE_OF_BIRTH, userModel.getDateOfBirth());
        cv.put(COLUMN_PROFILE_PICTURE, userModel.getProfilePicture());
        cv.put(COLUMN_WEIGHT, userModel.getWeight());
        cv.put(COLUMN_HEIGHT, userModel.getHeight());
        cv.put(COLUMN_STEPS_GOAL, userModel.getStepsGoal());
        cv.put(COLUMN_EXERCISE_MINUTES_GOAL, userModel.getExerciseGoal());
        cv.put(COLUMN_STUDY_HOURS_GOAL, userModel.getStudyHoursGoal());
        cv.put(COLUMN_SLEEP_HOURS_GOAL, userModel.getSleepHoursGoal());

        long insert;
        try {
            insert = db.insert(USER_TABLE, null, cv);
        } catch (SQLiteConstraintException e) {
            db.close();
            return false;
        }
        db.close();
        return insert != -1;
    }

    // updates user in database
    public boolean updateUser(UserModel userModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ID, 1);
        cv.put(COLUMN_FIRST_NAME, userModel.getFirstName());
        cv.put(COLUMN_EMAIL, userModel.getEmail());
        cv.put(COLUMN_DATE_JOINED, userModel.getDateJoined());
        cv.put(COLUMN_DATE_OF_BIRTH, userModel.getDateOfBirth());
        cv.put(COLUMN_PROFILE_PICTURE, userModel.getProfilePicture());
        cv.put(COLUMN_WEIGHT, userModel.getWeight());
        cv.put(COLUMN_HEIGHT, userModel.getHeight());
        cv.put(COLUMN_STEPS_GOAL, userModel.getStepsGoal());
        cv.put(COLUMN_EXERCISE_MINUTES_GOAL, userModel.getExerciseGoal());
        cv.put(COLUMN_STUDY_HOURS_GOAL, userModel.getStudyHoursGoal());
        cv.put(COLUMN_SLEEP_HOURS_GOAL, userModel.getSleepHoursGoal());

        long insert = db.update(USER_TABLE, cv, COLUMN_ID + " = ?", new String[] {"1"});
        db.close();
        return insert != -1;
    }

    public boolean updateUserName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FIRST_NAME, name);

        long insert = db.update(USER_TABLE, cv, COLUMN_ID + " = ?", new String[] {"1"});
        db.close();
        return insert != -1;
    }

    public String getUserName(){
        String query = "SELECT * FROM " + USER_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        String name = null;

        if(cursor.moveToFirst()){
            name = cursor.getString(1);
        }
        else{
            return "Name";
        }

        cursor.close();
        sqLiteDatabase.close();
        return name;
    }

    public boolean updateUserEmail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EMAIL, email);

        long insert = db.update(USER_TABLE, cv, COLUMN_ID + " = ?", new String[] {"1"});
        db.close();
        return insert != -1;
    }

    public String getUserEmail(){
        String query = "SELECT * FROM " + USER_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        String email = null;

        if(cursor.moveToFirst()){
            email = cursor.getString(2);
        }
        else{
            return "Email Address";
        }

        cursor.close();
        sqLiteDatabase.close();
        return email;
    }

    public boolean updateUserDateOfBirth(String dob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_DATE_OF_BIRTH, dob);

        long insert = db.update(USER_TABLE, cv, COLUMN_ID + " = ?", new String[] {"1"});
        db.close();
        return insert != -1;
    }

    public String getUserDateOfBirth(){
        String query = "SELECT * FROM " + USER_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        String dob = null;

        if(cursor.moveToFirst()){
            dob = cursor.getString(4);
        }
        else{
            return "Date of Birth";
        }

        cursor.close();
        sqLiteDatabase.close();
        return dob;
    }

    public boolean updateUserHeight(Double height) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_HEIGHT, height);

        long insert = db.update(USER_TABLE, cv, COLUMN_ID + " = ?", new String[] {"1"});
        db.close();
        return insert != -1;
    }

    public Double getUserHeight(){
        String query = "SELECT * FROM " + USER_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        Double height = null;

        if(cursor.moveToFirst()){
            height = cursor.getDouble(7);
        }
        else{
            return 0.00;
        }

        cursor.close();
        sqLiteDatabase.close();
        return height;
    }

    public boolean updateUserWeight(Double weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_WEIGHT, weight);

        long insert = db.update(USER_TABLE, cv, COLUMN_ID + " = ?", new String[] {"1"});
        db.close();
        return insert != -1;
    }

    public Double getUserWeight(){
        String query = "SELECT * FROM " + USER_TABLE;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        Double weight = null;

        if(cursor.moveToFirst()){
            weight = cursor.getDouble(6);
        }
        else{
            return 0.00;
        }

        cursor.close();
        sqLiteDatabase.close();
        return weight;
    }

    public boolean updateUserStepsGoal(int stepsNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_STEPS_GOAL, stepsNum);

        long insert = db.update(USER_TABLE, cv, COLUMN_ID + " = ?", new String[] {"1"});
        db.close();
        return insert != -1;
    }

    public boolean updateUserExerciseGoal(int exHours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EXERCISE_MINUTES_GOAL, exHours);

        long insert = db.update(USER_TABLE, cv, COLUMN_ID + " = ?", new String[] {"1"});
        db.close();
        return insert != -1;
    }

    public boolean updateUserStudyGoal(int studHours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_STUDY_HOURS_GOAL, studHours);

        long insert = db.update(USER_TABLE, cv, COLUMN_ID + " = ?", new String[] {"1"});
        db.close();
        return insert != -1;
    }

    public boolean updateUserSleepGoal(int sleepHours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_SLEEP_HOURS_GOAL, sleepHours);

        long insert = db.update(USER_TABLE, cv, COLUMN_ID + " = ?", new String[] {"1"});
        db.close();
        return insert != -1;
    }
}
