package com.theradot.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.theradot.R
import kotlinx.android.synthetic.main.activity_create_reminder.*

class CreateReminderFragment : Fragment() {

    private var is_sunday_selected: Boolean = false
    private var is_monday_selected: Boolean = false
    private var is_tuesday_selected: Boolean = false
    private var is_wednesday_selected: Boolean = false
    private var is_thursday_selected: Boolean = false
    private var is_friday_selected: Boolean = false
    private var is_saturday_selected: Boolean = false

    private var alarm_hour: Int = -1
    private var alarm_minute: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sunday.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?){
                if(is_sunday_selected){
                    sunday.setTextColor(resources.getColor(R.color.text_color))
                    sunday.background.setTint(resources.getColor(R.color.white))
                    is_sunday_selected = false
                }
                else{
                    sunday.setTextColor(resources.getColor(R.color.white))
                    sunday.background.setTint(resources.getColor(R.color.button_green))
                    is_sunday_selected = true
                }

                if(is_monday_selected){
                    monday.setTextColor(resources.getColor(R.color.text_color))
                    monday.background.setTint(resources.getColor(R.color.white))
                    is_monday_selected = false
                }
                else{
                    monday.setTextColor(resources.getColor(R.color.white))
                    monday.background.setTint(resources.getColor(R.color.button_green))
                    is_monday_selected = true
                }

                if(is_tuesday_selected){
                    tuesday.setTextColor(resources.getColor(R.color.text_color))
                    tuesday.background.setTint(resources.getColor(R.color.white))
                    is_tuesday_selected = false
                }
                else{
                    tuesday.setTextColor(resources.getColor(R.color.white))
                    tuesday.background.setTint(resources.getColor(R.color.button_green))
                    is_tuesday_selected = true
                }

                if(is_wednesday_selected){
                    wednesday.setTextColor(resources.getColor(R.color.text_color))
                    wednesday.background.setTint(resources.getColor(R.color.white))
                    is_wednesday_selected = false
                }
                else{
                    wednesday.setTextColor(resources.getColor(R.color.white))
                    wednesday.background.setTint(resources.getColor(R.color.button_green))
                    is_wednesday_selected = true
                }

                if(is_thursday_selected){
                    thursday.setTextColor(resources.getColor(R.color.text_color))
                    thursday.background.setTint(resources.getColor(R.color.white))
                    is_thursday_selected = false
                }
                else{
                    thursday.setTextColor(resources.getColor(R.color.white))
                    thursday.background.setTint(resources.getColor(R.color.button_green))
                    is_thursday_selected = true
                }

                if(is_friday_selected){
                    friday.setTextColor(resources.getColor(R.color.text_color))
                    friday.background.setTint(resources.getColor(R.color.white))
                    is_friday_selected = false
                }
                else{
                    friday.setTextColor(resources.getColor(R.color.white))
                    friday.background.setTint(resources.getColor(R.color.button_green))
                    is_friday_selected = true
                }

                if(is_saturday_selected){
                    saturday.setTextColor(resources.getColor(R.color.text_color))
                    saturday.background.setTint(resources.getColor(R.color.white))
                    is_saturday_selected = false
                }
                else{
                    saturday.setTextColor(resources.getColor(R.color.white))
                    saturday.background.setTint(resources.getColor(R.color.button_green))
                    is_saturday_selected = true
                }
            }
        })
        set_reminder.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                val days = arrayListOf<String>()
                if(is_sunday_selected){
                    days.add("Sun")
                }
                else{
                    days.remove("Sun")
                }

                if(is_monday_selected){
                    days.add("Mon")
                }
                else{
                    days.remove("Mon")
                }

                if(is_tuesday_selected){
                    days.add("Tue")
                }
                else{
                    days.remove("Tue")
                }

                if(is_wednesday_selected){
                    days.add("Wed")
                }
                else{
                    days.remove("Wed")
                }

                if(is_thursday_selected){
                    days.add("Thur")
                }
                else{
                    days.remove("Thur")
                }

                if(is_friday_selected){
                    days.add("Fri")
                }
                else{
                    days.remove("Fri")
                }

                if(is_saturday_selected){
                    days.add("Sat")
                }
                else{
                    days.remove("Sat")
                }

                alarm_hour = time_picker.hour
                alarm_minute = time_picker.minute

                //SimpleAlarmManager(context).setup()
            }
        })
        return inflater.inflate(R.layout.activity_create_reminder, container, false)
    }
}