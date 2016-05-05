package com.thehp.peek;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by HP on 23-04-2016.
 */
class WebstaSearch extends AsyncTask<Void,String,String>
{
    Context context;
    String search;
    String username="";
    int start;
    WebstaSearch(Context context, String search, int start)
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
            try {
                //Document doc = Jsoup.connect(url).get();
                Document doc=Jsoup.parse(s);

                Elements e=doc.getElementsByAttributeValue("class","username");


                username=e.get(0).html();
                 Toast.makeText(context,username,Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Log.e("Async", "catched");
                e.printStackTrace();
            }
            if(!username.equals("")) {
                //Toast.makeText(context, "Searched", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(context,InstaActivity.class);
                i.putExtra("username",username);
                context.startActivity(i);
                InstaScraper.IsFetching=false;
                MainActivity.load.setVisibility(View.GONE);

            }

            else {

                Toast.makeText(context, "Nope.", Toast.LENGTH_SHORT).show();
                MainActivity.load.setVisibility(View.GONE);

            }
        }
        else {

                Toast.makeText(context, "Internet?", Toast.LENGTH_SHORT).show();
                MainActivity.load.setVisibility(View.GONE);

        }


    }

    @Override
    protected String doInBackground(Void... voids) {

        String res="dickmysuck";
        search=search.replace(" ","%20");
        String url="http://websta.me/search/"+search;

        String html="";
        HttpClient httpClient=new DefaultHttpClient();
        HttpEntity httpEntity=null;
        HttpGet httpGet=new HttpGet(url);

        try {
            HttpResponse response=httpClient.execute(httpGet);
            httpEntity=response.getEntity();
            html= EntityUtils.toString(httpEntity);

            Log.e("Async", "inside");

        } catch (IOException e) {
            Log.e("Async", "catched");
            e.printStackTrace();
        }

        return html;
    }
}