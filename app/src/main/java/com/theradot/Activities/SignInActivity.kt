package com.theradot.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.theradot.R
import com.theradot.Utilities.DbHelper
import com.theradot.Utilities.Prevalent
import com.theradot.Utilities.User
import com.theradot.Utilities.UtilityMethods
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity: AppCompatActivity() {

    private lateinit var _card_number: String
    private val util = UtilityMethods(this@SignInActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        Paper.init(this)

        val logged_in = Paper.book().read(Prevalent.IS_LOGGED_IN, "")
        if(logged_in.equals("true")){
            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
            finish()
        }

        create_account.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?){
                val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        })

        login.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                _card_number = card_number.text.toString()
                if(!_card_number.isEmpty()){
                    progress_bar.visibility = View.VISIBLE
                    val ref = FirebaseDatabase.getInstance().reference.child("Users").child(_card_number)
                    ref.addValueEventListener(object: ValueEventListener{
                        override fun onCancelled(error: DatabaseError) {
                            progress_bar.visibility = View.GONE
                            util.toast("Error: ${error.toString()}")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(!snapshot.exists()){
                                progress_bar.visibility = View.GONE
                                util.toast("Card not found")
                            }
                            else{
                                Paper.book().write(Prevalent.IS_LOGGED_IN, "true")
                                Paper.book().write(Prevalent.CARD_NUMBER, _card_number)
                                val user = User(
                                    card_number = snapshot.child("Card number").value.toString(),
                                    creation_date = snapshot.child("Creation date").value.toString(),
                                    date_of_birth = snapshot.child("Date of birth").value.toString(),
                                    eye_color = snapshot.child("Eye color").value.toString(),
                                    firebase_token = snapshot.child("Firebase token").value.toString(),
                                    firstname = snapshot.child("First name").value.toString(),
                                    gender = snapshot.child("Gender").value.toString(),
                                    height = snapshot.child("Height").value.toString(),
                                    lastname = snapshot.child("Last name").value.toString(),
                                    middlename = snapshot.child("Middle name").value.toString(),
                                    phone_number = snapshot.child("Phone number").value.toString(),
                                    profile_image = snapshot.child("Profile image").value.toString()
                                )
                                DbHelper(this@SignInActivity).addUser(user)
                                startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                                finish()
                            }
                        }
                    })
                }
            }
        })
    }
}