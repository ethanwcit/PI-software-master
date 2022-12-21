package com.group20.pi_software.utility;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import com.group20.pi_software.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareHelper {

    public static File getScreenShot(View v) throws IOException {
        File file = new File(v.getContext().getExternalCacheDir(), "PI-software_" + System.currentTimeMillis() + ".jpg");
        Bitmap bitmap = null;
        v.setDrawingCacheEnabled(true);
        v.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
        try(FileOutputStream out = new FileOutputStream(file)){
            bitmap = Bitmap.createBitmap(v.getDrawingCache());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        }

        v.setDrawingCacheEnabled(false);
        return  file;
    }

    public static void openShareThisApp(View v){
        ShareCompat.IntentBuilder builder = new ShareCompat.IntentBuilder(v.getContext());
        String subject = "PI-Software";
        try {
            builder.setSubject(subject)
                    .setStream(FileProvider.getUriForFile(v.getContext(), BuildConfig.APPLICATION_ID + ".provider", getScreenShot(v)))
                    .setType("image/jpg");
            builder.createChooserIntent();
            builder.startChooser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
