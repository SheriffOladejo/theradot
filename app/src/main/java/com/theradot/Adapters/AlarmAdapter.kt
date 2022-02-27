package com.theradot.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.theradot.Models.Alarm
import com.theradot.R
import com.theradot.Utilities.DbHelper
import com.theradot.Utilities.NotificationHelper
import kotlinx.android.synthetic.main.item_reminder.view.*
import java.text.SimpleDateFormat
import java.util.*

class AlarmAdapter(val context: Context,
                   val list: ArrayList<Alarm>,
                   val select_all: Boolean,
                   val show_checkbox: Boolean,
                   val relative_layout: RelativeLayout,
                   val recycler_view: RecyclerView,
                   val number_selected: TextView,
                   val to_delete: ArrayList<Alarm>,
                   val select_all_: CheckBox,
                   val delete_reminder: ImageView,
                   val create_reminder: FloatingActionButton) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_reminder, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(context, list.get(position), list, select_all, show_checkbox, relative_layout, recycler_view, number_selected, to_delete, select_all_, delete_reminder, create_reminder)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(c: Context, a: Alarm, l_: ArrayList<Alarm>, s: Boolean, _show_checkbox: Boolean, relative_layout:RelativeLayout, recycler_view: RecyclerView, number_selected: TextView, to_delete: ArrayList<Alarm>, select_all_: CheckBox, delete_reminder: ImageView, create_reminder: FloatingActionButton){

            val helper = DbHelper(c)

            itemView.setOnLongClickListener(object: View.OnLongClickListener{
                override fun onLongClick(p0: View?): Boolean {
                    var adapter = AlarmAdapter(
                        c,
                        l_, false, true, relative_layout,
                        recycler_view, number_selected, to_delete,
                        select_all_, delete_reminder, create_reminder)
                    recycler_view.adapter = adapter
                    relative_layout.visibility = View.VISIBLE
                    delete_reminder.visibility = View.VISIBLE
                    create_reminder.visibility = View.GONE
                    return true
                }
            })

            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, a.hour.toInt())
            cal.set(Calendar.MINUTE, a.minute.toInt())

            itemView.time.setText(SimpleDateFormat("hh:mm a").format(cal.time.time))
            var days = a.days.split(" ")
            println("days: ${a.days}")
            if(days.size == 7)
                itemView.days.setText("Daily")
            else{
                itemView.days.setText("${a.days}")
            }

            itemView.select_for_deletion.setOnCheckedChangeListener{buttonView, p1 ->
                if(p1){
                    if(!to_delete.contains(l_.get(absoluteAdapterPosition))){
                        to_delete.add(l_.get(absoluteAdapterPosition))
                        number_selected.setText("${to_delete.size} items selected")
                    }
                }
                else{
                    if(to_delete.contains(l_.get(absoluteAdapterPosition))){
                        to_delete.remove(l_.get(absoluteAdapterPosition))
                        number_selected.setText("${to_delete.size} items selected")
                    }
                }
            }

            itemView.is_active.setOnCheckedChangeListener(object: CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    if(p1){
                        helper.disableAlarm(l_.get(absoluteAdapterPosition).id, "true")
                        //setAlarm(id = l_.get(absoluteAdapterPosition).id, c = c)
                    }
                    else{
                        helper.disableAlarm(l_.get(absoluteAdapterPosition).id, "false")
                    }
                }
            })

            itemView.is_active.isChecked = a.is_active == "true"
            itemView.select_for_deletion.isChecked = s

            if(_show_checkbox){
                itemView.is_active.visibility = View.GONE
                itemView.select_for_deletion.visibility = View.VISIBLE
            }
            else{
                itemView.is_active.visibility = View.VISIBLE
                itemView.select_for_deletion.visibility = View.GONE
            }
        }

        fun setAlarm(id: String, c: Context){
            val helper = DbHelper(c)
            val alarm = helper.getAlarm(id)
            val days = alarm.days.split(" ").toTypedArray()
            val today = SimpleDateFormat("EEE").format(Date())
            var _day_of_week = ""
            val day_of_week: Int

            for (i in days.indices) {
                if (days[i].toLowerCase().contains(today.toLowerCase())) {
                    _day_of_week = days[i]
                }
            }

            day_of_week = when (_day_of_week.toLowerCase()) {
                "sun" -> 1
                "mon" -> 2
                "tue" -> 3
                "wed" -> 4
                "thur" -> 5
                "fri" -> 6
                "sat" -> 7
                else -> -1
            }

            if (day_of_week != -1) {
                val calender = Calendar.getInstance()
                calender[Calendar.HOUR_OF_DAY] = Integer.valueOf(alarm.hour)
                calender[Calendar.MINUTE] = Integer.valueOf(alarm.minute)
                calender[Calendar.SECOND] = 0
                calender[Calendar.MILLISECOND] = 0
                calender[Calendar.DAY_OF_WEEK] = day_of_week
                helper.startAlarm(calender, id, alarm.id)

                println("AlarmAdapter.setAlarm: alarm has been set again")
            } else {
                println("AlarmAdapter.setAlarm: Day of week not set and is -1")
            }
        }
    }
}