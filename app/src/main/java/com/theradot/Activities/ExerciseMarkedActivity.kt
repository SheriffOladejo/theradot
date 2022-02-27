package com.theradot.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.theradot.R
import kotlinx.android.synthetic.main.activity_exercise_marked.*

class ExerciseMarkedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_marked)

        back.setOnClickListener{
            startActivity(Intent(this@ExerciseMarkedActivity, MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ExerciseMarkedActivity, MainActivity::class.java))
        finish()
    }
}