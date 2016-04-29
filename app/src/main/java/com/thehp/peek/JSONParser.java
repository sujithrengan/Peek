package com.thehp.peek;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by HP on 23-04-2016.
 */
public class JSONParser {

    public static void ParseData(String s)
    {
        String url="url";
        String turl="url";
        String name="name";
        String title="title";
        int height=0,width=0;
        try {
            JSONObject data=new JSONObject(s);
            data=data.getJSONObject("data");
            JSONArray children=data.getJSONArray("children");
            for(int i=0;i<children.length();i++)
            {
                JSONObject childdata=children.getJSONObject(i).getJSONObject("data");
                url=childdata.getString("url");
                title=childdata.getString("title");
                name=childdata.getString("name");
                turl=url;
                String hint="";
                try {
                    hint = childdata.getString("post_hint");
                }
                catch (JSONException e){}
                if(hint.equals("link"))
                {
                    url+=".jpg";
                }
                try {


                    if (!childdata.getString("domain").startsWith("self")) {

                        width = childdata.getJSONObject("preview").getJSONArray("images").getJSONObject(0).getJSONObject("source").getInt("width");
                        if (width > 1000) {
                            JSONArray res = childdata.getJSONObject("preview").getJSONArray("images").getJSONObject(0).getJSONArray("resolutions");
                            for (int j = res.length() - 1; j >= 0; j--) {
                                width = res.getJSONObject(j).getInt("width");
                                turl = res.getJSONObject(j).getString("url");
                                turl = turl.replace("&amp;", "&");
                                if (width < 1000) {
                                    break;
                                }

                            }
                        } else
                            turl = url;
                    }

                }
                catch (JSONException e){

                }
                Utilities.dataset.add(new Data(url,turl,name,title,2));
                MainActivity.myAppAdapter.notifyDataSetChanged();
                Utilities.last_name=name;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public static void ParseGData(Context context,String search,int start,String s) {
        String vurl="";
        String url="";
        String username="";
        boolean retry=true;
        try {
            JSONObject data=new JSONObject(s);
            data=data.getJSONObject("responseData");
            JSONArray children=data.getJSONArray("results");
            for(int i=0;i<children.length();i++)
            {
                vurl=children.getJSONObject(i).getString("visibleUrl");
                url=children.getJSONObject(i).getString("unescapedUrl");

                if(vurl.equals("www.instagram.com")) {

                    username=url.substring(26,url.length());
                    // 26 is the length of http://www.instagram.com/

                   // Log.e("gsearch",vurl+"-"+url+"-"+username);
                    username=username.substring(0,username.indexOf("/"));
                    //Log.e("gsearch",vurl+"-"+url+"-"+username);
                    retry=false;
                    Log.e("insta",username);
                    break;
                }


            }

            start+=4;
            if(retry&&start<8)
                new GSearch(context,search,start).execute();

            else if(retry&start>=8)
            {

                Toast.makeText(context,"Account not found.",Toast.LENGTH_SHORT).show();
                MainActivity.load.setVisibility(View.GONE);
                InstaScraper.IsFetching=false;
            }

            else if(!retry)
            {
                MainActivity.myAppAdapter.notifyDataSetChanged();

                Intent i=new Intent(context,InstaActivity.class);
                i.putExtra("username",username);
                context.startActivity(i);
                InstaScraper.IsFetching=false;
                MainActivity.load.setVisibility(View.GONE);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    public static void ParseIData(String s) {
        String title="";
        String url="";
        String id="";
        try {
            JSONObject data=new JSONObject(s);
            data=data.getJSONObject("entry_data");
            JSONObject media=data.getJSONArray("ProfilePage").getJSONObject(0).getJSONObject("user").getJSONObject("media");
            Utilities.insta_start=media.getJSONObject("page_info").getString("end_cursor");
            JSONArray children=media.getJSONArray("nodes");
            Log.e("start",Utilities.insta_start);
            for(int i=0;i<children.length();i++)
            {

                JSONObject childdata=children.getJSONObject(i);
                url=childdata.getString("display_src");

                try {
                    title=childdata.getString("caption");
                }
                catch (JSONException e){

                }

                //id=childdata.getString("id");
                //Log.e("child",url+"="+title);

                Utilities.idataset.add(new Data(url,title, MyAppAdapter.TYPE_DATA));
                InstaActivity.myAppAdapter.notifyDataSetChanged();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
