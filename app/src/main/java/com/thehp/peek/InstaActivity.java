package com.thehp.peek;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class InstaActivity extends AppCompatActivity implements FlingCardListener.ActionDownInterface {

    public  static MainActivity.MyAppAdapter myAppAdapter;
    public static MainActivity.ViewHolder viewHolder;
    private SwipeFlingAdapterView flingContainer;

    public static void removeBackground() {


        viewHolder.background.setVisibility(View.GONE);
        myAppAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        Utilities.idataset = new ArrayList<>();


        Utilities.idataset.add(new Data("info", "Swipe left to heart. Swipe right to skip", 1));
        //Todo scrap insta pics


        flingContainer.setAdapter(myAppAdapter);
    }

    @Override
    public void onActionDownPerform() {

    }
}
