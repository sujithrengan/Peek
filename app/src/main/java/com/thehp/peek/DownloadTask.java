package com.thehp.peek;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by HP on 23-04-2016.
 */
class DownloadTask extends AsyncTask<String, Void, Bitmap> {
    ProgressDialog myPd_ring = null;

    Context context;
    String url;
    String name;

    DownloadTask(Context context,String url,String name)
    {
        this.context=context;
        this.url=url;
        this.name=name;
    }
    @Override
    protected void onPreExecute() {

        //myPd_ring = new ProgressDialog(context);
        //myPd_ring.setMessage("Loading...");
        //myPd_ring.show();

    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        HttpClient httpclient = new DefaultHttpClient();
        Bitmap image = null;
        HttpGet httpget = new HttpGet(url);
        try {

            HttpResponse response = httpclient.execute(httpget);
            HttpEntity httpEntity = null;

            httpEntity = response.getEntity();

            Log.e("code",String.valueOf(response.getStatusLine().getStatusCode())+"=="+response.getStatusLine().getReasonPhrase());

            Header h[]=response.getAllHeaders();
            for(int i=0;i<h.length;i++)
                Log.e("header",h[i].getName()+"="+h[i].getValue());
            byte[] img = EntityUtils.toByteArray(httpEntity);
            image = BitmapFactory.decodeByteArray(img, 0, img.length);



            Log.e("imgsave",img.length+"="+url);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;


    }

    @Override
    protected void onPostExecute(Bitmap bmp) {
        super.onPostExecute(bmp);
        //myPd_ring.dismiss();


        SaveImage save = new SaveImage(name, bmp);
        save.saveToCacheFile(bmp);
        addImageToGallery(save.getCacheFilename(), context);

        //Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_LONG).show();
    }


    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        values.put(MediaStore.MediaColumns.DATA, filePath);
        values.put(MediaStore.Images.Media.TITLE,"title");
        values.put(MediaStore.Images.Media.DISPLAY_NAME,"display_name");
        values.put(MediaStore.Images.Media.DESCRIPTION,"description");


        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}