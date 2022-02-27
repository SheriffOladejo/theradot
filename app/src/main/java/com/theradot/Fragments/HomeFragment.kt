package com.theradot.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.theradot.Adapters.ExerciseAdapter
import com.theradot.Models.Exercise
import com.theradot.R
import com.theradot.Utilities.Prevalent
import com.theradot.Utilities.UtilityMethods
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment(val c: Context): Fragment() {

    private var list = ArrayList<Exercise>()
    private lateinit var adapter: ExerciseAdapter
    private var util = UtilityMethods(context = c)
    private lateinit var card_number: String
    private lateinit var view_: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Paper.init(c)
        view_ = inflater.inflate(R.layout.fragment_home, container, false)
        card_number = Paper.book().read(Prevalent.CARD_NUMBER)
        view_.recyclerview.layoutManager = LinearLayoutManager(c)
        view_.refresh.setOnRefreshListener { getExercises() }
        getExercises()
        return view_
    }

    fun getExercises(){
        list.clear()
        view_.refresh.isRefreshing = true
        val ref = FirebaseDatabase.getInstance().reference.child("Users").child(card_number).child("Activity")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                util.toast("An error occurred... Try again")
                view_.refresh.isRefreshing = false
                //getExercises()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(data in snapshot.children){
                        list.add(Exercise(
                            image_thumbnail = data.child("thumbnail_url").value.toString(),
                            exercise_category = data.child("category").value.toString(),
                            exercise_title = data.child("title").value.toString(),
                            video_url = data.child("video_url").value.toString(),
                            objectives = data.child("objectives").value.toString(),
                            exercise_description = data.child("description").value.toString(),
                            completed_reps = data.child("completed_reps").value.toString(),
                            creation_date = data.child("creation_date").value.toString(),
                            exercise_id = data.key.toString(),
                            number_of_reps = data.child("number_of_reps").value.toString()
                        ))
                    }
                    adapter = ExerciseAdapter(c, list)
                    view_.recyclerview.adapter = adapter
                    view_.refresh.isRefreshing = false
                }
                else{
                    //title.setText("You don't have any ativities yet")
                    view_.refresh.isRefreshing = false
                }
            }
        })
    }
}