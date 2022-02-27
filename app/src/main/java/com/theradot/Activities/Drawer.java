package com.theradot.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.theradot.R;
import com.theradot.Utilities.Prevalent;

import io.paperdb.Paper;

public class Drawer extends AppCompatActivity {

    LinearLayout profile, about, privacy, sign_out;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_board);
    }

    void initDrawer(){
        Paper.init(Drawer.this);

        profile = findViewById(R.id.profile);
        about = findViewById(R.id.about);
        privacy = findViewById(R.id.privacy);
        sign_out = findViewById(R.id.sign_out);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Drawer.this,ProfileActivity.class));
            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().write(Prevalent.INSTANCE.getIS_LOGGED_IN(), "false");
                startActivity(new Intent(Drawer.this,SignInActivity.class));
                finish();
            }
        });
    }
}
