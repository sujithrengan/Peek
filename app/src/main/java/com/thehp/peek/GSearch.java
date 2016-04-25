package com.thehp.peek;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by HP on 23-04-2016.
 */
class GSearch extends AsyncTask<Void,String,String>
{
    Context context;
    String search;
    int start;
    GSearch(Context context, String search, int start)
    {
        this.context=context;
        this.search=search;
        this.start=start;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
        if(!s.equals("dickmysuck"))
        {
            //Toast.makeText(context, "Searched", Toast.LENGTH_SHORT).show();
            JSONParser.ParseGData(context,search,start,s);

        }
        else {

                Toast.makeText(context, "Internet?", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    protected String doInBackground(Void... voids) {

        String res="dickmysuck";
        HttpClient httpClient=new DefaultHttpClient();
        HttpEntity httpEntity=null;
        search=search.replace(" ","+");
        String url="http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q="+search+"&start="+start;
        HttpGet httpGet=new HttpGet(url);

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