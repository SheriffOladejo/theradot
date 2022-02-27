package com.theradot.Activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.theradot.R
import com.theradot.Utilities.MyVideoView
import com.theradot.Utilities.Prevalent
import com.theradot.Utilities.UtilityMethods
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_exercise_details.*


class ExerciseDetailsActivity: AppCompatActivity() {

    private lateinit var _exercise_category: String
    private lateinit var _video_url: String
    private lateinit var _number_of_reps: String
    private lateinit var _reps_completed: String
    private lateinit var _description: String
    private lateinit var _objectives: String
    private lateinit var _exercise_id: String
    private lateinit var media_controller: MediaController

    //private lateinit var video_view:MyVideoView

    private var util = UtilityMethods(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_details)

        Paper.init(this)
        //video_view = MyVideoView(this)

        media_controller = MediaController(this)
        media_controller.setAnchorView(video_view)
        video_view.setMediaController(media_controller)
        _exercise_category = intent.getStringExtra("exercise_category") as String
        _video_url = intent.getStringExtra("video_url") as String
        _number_of_reps = intent.getStringExtra("number_of_reps") as String
        _reps_completed = intent.getStringExtra("reps_completed") as String
        _description = intent.getStringExtra("description") as String
        _objectives = intent.getStringExtra("objectives") as String
        _exercise_id = intent.getStringExtra("exercise_id") as String

        exercise_category.setText(_exercise_category)
        exercise_description.setText(_description)

        video_view.setVideoPath(_video_url)

        video_view.start()
        video_view.setOnPreparedListener(object: MediaPlayer.OnPreparedListener{
            override fun onPrepared(mp: MediaPlayer){
                progress_bar.visibility = View.GONE
            }
        })

        if(_reps_completed == _number_of_reps){
            mark_complete.setBackgroundColor(resources.getColor(R.color.text_color))
            progress_checkbox.isChecked = true
            progress_checkbox.setText("Completed: ${_number_of_reps}/${_reps_completed}")
        }
        else{
            progress_checkbox.isChecked = false
            progress_checkbox.setText("Performed: ${_reps_completed}/${_number_of_reps}")
        }

        val objectives_list = _objectives.split(",")
        for(data in objectives_list){
            exercise_objectives.setText("- ${data}\n")
        }

        mark_complete.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                if(_reps_completed != _number_of_reps) {
                    if(video_view.isPlaying){
                        util.toast("Please complete the video first")
                    }
                    else{
                        updateLog()
                    }
                }
            }
        })

        back.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                startActivity(Intent(this@ExerciseDetailsActivity, MainActivity::class.java))
                finish()
            }
        })
    }

    private fun setDimension() {
        // Adjust the size of the video
        // so it fits on the screen
        val videoProportion = getVideoProportion()
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = 500
        val screenProportion = screenHeight.toFloat() / screenWidth.toFloat()
        val lp: ViewGroup.LayoutParams = video_view.getLayoutParams()
        if (videoProportion < screenProportion) {
            lp.height = screenHeight
            lp.width = (screenHeight.toFloat() / videoProportion).toInt()
        } else {
            lp.width = screenWidth
            lp.height = (screenWidth.toFloat() * videoProportion).toInt()
        }
        video_view.setLayoutParams(lp)
    }

    // This method gets the proportion of the video that you want to display.
    // I already know this ratio since my video is hardcoded, you can get the
    // height and width of your video and appropriately generate  the proportion
    //    as :height/width
    private fun getVideoProportion(): Float {
        return 1.5f
    }

    fun updateLog(){
        val card_number = Paper.book().read(Prevalent.CARD_NUMBER, "")
        val map = HashMap<String, Any>()
        val completed_reps = _reps_completed.toInt() + 1
        val time_performed = System.currentTimeMillis().toString()
        map.put("completed_reps", "${completed_reps}")
        map.put("time_performed", time_performed)
        val ref1 = FirebaseDatabase.getInstance().reference.child("Users").child(card_number).child("Activity").child(_exercise_id)
        ref1.updateChildren(map)
        val ref = FirebaseDatabase.getInstance().reference.child("Users").child(card_number).child("Activity").child(_exercise_id).child("logs").child(time_performed)
        ref.updateChildren(map).addOnCompleteListener{it ->
            if(it.isSuccessful){
                startActivity(Intent(this@ExerciseDetailsActivity, ExerciseMarkedActivity::class.java))
            }
            else{
                util.toast("Could not update")
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ExerciseDetailsActivity, MainActivity::class.java))
        finish()
    }

}