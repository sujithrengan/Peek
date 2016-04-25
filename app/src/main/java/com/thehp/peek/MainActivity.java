package com.thehp.peek;

        import android.content.Context;
        import android.graphics.Typeface;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.FrameLayout;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
        import com.bumptech.glide.load.engine.DiskCacheStrategy;

        import java.io.File;
        import java.util.ArrayList;
        import java.util.List;


public class MainActivity extends AppCompatActivity implements FlingCardListener.ActionDownInterface {

    public static MyAppAdapter myAppAdapter;
    public static ViewHolder viewHolder;
    private SwipeFlingAdapterView flingContainer;

    public static void removeBackground() {


        viewHolder.background.setVisibility(View.GONE);
        myAppAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        Utilities.dataset = new ArrayList<>();

        Utilities.janitor= Typeface.createFromAsset(getAssets(),"font/Janitor.otf");
        Utilities.splurge= Typeface.createFromAsset(getAssets(),"font/Splurge.TTF");



        Utilities.dataset.add(new Data("info","Swipe left to heart. Swipe right to skip",1));
        new PageScraper(MainActivity.this, "https://www.reddit.com/r/prettygirls/.json").execute();

        myAppAdapter = new MyAppAdapter(Utilities.dataset, MainActivity.this);
        flingContainer.setAdapter(myAppAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {


                if(myAppAdapter.parkingList.get(0).type==myAppAdapter.TYPE_DATA)
                new DownloadTask(MainActivity.this,Utilities.dataset.get(0).getImagePath(),Utilities.dataset.get(0).getName()).execute();

                Utilities.dataset.remove(0);
                myAppAdapter.notifyDataSetChanged();


                if(Utilities.dataset.size()<10)
                {
                    //TODO:iterate to next page
                    if(Utilities.last_name==null&&Utilities.IsFetching==false) {
                        new PageScraper(MainActivity.this, "https://www.reddit.com/r/prettygirls/.json").execute();

                    }
                    else if(Utilities.IsFetching==false)
                    {
                        new PageScraper(MainActivity.this,Utilities.URL+"?count=25&after="+Utilities.last_name).execute();

                    }
                }

                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject

            }


            @Override
            public void onRightCardExit(Object dataObject) {

                Utilities.dataset.remove(0);
                myAppAdapter.notifyDataSetChanged();
                if(Utilities.dataset.size()<10)
                {
                    //TODO:iterate to next page
                    if(Utilities.last_name==null&&Utilities.IsFetching==false) {
                        new PageScraper(MainActivity.this, Utilities.URL).execute();

                    }
                    else if(Utilities.IsFetching==false)
                    {
                        new PageScraper(MainActivity.this,Utilities.URL+"?count=25&after="+Utilities.last_name).execute();

                    }
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

                //TODO:iterate to next page
                //new PageScraper(MainActivity.this,"").execute();
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                if(myAppAdapter.parkingList.get(0).type==myAppAdapter.TYPE_DATA) {
                    View view = flingContainer.getSelectedView();
                    view.findViewById(R.id.background).setAlpha(0);
                    view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                    view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                }
                }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);

                Log.e("url", Utilities.dataset.get(0).getThumbPath());
                new GSearch(MainActivity.this,Utilities.dataset.get(0).title,0).execute();

            }
        });

    }

    @Override
    public void onActionDownPerform() {
        Log.e("action", "bingo");
    }

    public static class ViewHolder {
        public static FrameLayout background;
        public TextView infoText;
        public ImageView cardImage;
        public TextView titleText;

    }

    public class MyAppAdapter extends BaseAdapter {


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

                LayoutInflater inflater = getLayoutInflater();
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
                Glide.with(MainActivity.this).load(parkingList.get(position).getThumbPath()).into(viewHolder.cardImage);
                viewHolder.titleText.setText(parkingList.get(position).title);
                viewHolder.titleText.setTypeface(Utilities.janitor);
            }
            else if(parkingList.get(position).type==TYPE_INFO) {
                viewHolder.infoText.setText(parkingList.get(position).getName());
            }



            //Log.e("type",getItemViewType(position)+"");
            return rowView;
        }
    }





    @Override
    protected void onDestroy() {

            super.onDestroy();
        try {
            trimCache(this);
             //Toast.makeText(this, "onDestroy ", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }







}



