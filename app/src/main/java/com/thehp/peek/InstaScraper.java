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
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by HP on 23-04-2016.
 */
class InstaScraper extends AsyncTask<Void,String,String>
{
    Context context;
    String url;
    String username;
    public static boolean IsFetching=false;
    InstaScraper(Context context, String username,String start)
    {
        this.context=context;
        this.username=username;
        this.url="https://www.instagram.com/"+username+"/?max_id="+start;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("j", "pree");
        IsFetching=true;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        //Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
        if(!s.equals("dickmysuck"))
        {
            Toast.makeText(context, "Received", Toast.LENGTH_SHORT).show();

            JSONParser.ParseIData(s);
            IsFetching=false;

        }
        else {

                Toast.makeText(context, "Internet?", Toast.LENGTH_SHORT).show();
            IsFetching=false;
        }


    }

    @Override
    protected String doInBackground(Void... voids) {

        String res="dickmysuck";
        try {
            Document doc = Jsoup.connect(url).get();
           // Log.e("j", doc.title());

            Elements e=doc.getElementsByTag("body");
            Log.e("ji", e.html());
            Log.e("jo", e.outerHtml());

            doc=Jsoup.parse(e.html());
            e=doc.getElementsByTag("script");
            for(int i=0;i<e.size();i++)
            {
                if(e.get(i).data().startsWith("window._sharedData = "))
                    res=e.get(i).data().substring(("window._sharedData = ").length(),e.get(i).data().length());
            }

        } catch (Exception e) {
            Log.e("Async", "catched");
            e.printStackTrace();
        }
        return res;
    }
}