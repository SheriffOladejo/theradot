package com.theradot.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.theradot.Activities.ExerciseDetailsActivity
import com.theradot.Models.Exercise
import com.theradot.R
import kotlinx.android.synthetic.main.item_exercise.view.*

class ExerciseAdapter(val context: Context, val list: ArrayList<Exercise>) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list.get(position), context)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(e: Exercise, c: Context){

            itemView.setOnClickListener(object: View.OnClickListener{
                override fun onClick(p0: View?) {
                    val intent = Intent(c, ExerciseDetailsActivity::class.java)
                    intent.putExtra("exercise_category", e.exercise_title)
                    intent.putExtra("video_url", e.video_url)
                    intent.putExtra("number_of_reps", e.number_of_reps)
                    intent.putExtra("reps_completed", e.completed_reps)
                    intent.putExtra("description", e.exercise_description)
                    intent.putExtra("objectives", e.objectives)
                    intent.putExtra("exercise_id", e.exercise_id)
                    c.startActivity(intent)
                }
            })

            Picasso.with(c).load(e.image_thumbnail).into(itemView.thumbnail)
            itemView.exercise_category.setText(e.exercise_category)
            itemView.exercise_title.setText(e.exercise_title)
            itemView.exercise_description.setText(e.exercise_description)
        }
    }

}