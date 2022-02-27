package com.theradot.Activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.installations.FirebaseInstallations
import com.theradot.R
import com.theradot.Utilities.Prevalent
import com.theradot.Utilities.UtilityMethods
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_verify_phone_number.*
import java.util.concurrent.TimeUnit

class VerifyPhoneNumberActivity : AppCompatActivity() {

    private lateinit var phone_number: String
    private lateinit var card_number: String

    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var auth: FirebaseAuth
    private lateinit var verification_id: String
    private lateinit var code: String


    private var util = UtilityMethods(this@VerifyPhoneNumberActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone_number)

        auth = FirebaseAuth.getInstance()

        callbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                progress_bar.visibility = View.GONE
                signInWithPhoneAuthCredential(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                progress_bar.visibility = View.GONE
                util.toast("Error verifying your number: $p0")
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                verification_id = p0
            }

        }

        verify.setOnClickListener(object: View.OnClickListener{
            override fun onClick(view: View){
                code = edt1.text.toString() + edt2.text.toString() + edt3.text.toString() + edt4.text.toString()+ edt5.text.toString()+ edt6.text.toString()
                if(code.length == 6){
                    val credential = PhoneAuthProvider.getCredential(verification_id, code)
                    signInWithPhoneAuthCredential(credential)
                }
            }
        })

        edt1.addTextChangedListener(GenericTextWatcher(edt1, edt2))
        edt2.addTextChangedListener(GenericTextWatcher(edt2, edt3))
        edt3.addTextChangedListener(GenericTextWatcher(edt3, edt4))
        edt4.addTextChangedListener(GenericTextWatcher(edt4, edt5))
        edt5.addTextChangedListener(GenericTextWatcher(edt5, edt6))
        edt6.addTextChangedListener(GenericTextWatcher(edt6, null))

        edt1.setOnKeyListener(GenericKeyEvent(edt1, null))
        edt2.setOnKeyListener(GenericKeyEvent(edt2, edt1))
        edt3.setOnKeyListener(GenericKeyEvent(edt3, edt2))
        edt4.setOnKeyListener(GenericKeyEvent(edt4, edt3))
        edt5.setOnKeyListener(GenericKeyEvent(edt5, edt4))
        edt6.setOnKeyListener(GenericKeyEvent(edt6, edt5))

        phone_number = "+234"+intent.getStringExtra("phone_number") as String
        card_number = intent.getStringExtra("card_number") as String
        verifyPhoneNumber(phone_number)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        progress_bar.visibility = View.VISIBLE
        auth.signInWithCredential(credential).addOnCompleteListener(this@VerifyPhoneNumberActivity, OnCompleteListener {task->
            if(task.isSuccessful){
                val user_ref = FirebaseDatabase.getInstance().reference.child("Users").child(card_number)
                val map = HashMap<String, Any>()
                map.put("Phone number", phone_number)
                FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener{
                    if(it.isSuccessful){
                        val token = it.result!!.token
                        map.put("Firebase token", token)
                        user_ref.updateChildren(map).addOnCompleteListener(this@VerifyPhoneNumberActivity, OnCompleteListener {task->
                            if(task.isSuccessful){
                                progress_bar.visibility = View.GONE
                                Paper.book().write(Prevalent.IS_LOGGED_IN, "true")
                                Paper.book().write(Prevalent.CARD_NUMBER, card_number)
                                val intent = Intent(this@VerifyPhoneNumberActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                progress_bar.visibility = View.GONE
                                util.toast("An error occurred")
                            }
                        })
                    }
                    else{
                        progress_bar.visibility = View.GONE
                        Toast.makeText(this@VerifyPhoneNumberActivity, "Could not update firebase token", Toast.LENGTH_LONG).show()
                    }
                }

            }
            else{
                util.toast("Unable to verify your number")
                progress_bar.visibility = View.GONE
            }
        })
    }

    private fun verifyPhoneNumber(phone_number: String){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phone_number,
            45,
            TimeUnit.SECONDS,
            this@VerifyPhoneNumberActivity,
            callbacks
        )
    }

    fun verifyCode(code: String){
        if(code.equals("1234")){
            print("Code correct")
        }
        else{
            print("Incorrect code")
        }
    }

    class GenericKeyEvent internal constructor(private val currentView: EditText, private val previousView: EditText?) : View.OnKeyListener{
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if(event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.edt1 && currentView.text.isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }
    }

    class GenericTextWatcher internal constructor(private val currentView: View, private val nextView: View?) :
        TextWatcher {

        override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
            val text = editable.toString()
            when (currentView.id) {
                R.id.edt1 -> if (text.length == 1) {
                    nextView!!.requestFocus()
                }
                R.id.edt2 -> if (text.length == 1) {
                    nextView!!.requestFocus()
                }
                R.id.edt3 -> if (text.length == 1) {
                    nextView!!.requestFocus()
                }
                R.id.edt4 -> if (text.length == 1) {
                    nextView!!.requestFocus()
                }
                R.id.edt5 -> if (text.length == 1) {
                    nextView!!.requestFocus()
                }
                R.id.edt6 -> if (text.length == 1) {
                }
            }
        }

        override fun beforeTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) { // TODO Auto-generated method stub
        }

        override fun onTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) { // TODO Auto-generated method stub
        }

    }
}