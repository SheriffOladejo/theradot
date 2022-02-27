package com.theradot.Utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.theradot.Activities.MainActivity
import com.theradot.R


class UtilityMethods(val context: Context) {
//
//        fun replaceFragment(fragment: Fragment, tag: String) {
//            val transaction: FragmentTransaction = MainActivity.fragment_manager.beginTransaction()
//            //transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
//            transaction.replace(R.id.frame_layout, fragment)
//            transaction.setReorderingAllowed(true)
//            transaction.addToBackStack(tag)
//            transaction.commit()
//        }

      fun toast(message: String){
        val toast = Toast(context)
        toast.duration = Toast.LENGTH_LONG
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.custom_toast, null)
        val text_view = view.findViewById<TextView>(R.id.text)
        text_view.setTextColor(ContextCompat.getColor(context, R.color.white))
        text_view.setText(message)
        view.setBackgroundColor(context.resources.getColor(R.color.white))
        view.setBackgroundResource(R.drawable.edittext_curved_corner)
        toast.view = view
        toast.show()
    }

    fun fade(view: View) {
        val fade_in = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        view.animation = fade_in
        fade_in.start()
        view.visibility = View.VISIBLE
    }
}