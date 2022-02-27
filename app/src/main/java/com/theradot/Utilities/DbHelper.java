package com.theradot.Utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.theradot.Models.Alarm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;

public class DbHelper extends SQLiteOpenHelper {

    //Alarm table columns
    private static final String ALARM_TABLE = "ALARM_TABLE";
    private static final String ALARM_TABLE_1 = "HOUR";
    private static final String ALARM_TABLE_2 = "MINUTE";
    private static final String ALARM_TABLE_3 = "DAYS";
    private static final String ALARM_TABLE_4 = "IS_ACTIVE";
    private static final String ALARM_TABLE_5 = "ID";
    private static final String ALARM_TABLE_6 = "AM_PM";

    //User table columns
    private static final String USER_TABLE = "USER_TABLE";
    private static final String USER_TABLE_1 = "CARD_NUMBER";
    private static final String USER_TABLE_2 = "CREATION_DATE";
    private static final String USER_TABLE_3 = "DATE_OF_BIRTH";
    private static final String USER_TABLE_4 = "EYE_COLOR";
    private static final String USER_TABLE_5 = "FIREBASE_TOKEN";
    private static final String USER_TABLE_6 = "FIRSTNAME";
    private static final String USER_TABLE_7 = "GENDER";
    private static final String USER_TABLE_8 = "HEIGHT";
    private static final String USER_TABLE_9 = "LASTNAME";
    private static final String USER_TABLE_10 = "MIDDLENAME";
    private static final String USER_TABLE_11 = "PHONE_NUMBER";
    private static final String USER_TABLE_12 = "PROFILE_IMAGE";

    private static final String DATABASE_NAME = "THERADOT";
    Context context;
    SQLiteDatabase db;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String create_user_table = "create table " + USER_TABLE + " (" +
                "CARD_NUMBER text, " +
                "CREATION_DATE text, " +
                "DATE_OF_BIRTH text, " +
                "EYE_COLOR text, " +
                "FIREBASE_TOKEN text, " +
                "FIRSTNAME text, " +
                "GENDER text, " +
                "HEIGHT text, " +
                "LASTNAME text, " +
                "MIDDLENAME text, " +
                "PHONE_NUMBER text, " +
                "PROFILE_IMAGE text)";

        String create_alarm_table = "create table " + ALARM_TABLE + " (" +
                "HOUR text, " +
                "MINUTE text, " +
                "DAYS text, " +
                "IS_ACTIVE text, " +
                "AM_PM text, " +
                "ID text)";



