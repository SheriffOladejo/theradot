package com.theradot.Activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.theradot.Fragments.OnBoardScreen1
import com.theradot.Fragments.OnBoardScreen2
import com.theradot.Fragments.OnBoardScreen3
import com.theradot.R
import com.theradot.Utilities.Prevalent
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_onboarding.*

class OnBoardingActivity : FragmentActivity(){

    private lateinit var is_first_time: String
    private var current_item = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        view_pager.adapter = MyPagerAdapter(get_started, next, skip_for_now, supportFragmentManager)

        Paper.init(this)

        Paper.init(this)

        val logged_in = Paper.book().read(Prevalent.IS_LOGGED_IN, "")
        if(logged_in.equals("true")){
            startActivity(Intent(this@OnBoardingActivity, MainActivity::class.java))
            finish()
        }

//        is_first_time = Paper.book().read(Prevalent.IS_FIRST_TIME,"")
//        if(is_first_time == "true"){
//            startActivity(Intent(this@OnBoardingActivity, SignInActivity::class.java))
//            finish()
//        }

        next.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                current_item = current_item + 1
                if(current_item == 2){
                    next.visibility = View.GONE
                    get_started.visibility = View.VISIBLE
                    skip_for_now.setText("Go back")
                }
                view_pager.setCurrentItem(current_item)
            }
        })

        skip_for_now.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                if(current_item == 2){
                    current_item = current_item - 1
                    view_pager.setCurrentItem(current_item)
                    skip_for_now.setText("Skip for now")
                    next.visibility = View.VISIBLE
                    get_started.visibility = View.GONE
                }
            }
        })

        get_started.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                Paper.book().write(Prevalent.IS_FIRST_TIME, "false")
                startActivity(Intent(this@OnBoardingActivity, SignInActivity::class.java))
                finish()
            }
        })
    }

    private class MyPagerAdapter(val get_started: Button, val next: Button, val skip_for_now: TextView, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager){
        override fun getItem(position: Int): Fragment {
            when(position){
                0 -> {
                    System.out.println("Position: " + position.toString())
                    return OnBoardScreen1()}
                1 -> {
//                    skip_for_now.setText("Skip for now")
//                    next.visibility = View.VISIBLE
//                    get_started.visibility = View.GONE
                    System.out.println("Position: " + position.toString())
                    return OnBoardScreen2()
                }
                2 -> {
                    System.out.println("Position: " + position.toString())
                    skip_for_now.setText("Go back")
                    //next.visibility = View.GONE
                    //get_started.visibility = View.VISIBLE
                    return OnBoardScreen3()
                }
                else -> {
                    return OnBoardScreen1()
                }
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }
}