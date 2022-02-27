package com.theradot.Activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.theradot.Fragments.ReminderFragment
import com.theradot.Fragments.HomeFragment
import com.theradot.Fragments.ProgressFragment
import com.theradot.R
import com.theradot.Utilities.DbHelper
import com.theradot.Utilities.Prevalent
import com.theradot.Utilities.User
import com.theradot.Utilities.UtilityMethods
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Drawer() {

    private lateinit var card_number: String
    private var util = UtilityMethods(this)
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var user: User
    private lateinit var helper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Paper.init(this)
        helper = DbHelper(this)
        initDrawer()
        card_number = Paper.book().read(Prevalent.CARD_NUMBER)
        user = helper.user

        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            if(it.isSuccessful){
                var card_number = Paper.book().read(Prevalent.CARD_NUMBER) as String
                val ref = FirebaseDatabase.getInstance().reference.child("Users").child(card_number)
                val map = HashMap<String, Any>()
                map.put("Firebase token",it.result.toString())
                ref.updateChildren(map)
            }
        }

        user_greeting.setText("Hi there, ${user.firstname}")
        activities_tv.setText("Here are your activities")

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open,R.string.close)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.text_color)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        initBottomNav()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }

    fun initBottomNav(){
        val item1 = AHBottomNavigationItem("Home", R.drawable.ic_home)
        val item2 = AHBottomNavigationItem("Progress", R.drawable.ic_progress)
        val item3 = AHBottomNavigationItem("Reminder", R.drawable.ic_task)

        bottom_nav.addItem(item1)
        bottom_nav.addItem(item2)
        bottom_nav.addItem(item3)

        bottom_nav.setOnTabSelectedListener(object: AHBottomNavigation.OnTabSelectedListener{
            override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
                when(position){
                    0 -> {
                        title_.visibility = View.GONE
                        user_greeting.visibility = View.VISIBLE
                        user_greeting.setText("Hi there, ${user.firstname}")
                        activities_tv.setText("Here are your activities")
                        activities_tv.visibility = View.VISIBLE
                        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, HomeFragment(this@MainActivity)).commit()
                        //util.replaceFragment(HomeFragment(), "HomeFragment", fragment_manager = supportFragmentManager)
                        return true
                    }

                    1 -> {
                        title_.visibility = View.VISIBLE
                        title_.setText("Exercise progress")
                        user_greeting.visibility = View.GONE
                        activities_tv.visibility = View.GONE
                        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, ProgressFragment(this@MainActivity)).commit()
                        return true
                    }

                    2 -> {
                        title_.visibility = View.VISIBLE
                        title_.setText("Reminder")
                        user_greeting.visibility = View.GONE
                        activities_tv.visibility = View.GONE
                        supportFragmentManager.beginTransaction().replace(R.id.frame_layout,
                            ReminderFragment(this@MainActivity)
                        ).commit()
                        return true
                    }

                    else ->{
                        title_.visibility = View.GONE
                        user_greeting.visibility = View.VISIBLE
                        user_greeting.setText("Hi there, ${user.firstname}")
                        activities_tv.setText("Here are your activities")
                        activities_tv.visibility = View.VISIBLE
                        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, HomeFragment(this@MainActivity)).commit()
                        return true
                    }
                }
            }
        })

        bottom_nav.defaultBackgroundColor == resources.getColor(R.color.text_color)
        bottom_nav.accentColor = resources.getColor(R.color.button_green)
        bottom_nav.inactiveColor = resources.getColor(R.color.text_color)

        bottom_nav.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        bottom_nav.currentItem = 0
    }
}