package com.theradot.Utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.theradot.Activities.MainActivity;
import com.theradot.Activities.ReminderActivity;
import com.theradot.Models.Alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        DbHelper helper = new DbHelper(context);

        String id = intent.getStringExtra("id");
        String alarm_id = intent.getStringExtra("alarm_id");

        Alarm alarm = helper.getAlarm(alarm_id);
        if(alarm.is_active().equals("true")){
            NotificationHelper notificationHelper = new NotificationHelper(context);
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
            notificationHelper.getManager().notify(Integer.valueOf(id), nb.build());

            String[] days = alarm.getDays().split(" ");
            String today = new SimpleDateFormat("EEE").format(new Date());
            String _day_of_week = "";
            int day_of_week;

            for(int i=0; i<days.length; i++){
                if(days[i].toLowerCase().contains(today.toLowerCase())){
                    _day_of_week = days[i];
                }
            }

            switch (_day_of_week.toLowerCase()){
                case "sun":
                    day_of_week = 1;
                    break;
                case "mon":
                    day_of_week = 2;
                    break;
                case "tue":
                    day_of_week = 3;
                    break;
                case "wed":
                    day_of_week = 4;
                    break;
                case "thur":
                    day_of_week = 5;
                    break;
                case "fri":
                    day_of_week = 6;
                    break;
                case "sat":
                    day_of_week = 7;
                    break;
                default:
                    day_of_week = -1;
            }

            if(day_of_week != -1){
                Calendar calender = Calendar.getInstance();
                calender.set(Calendar.HOUR_OF_DAY, Integer.valueOf(alarm.getHour()));
                calender.set(Calendar.MINUTE, Integer.valueOf(alarm.getMinute()));
                calender.set(Calendar.SECOND, 0);
                calender.set(Calendar.MILLISECOND, 0);
                calender.set(Calendar.DAY_OF_WEEK, day_of_week);

                helper.startAlarm(calender, id, alarm_id);
                System.out.println("AlertReceiver.onReceive: alarm has been set again");
            }
            else{
                System.out.println("AlertReceiver.onReceive: Day of week not set and is -1");
            }

            Intent i = new Intent(context, ReminderActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
