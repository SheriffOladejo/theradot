package com.theradot.Activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.theradot.R
import com.theradot.Utilities.UtilityMethods
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var _phone_number: String
    private lateinit var _card_number: String

    val util = UtilityMethods(this@SignUpActivity)
    private var is_verified = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        back.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                finish()
            }
        })

        next.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {

                if(is_verified){
                    is_verified = false
                    next.text = "Next"
                    val intent = Intent(this@SignUpActivity, VerifyPhoneNumberActivity::class.java)
                    intent.putExtra("phone_number", _phone_number)
                    intent.putExtra("card_number", _card_number)
                    startActivity(intent)
                }

                _phone_number = phone_number.text.toString()
                _card_number = card_number.text.toString()

                if(_card_number.isEmpty()){
                    card_number.setError("Required")
                    return;
                }
                else{
                    card_number.setError(null)
                }
                if(_phone_number.isEmpty()){
                    phone_number.setError("Required")
                    return
                }
                else if(_phone_number.length < 10){
                    phone_number.setError("Invalid")
                    return
                }
                else{
                    phone_number.setError(null)
                }
                verifyPhoneNumber(_phone_number)
            }
        })
    }

    fun verifyPhoneNumber(phone_number: String){
        progress_bar.visibility = View.VISIBLE
        val ref = FirebaseDatabase.getInstance().reference.child("Users")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    val _phone_number = data.child("Phone number").value.toString()
                    if(_phone_number == phone_number){
                        util.toast("This number is taken by another user")
                        return
                    }
                }
                verifyCardNumber(_card_number)
            }
        })
    }

    fun verifyCardNumber(card_number: String){
        progress_bar.visibility = View.VISIBLE
        val card_number_ref = FirebaseDatabase.getInstance().getReference().child("Users").child(card_number)
        card_number_ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                progress_bar.visibility = View.GONE
                util.toast("An error occurred, try again")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists()){
                    progress_bar.visibility = View.GONE
                    util.toast("Card not found")
                }
                else{
                    var _first_name = ""
                    var _last_name = ""
                    _first_name = snapshot.child("First name").value.toString()
                    _last_name = snapshot.child("Last name").value.toString()
                    util.fade(first_name_layout)
                    util.fade(last_name_layout)
                    first_name.setText(_first_name)
                    last_name.setText(_last_name)
                    next.text = "Proceed"
                    is_verified = true
                    progress_bar.visibility = View.GONE
                }
            }
        })
    }
}