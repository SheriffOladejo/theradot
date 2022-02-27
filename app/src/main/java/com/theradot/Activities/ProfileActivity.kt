package com.theradot.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.theradot.R
import com.theradot.Utilities.DbHelper
import com.theradot.Utilities.User
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var helper :DbHelper

    private final val REQUEST_TAKE_IMAGE = 200
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        helper = DbHelper(this)
        user = helper.user

        if(user.profile_image=="" || user.profile_image=="null"){

        }
        else{
            Picasso.with(this@ProfileActivity).load(user.profile_image).into(profile_image)
        }
        fullname.setText("${user.lastname} ${user.firstname} ${user.lastname}")
        phone_number.setText(user.phone_number)
        card_number.setText(user.card_number)
        gender.setText(user.gender)
        date_of_birth.setText(user.date_of_birth)
        eye_color.setText(user.eye_color)
        height.setText(user.height)

        select_profile_image.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select imaae"),
                    REQUEST_TAKE_IMAGE
                )
            }
        })

        back.setOnClickListener{
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_TAKE_IMAGE){
            val _uri = data?.data
            if(_uri != null){
                uploadImage(_uri)
            }
            else{
            }
        }
    }

    fun updateUser(user: User){
        helper.deleteUser()
        helper.addUser(user)
        Picasso.with(this@ProfileActivity).load(user.profile_image).into(profile_image)
        val map = HashMap<String, Any>()
        map.put("Profile image", user.profile_image)
        val ref = FirebaseDatabase.getInstance().reference.child("Users").child(user.card_number)
        ref.updateChildren(map)
        progress_bar.visibility = View.GONE
    }

    fun uploadImage(uri: Uri) {
        progress_bar.visibility = View.VISIBLE
        val ref = FirebaseStorage.getInstance().reference.child("user_images")
            .child(System.currentTimeMillis().toString() + "")
        ref.putFile(uri).addOnSuccessListener { taskSnapshot ->
            val urlTask =
                taskSnapshot.storage.downloadUrl
            while (!urlTask.isSuccessful);
            val downloadUrl = urlTask.result
            var url = downloadUrl.toString()
            user.profile_image = url
            updateUser(user)
        }.addOnFailureListener { e ->
            progress_bar.visibility = View.GONE
            println("Exception add chartview: " + e.message)
            Toast.makeText(
                this@ProfileActivity,
                "Unable to upload thumbnail",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}