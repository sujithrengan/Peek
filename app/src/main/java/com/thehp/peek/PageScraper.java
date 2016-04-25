package com.thehp.peek;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by HP on 23-04-2016.
 */
class PageScraper extends AsyncTask<Void,String,String>
{
    Context context;
    String url;
    public static boolean IsFetching=false;
    PageScraper(Context context,String url)
    {
        this.context=context;
        this.url=url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Utilities.IsFetching=true;
        IsFetching=true;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
        if(!s.equals("dickmysuck"))
        {
            //Toast.makeText(context, "Received", Toast.LENGTH_SHORT).show();

            JSONParser.ParseData(s);
            //Utilities.IsFetching=false;
            IsFetching=false;

        }
        else {

                Toast.makeText(context, "Internet?", Toast.LENGTH_SHORT).show();
            //Utilities.IsFetching=false;
            IsFetching=false;
        }


    }

    @Override
    protected String doInBackground(Void... voids) {

        String res="dickmysuck";
        HttpClient httpClient=new DefaultHttpClient();
        HttpEntity httpEntity=null;
        HttpGet httpGet=new HttpGet(url);
        httpGet.addHeader("User-agent","windows:peek:v1.0");


        try {
            HttpResponse response=httpClient.execute(httpGet);
            httpEntity=response.getEntity();
            res= EntityUtils.toString(httpEntity);

            Log.e("Async", "inside");

        } catch (IOException e) {
            Log.e("Async", "catched");
            e.printStackTrace();
        }
        return res;
    }
}