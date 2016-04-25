package com.thehp.peek;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

public class InstaActivity extends AppCompatActivity implements FlingCardListener.ActionDownInterface {

    public  static MyAppAdapter myAppAdapter=null;
    public static MyAppAdapter.ViewHolder viewHolder;
    private SwipeFlingAdapterView flingContainer;

    public static int mode;
    public Stack<Data> dellist;
    public String username="";
    public static void removeBackground() {

    if(myAppAdapter!=null) {


    viewHolder.background.setVisibility(View.GONE);
    myAppAdapter.notifyDataSetChanged();
        //Log.e("vis",viewHolder.background.getVisibility()+"-"+MainActivity.viewHolder.background.getVisibility());
    }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta);
        mode=MainActivity.MODE_BROWSE;
        username=getIntent().getStringExtra("username");
        Toast.makeText(this,username,Toast.LENGTH_SHORT).show();
        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        final Button mode_btn=(Button)findViewById(R.id.mode_btn);
        mode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode==MainActivity.MODE_BROWSE)
                {
                    mode=MainActivity.MODE_HEART;
                    mode_btn.setBackgroundResource(R.drawable.heart2);
                }

                else
                {
                    mode=MainActivity.MODE_BROWSE;
                    mode_btn.setBackgroundResource(R.drawable.browse2);
                }

            }
        });



        Utilities.idataset = new ArrayList<>();
        dellist=new Stack<Data>();

        Utilities.idataset.add(new Data("info", "Swipe left or right to browse.", 1));
        //Todo scrap insta pics

        Utilities.insta_start="0";
        new InstaScraper(this,username,Utilities.insta_start).execute();
        myAppAdapter = new MyAppAdapter(Utilities.idataset, InstaActivity.this);

        flingContainer.setAdapter(myAppAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Utilities.idataset.add(0,dellist.pop());
                myAppAdapter.notifyDataSetChanged();
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                dellist.push(Utilities.idataset.remove(0));
                //Utilities.idataset.remove(0);
                myAppAdapter.notifyDataSetChanged();

                if(Utilities.idataset.size()<7)
                {
                    if(InstaScraper.IsFetching==false)
                    {
                        new InstaScraper(InstaActivity.this,username,Utilities.insta_start).execute();

                    }
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
            }
        });
    }

    @Override
    public void onActionDownPerform() {

    }
}
