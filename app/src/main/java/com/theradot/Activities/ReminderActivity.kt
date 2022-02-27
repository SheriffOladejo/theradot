package com.theradot.Activities

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.theradot.R
import kotlinx.android.synthetic.main.activity_reminder.*


class ReminderActivity : AppCompatActivity() {

    private val id: Long = 0
    private var mMediaPlayer: MediaPlayer? = null
    private lateinit var mVibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        mVibrator = applicationContext.getSystemService(VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(0, 1500, 5000)
        mVibrator.vibrate(pattern, 0)

       mMediaPlayer = MediaPlayer.create(this, R.raw.cuco_sound)

        mMediaPlayer!!.setAudioAttributes(
            AudioAttributes.Builder()
            .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build())
        //mMediaPlayer!!.setDataSource(this@ReminderActivity, Uri.parse("android.resource://com.theradot/raw/cuco_sound"))
        mMediaPlayer!!.isLooping = true
        //mMediaPlayer!!.prepare()
        mMediaPlayer!!.start()

        open_app.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                onFinish()
                startActivity(Intent(this@ReminderActivity, MainActivity::class.java))
                finish()
            }
        })
    }

    private fun stopMedialPlayer() {
        if (mMediaPlayer != null && mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
        }
    }

    private fun stopVibrator() {
        if (mVibrator != null) {
            mVibrator.cancel()
        }
    }

    override fun onPause() {
        super.onPause()
        //onFinish()
    }

    fun onFinish() {
        stopMedialPlayer()
        stopVibrator()
    }

}