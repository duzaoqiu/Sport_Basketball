package com.bench.android.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.bench.android.core.content.commonio.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 拍照工具类
 * @author zhouyi
 */
public class TakePhotoUtils {

    private String mPath;

    private Activity activity;

    public TakePhotoUtils(Activity activity) {
        this.activity = activity;
    }

    /**
     *
     * @param actionCode startActivityForResult的requestCode
     */
    public void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = new File(createImageFile(activity.getApplicationContext()));
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mPath = photoFile.getAbsolutePath();
                Uri photoURI = FileUtils.getFileUri(activity, new File(mPath));
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                activity.startActivityForResult(takePictureIntent, actionCode);
            }
        }

    }

    public static String createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        String path = image.getAbsolutePath();
        return path;
    }

    public String getImagePath() {
        return mPath;
    }


}
