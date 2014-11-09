package com.HackS.Fall2014.hacksc_fooder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Cuiweidi on 11/8/2014.
 */
class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public ImageDownloader(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap mIcon = null;
        System.out.println("Hello1");
        try {
            InputStream in = new java.net.URL(url).openStream();
            System.out.println("Hello2");
            mIcon = BitmapFactory.decodeStream(in);
            System.out.println("Hello4");
        } catch (Exception e)    {
            Log.e("Error", e.getMessage());
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}