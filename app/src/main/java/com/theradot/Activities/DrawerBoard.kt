package com.theradot.Activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.theradot.R
import com.theradot.Utilities.Prevalent
import io.paperdb.Paper
import kotlinx.android.synthetic.main.drawer_board.*

class DrawerBoard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_board)

        Paper.init(this)
    }

    fun initDrawer(){
        sign_out.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                Paper.book().write(Prevalent.IS_LOGGED_IN, "false")
                startActivity(Intent(this@DrawerBoard,SignInActivity::class.java))
                finish()
            }
        })

        profile.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                startActivity(Intent(this@DrawerBoard,ProfileActivity::class.java))
            }
        })
    }
}