        sqLiteDatabase.execSQL(create_user_table);
        sqLiteDatabase.execSQL(create_alarm_table);
    }

    public void addUser(User user){
        ContentValues values = new ContentValues();
        values.put(USER_TABLE_1, user.getCard_number());
        values.put(USER_TABLE_2, user.getCreation_date());
        values.put(USER_TABLE_3, user.getDate_of_birth());
        values.put(USER_TABLE_4, user.getEye_color());
        values.put(USER_TABLE_5, user.getFirebase_token());
        values.put(USER_TABLE_6, user.getFirstname());
        values.put(USER_TABLE_7, user.getGender());
        values.put(USER_TABLE_8, user.getHeight());
        values.put(USER_TABLE_9, user.getLastname());
        values.put(USER_TABLE_10, user.getMiddlename());
        values.put(USER_TABLE_11, user.getPhone_number());
        values.put(USER_TABLE_12, user.getProfile_image());
        db.insert(USER_TABLE, null, values);
    }

    public User getUser(){
        User user = null;
        String query = "select * from " + USER_TABLE;
        Cursor data = db.rawQuery(query, null);
        if(data.getCount() != 0){
            while(data.moveToNext()){
                user = new User(
                        data.getString(data.getColumnIndex(USER_TABLE_1)),
                        data.getString(data.getColumnIndex(USER_TABLE_2)),
                        data.getString(data.getColumnIndex(USER_TABLE_3)),
                        data.getString(data.getColumnIndex(USER_TABLE_4)),
                        data.getString(data.getColumnIndex(USER_TABLE_5)),
                        data.getString(data.getColumnIndex(USER_TABLE_6)),
                        data.getString(data.getColumnIndex(USER_TABLE_7)),
                        data.getString(data.getColumnIndex(USER_TABLE_8)),
                        data.getString(data.getColumnIndex(USER_TABLE_9)),
                        data.getString(data.getColumnIndex(USER_TABLE_10)),
                        data.getString(data.getColumnIndex(USER_TABLE_11)),
                        data.getString(data.getColumnIndex(USER_TABLE_12))
                );
            }
        }
        return user;
    }

    public void deleteUser(){
        String query = "delete from " + USER_TABLE;
        db.execSQL(query);
    }

    public void addAlarm(Alarm a){
        ContentValues values = new ContentValues();
        values.put(ALARM_TABLE_1, a.getHour());
        values.put(ALARM_TABLE_2, a.getMinute());
        values.put(ALARM_TABLE_3, a.getDays());
        values.put(ALARM_TABLE_4, a.is_active());
        values.put(ALARM_TABLE_5, a.getId());
        values.put(ALARM_TABLE_6, a.getAm_pm());
        db.insert(ALARM_TABLE, null, values);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(a.getHour()));
        cal.set(Calendar.MINUTE, Integer.valueOf(a.getMinute()));
    }

    public Alarm getAlarm(String id){
        Alarm alarm = null;
        String query = "select * from " + ALARM_TABLE + " where " + ALARM_TABLE_5 + " ='" + id + "'";
        Cursor data = db.rawQuery(query, null);
        if(data.getCount() != 0){
            while(data.moveToNext()){
                alarm = new Alarm(
                    data.getString(data.getColumnIndex(ALARM_TABLE_1)),
                    data.getString(data.getColumnIndex(ALARM_TABLE_2)),
                    data.getString(data.getColumnIndex(ALARM_TABLE_3)),
                    data.getString(data.getColumnIndex(ALARM_TABLE_4)),
                    data.getString(data.getColumnIndex(ALARM_TABLE_5)),
                    data.getString(data.getColumnIndex(ALARM_TABLE_6))
                );
            }
        }
        return alarm;
    }

    public ArrayList<Alarm> getAlarms(){
        ArrayList<Alarm> alarms = new ArrayList<>();
        String query = "select * from "+ ALARM_TABLE;
        Cursor data = db.rawQuery(query, null);
        if(data.getCount() != 0){
            while(data.moveToNext()){
                alarms.add(new Alarm(
                        data.getString(data.getColumnIndex(ALARM_TABLE_1)),
                        data.getString(data.getColumnIndex(ALARM_TABLE_2)),
                        data.getString(data.getColumnIndex(ALARM_TABLE_3)),
                        data.getString(data.getColumnIndex(ALARM_TABLE_4)),
                        data.getString(data.getColumnIndex(ALARM_TABLE_5)),
                        data.getString(data.getColumnIndex(ALARM_TABLE_6))
                ));
            }
        }
        System.out.println("Alarm list: " + alarms.size());
        return alarms;
    }

    public void disableAlarm(String id, String active){
        String query = "update " + ALARM_TABLE + " set " + ALARM_TABLE_4 + " ='" + active + "' where ID='" + id + "'";
        db.execSQL(query);
    }

    public void deleteAlarm(String id){
        String query = "delete from " + ALARM_TABLE + " where ID='" + id + "'";
        db.execSQL(query);
        cancelAlarm(id);
    }

    public void cancelAlarm(String id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.valueOf(id), intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    public void startAlarm(Calendar c, String id, String alarm_id) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra("id", id);
        intent.putExtra("alarm_id", alarm_id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.valueOf(id), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Long alarm_time = c.getTimeInMillis();
        if (c.before(Calendar.getInstance())) {
            //c.add(Calendar.DATE, 1);
            alarm_time += AlarmManager.INTERVAL_DAY * 7;
        }

        Long alarmTimeAtUTC = System.currentTimeMillis() + alarm_time;

// Depending on the version of Android use different function for setting an
// Alarm.
// setAlarmClock() - used for everything lower than Android M
// setExactAndAllowWhileIdle() - used for everything on Android M and higher
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            alarmManager.setAlarmClock(
                    new AlarmManager.AlarmClockInfo(alarm_time, pendingIntent),
                    pendingIntent
            );
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarm_time,
                    pendingIntent
            );
        }

        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm_time, AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        //alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm_time, pendingIntent);
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm_time, pendingIntent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
