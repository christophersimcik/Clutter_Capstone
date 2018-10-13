package com.clutter.note.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by csimcik on 10/6/2017.
 */
public class ImageProcessor {
    String myPath;
    ExifInterface exif = null;
    public ImageProcessor(String photoPath) {
        myPath = photoPath;
    }

        public static Bitmap rotateImage(Bitmap source, float angle) {

            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        }
    }

