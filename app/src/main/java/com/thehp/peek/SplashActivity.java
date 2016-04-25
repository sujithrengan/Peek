package com.thehp.peek;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //ImageView iv=(ImageView)findViewById(R.id.iv);

        //Glide.with(this).load("http://www.rsc.org/learn-chemistry/wiki/images/a/a5/X.png").into(iv);

        new PageScraper(this,"https://www.reddit.com/r/prettygirls/.json").execute();

    }


}
