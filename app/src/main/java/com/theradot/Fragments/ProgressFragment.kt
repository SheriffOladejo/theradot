package com.theradot.Fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.theradot.Adapters.ExerciseProgressAdapter
import com.theradot.Models.ExerciseProgress
import com.theradot.R
import com.theradot.Utilities.Prevalent
import com.theradot.Utilities.UtilityMethods
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_progress.*
import kotlinx.android.synthetic.main.fragment_progress.view.*
import java.lang.Exception

class ProgressFragment(val c: Context) : Fragment() {

    private lateinit var view_: View
    private var util = UtilityMethods(context = c)
    private lateinit var adapter: ExerciseProgressAdapter
    private lateinit var list: ArrayList<ExerciseProgress>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Paper.init(context)
        view_ =  inflater.inflate(R.layout.fragment_progress, container, false)
        view_.recyclerview.layoutManager = LinearLayoutManager(c)
        view_.refresh.setOnRefreshListener { getActivities() }
        //view_.refresh.isRefreshing = true
        getActivities()
        return view_
    }

    fun getActivities(){
        list = ArrayList<ExerciseProgress>()
        view_.refresh.isRefreshing = true
        val card_number = Paper.book().read(Prevalent.CARD_NUMBER, "")
        println("this")
        val ref = FirebaseDatabase.getInstance().reference.child("Users").child(card_number).child("Activity")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                view_.refresh.isRefreshing = false
                util.toast("Could not fetch data, try again")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(data in snapshot.children){
                        val title = data.child("title").value.toString()
                        val number_of_reps = data.child("number_of_reps").value.toString()
                        val reps_completed = data.child("completed_reps").value.toString()
                        val time_performed = data.child("time_performed").value.toString()
                        list.add(
                            ExerciseProgress(
                            title = title,
                            number_of_reps = number_of_reps,
                            completed_reps = reps_completed,
                            time_performed = time_performed
                        ))
                    }
                    if(list.size == 0){
                        try{
                            no_exercise.visibility = View.VISIBLE
                        }
                        catch(e: Exception){}

                    }
                    else{
                        try{
                            no_exercise.visibility = View.GONE
                        }
                        catch(e: Exception){}
                    }
                    adapter = ExerciseProgressAdapter(context = c, list= list)
                    view_.recyclerview.adapter = adapter
                    view_.refresh.isRefreshing = false
                }
                else{
                    view_.refresh.isRefreshing = false
                    util.toast("No exercises found")
                }
            }
        })
    }
}