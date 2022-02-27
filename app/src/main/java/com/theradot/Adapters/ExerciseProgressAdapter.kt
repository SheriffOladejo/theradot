package com.theradot.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.theradot.Models.Exercise
import com.theradot.Models.ExerciseProgress
import com.theradot.R
import kotlinx.android.synthetic.main.item_exercise.view.*
import kotlinx.android.synthetic.main.item_progress.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ExerciseProgressAdapter(val context: Context, val list: ArrayList<ExerciseProgress>) : RecyclerView.Adapter<ExerciseProgressAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseProgressAdapter.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_progress, parent, false)
        return ExerciseProgressAdapter.ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ExerciseProgressAdapter.ViewHolder, position: Int) {
        holder.setData(list.get(position), context)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(e: ExerciseProgress, c: Context){
            try{
                itemView.time.setText(SimpleDateFormat("hh:mm a").format(Date(e.time_performed.toLong())))
            }
            catch(e: Exception){

            }

            itemView.title.setText(e.title)
            val number_of_reps = e.number_of_reps.toDouble()
            val reps_performed = e.completed_reps.toDouble()
            itemView.num_completed.setText("${reps_performed.toInt()} out of ${number_of_reps.toInt()} completed")
            val progress = reps_performed/number_of_reps * 100
            itemView.progress.progress = progress.toInt()
        }
    }
}