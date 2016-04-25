package com.thehp.peek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by HP on 25-04-2016.
 */
public class MyAppAdapter extends BaseAdapter {


    public  ViewHolder viewHolder;
    private int TYPE_INFO=1;
    private int TYPE_DATA=2;

    public List<Data> parkingList;
    public Context context;

    private MyAppAdapter(List<Data> apps, Context context) {
        this.parkingList = apps;
        this.context = context;
    }

    @Override
    public int getCount() {
        return parkingList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        //View rowView = convertView;
        View rowView = null;



        if (rowView == null) {

            LayoutInflater inflater = LayoutInflater.from(context);
            if(parkingList.get(position).type==TYPE_DATA) {
                rowView = inflater.inflate(R.layout.item, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
                viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
                viewHolder.titleText = (TextView) rowView.findViewById(R.id.title_text);

            }
            else if(parkingList.get(position).type==TYPE_INFO)
            {
                rowView = inflater.inflate(R.layout.infoitem, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
                viewHolder.infoText = (TextView) rowView.findViewById(R.id.infoText);
            }


            //Log.e("cv","newview");
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            //Log.e("cv","convertview");

        }

        if(parkingList.get(position).type==TYPE_DATA) {
            Glide.with(context).load(parkingList.get(position).getThumbPath()).into(viewHolder.cardImage);
            viewHolder.titleText.setText(parkingList.get(position).title);
            viewHolder.titleText.setTypeface(Utilities.janitor);
        }
        else if(parkingList.get(position).type==TYPE_INFO) {
            viewHolder.infoText.setText(parkingList.get(position).getName());
        }



        //Log.e("type",getItemViewType(position)+"");
        return rowView;
    }

    public static class ViewHolder {
        public static FrameLayout background;
        public TextView infoText;
        public ImageView cardImage;
        public TextView titleText;

    }
}