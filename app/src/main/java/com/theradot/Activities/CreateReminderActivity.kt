package com.theradot.Activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.theradot.Models.Alarm
import com.theradot.R
import com.theradot.Utilities.DbHelper
import com.theradot.Utilities.UtilityMethods
import kotlinx.android.synthetic.main.activity_create_reminder.*
import java.util.*


class CreateReminderActivity : AppCompatActivity() {

    private var alarm_id = Random().nextInt(10000).toString()
    private var days = BooleanArray(7)
    private lateinit var hour: String
    private lateinit var minute: String
    private lateinit var am_pm: String
    private var _days = ""
    private var util = UtilityMethods(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_reminder)

        hour = "-1"
        minute = "-1"

        back.setOnClickListener{
            finish()
        }

        set_reminder.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                var h = DbHelper(this@CreateReminderActivity)

                var is_empty = true
                for(i in days){
                    if(i){
                        is_empty = false
                        break
                    }
                }
                if(is_empty) {
                    util.toast("Select a day")
                    return
                }

                if(hour == "-1" || minute == "-1"){
                    util.toast("Select a future time")
                    return
                }

                for((i, data) in days.withIndex()){
                    if(data) {
                        when(i){
                            0 -> {_days = "${_days}Sun "}
                            1 -> {_days = "${_days}Mon "}
                            2 -> {_days = "${_days}Tue "}
                            3 -> {_days = "${_days}Wed "}
                            4 -> {_days = "${_days}Thur "}
                            5 -> {_days = "${_days}Fri "}
                            6 -> {_days = "${_days}Sat "}
                            else->{
                                util.toast("Select a day")
                                return
                            }
                        }

                        var day_of_week = i + 1
                        var id = System.currentTimeMillis().toInt()

                        var intent = Intent(this@CreateReminderActivity, MainActivity::class.java)

                        var calender = Calendar.getInstance()
                        calender.set(Calendar.HOUR_OF_DAY, hour.toInt())
                        calender.set(Calendar.MINUTE, minute.toInt())
                        calender.set(Calendar.SECOND, 0)
                        calender.set(Calendar.MILLISECOND, 0)
                        calender.set(Calendar.DAY_OF_WEEK, day_of_week)

                        h.startAlarm(calender, id.toString(), alarm_id)
                    }
                }
                var alarm = Alarm(
                    hour = hour,
                    minute = minute,
                    days = _days,
                    is_active = "true",
                    id = alarm_id,
                    am_pm = am_pm
                )
                h.addAlarm(alarm)
                util.toast("Alarm has been set")
                finish()
            }
        })

        time_picker.setOnTimeChangedListener(object: TimePicker.OnTimeChangedListener{
            override fun onTimeChanged(p0: TimePicker?, p1: Int, p2: Int) {
                hour = p1.toString()
                minute = p2.toString()

                if(hour.toInt() < 12)
                    am_pm = "AM"
                else
                    am_pm = "PM"
            }
        })

        sunday.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                if(days[0]){
                    days[0] = false
                    sunday.setTextColor(resources.getColor(R.color.text_color))
                }
                else{
                    days[0] = true
                    sunday.setTextColor(resources.getColor(R.color.button_green))
                }
            }
        })

        monday.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                if(days[1]){
                    days[1] = false
                    monday.setTextColor(resources.getColor(R.color.text_color))
                }
                else{
                    days[1] = true
                    monday.setTextColor(resources.getColor(R.color.button_green))
                }
            }
        })

        tuesday.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                if(days[2]){
                    days[2] = false
                    tuesday.setTextColor(resources.getColor(R.color.text_color))
                }
                else{
                    days[2] = true
                    tuesday.setTextColor(resources.getColor(R.color.button_green))
                }
            }
        })

        wednesday.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                if(days[3]){
                    days[3] = false
                    wednesday.setTextColor(resources.getColor(R.color.text_color))
                }
                else{
                    days[3] = true
                    wednesday.setTextColor(resources.getColor(R.color.button_green))
                }
            }
        })

        thursday.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                if(days[4]){
                    days[4] = false
                    thursday.setTextColor(resources.getColor(R.color.text_color))
                }
                else{
                    days[4] = true
                    thursday.setTextColor(resources.getColor(R.color.button_green))
                }
            }
        })

        friday.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                if(days[5]){
                    days[5] = false
                    friday.setTextColor(resources.getColor(R.color.text_color))
                }
                else{
                    days[5] = true
                    friday.setTextColor(resources.getColor(R.color.button_green))
                }
            }
        })

        saturday.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                if(days[6]){
                    days[6] = false
                    saturday.setTextColor(resources.getColor(R.color.text_color))
                }
                else{
                    days[6] = true
                    saturday.setTextColor(resources.getColor(R.color.button_green))
                }
            }
        })
    }
}