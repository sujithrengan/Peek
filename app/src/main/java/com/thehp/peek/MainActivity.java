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
        import android.widget.Button;
        import android.widget.FrameLayout;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
        import com.bumptech.glide.load.engine.DiskCacheStrategy;

        import java.io.File;
        import java.util.ArrayList;
        import java.util.List;


public class MainActivity extends AppCompatActivity implements FlingCardListener.ActionDownInterface {

    public static MyAppAdapter myAppAdapter;
    public static MyAppAdapter.ViewHolder viewHolder;
    private SwipeFlingAdapterView flingContainer;
    public static int MODE_BROWSE=1;
    public static int MODE_HEART=2;

    public static int mode;
    public static ProgressBar load;
    public static void removeBackground() {

        viewHolder.background.setVisibility(View.GONE);
        myAppAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mode=MODE_BROWSE;
        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        load=(ProgressBar)findViewById(R.id.marker_progress);
        final Button mode_btn=(Button)findViewById(R.id.mode_btn);
        mode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode==MODE_BROWSE)
                {
                    mode=MODE_HEART;
                    mode_btn.setBackgroundResource(R.drawable.heart2);
                }

                else
                {
                    mode=MODE_BROWSE;
                    mode_btn.setBackgroundResource(R.drawable.browse2);
                }

            }
        });

        Utilities.dataset = new ArrayList<>();

        Utilities.janitor= Typeface.createFromAsset(getAssets(),"font/Janitor.otf");
        Utilities.splurge= Typeface.createFromAsset(getAssets(),"font/Splurge.TTF");



        Utilities.dataset.add(new Data("info","Swipe left to heart. Swipe right to skip",1));
        new PageScraper(MainActivity.this, Utilities.URL).execute();

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
                    if(Utilities.last_name==null&&PageScraper.IsFetching==false) {
                        new PageScraper(MainActivity.this, Utilities.URL).execute();

                    }
                    else if(PageScraper.IsFetching==false)
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
                    if(Utilities.last_name==null&&PageScraper.IsFetching==false) {
                        new PageScraper(MainActivity.this, Utilities.URL).execute();

                    }
                    else if(PageScraper.IsFetching==false)
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

                load.setVisibility(View.VISIBLE);
                Log.e("url", Utilities.dataset.get(0).getThumbPath());
                new GSearch(MainActivity.this,Utilities.dataset.get(0).title,0).execute();

            }
        });

    }

    @Override
    public void onActionDownPerform() {
        Log.e("action", "bingo");
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



