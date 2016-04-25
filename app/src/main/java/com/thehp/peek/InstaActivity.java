package com.thehp.peek;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class InstaActivity extends AppCompatActivity implements FlingCardListener.ActionDownInterface {

    public  static MyAppAdapter myAppAdapter;
    public static MyAppAdapter.ViewHolder viewHolder;
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


        Utilities.idataset.add(new Data("info", "Swipe left or right to browse.", 1));
        //Todo scrap insta pics

        myAppAdapter = new MyAppAdapter(Utilities.idataset, InstaActivity.this);

        flingContainer.setAdapter(myAppAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {

            }

            @Override
            public void onRightCardExit(Object dataObject) {

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });
    }

    @Override
    public void onActionDownPerform() {

    }
}
