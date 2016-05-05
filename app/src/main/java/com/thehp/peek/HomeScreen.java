package com.thehp.peek;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import link.fls.swipestack.SwipeStack;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ArrayList<String> mData =new ArrayList<String>();
        mData.add("Heyy");
        mData.add("Hello");
        mData.add("Holaa");
        mData.add("Oyye");

        SwipeStack swipeStack = (SwipeStack) findViewById(R.id.swipeStack);
        swipeStack.setAdapter(new SwipeStackAdapter(mData));
    }


    public class SwipeStackAdapter extends BaseAdapter {

        private ArrayList<String> mData;

        public SwipeStackAdapter(ArrayList<String> data) {
            this.mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.searchitem, parent, false);
            TextView textViewCard = (TextView) convertView.findViewById(R.id.clicktxt);
            textViewCard.setText(mData.get(position));

            return convertView;
        }
    }


}


