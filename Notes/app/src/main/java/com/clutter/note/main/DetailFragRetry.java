package com.clutter.note.main;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by csimcik on 10/5/2017.
 */
public class DetailFragRetry extends Fragment {
    TextView photo,movie,voice,note,vocab;
    ImageView picTaken;
    RecyclerView activityList;
    public static ActivityAdapter thisAdapter;
    FrameLayout workPlace;
    LinearLayout addBar;
    LinearLayout mmLayout;
    LinearLayout.LayoutParams params1;
    LinearLayout.LayoutParams params2;


    static String newVocabWord;
    static String newNotePass;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.detail_list, container, false);
        activityList = (RecyclerView) view.findViewById(R.id.daylist);
        LinearLayoutManager activityMan = new LinearLayoutManager(getActivity());
        addBar = (LinearLayout) view.findViewById(R.id.action_row);
        activityList.setLayoutManager(activityMan);
        thisAdapter = new ActivityAdapter(getActivity());
        activityList.setAdapter(thisAdapter);
        photo = (TextView) view.findViewById(R.id.pic);
        movie = (TextView) view.findViewById(R.id.mov);
        voice = (TextView) view.findViewById(R.id.voi);
        note = (TextView) view.findViewById(R.id.not);
        vocab = (TextView) view.findViewById(R.id.voc);
        //notes on-click listener
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                NoteFrag noteFrag = new NoteFrag();
                transaction.replace(MainActivity.fragmentWindow.getId(), noteFrag);
                transaction.addToBackStack(null);
                transaction.commit();
                addBar.setVisibility(View.INVISIBLE);


            }
        });
        //photo on-click Listener
        photo.setOnClickListener(new View.OnClickListener() {
            static final int REQUEST_TAKE_PHOTO = 1;

            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(),
                            "com.example.android.fileprovider",
                            photoFile);
                    photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(photoIntent, REQUEST_TAKE_PHOTO);
                }
            }
        });
        vocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                VocabFrag vocabFrag = new VocabFrag();
                transaction.replace(MainActivity.fragmentWindow.getId(),vocabFrag);
                transaction.addToBackStack(null);
                transaction.commit();
                addBar.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            File storageDir = getActivity().getFilesDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File img = null;
            try {
                img = File.createTempFile(timeStamp + "thumb",".png",storageDir);
                img.mkdir();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MainActivity.daysL.get(MainActivity.todayObj).photoObj.add(mCurrentPhotoPath);
            String[] tempString = new String[3];
            tempString[0] = "P";
            tempString[1] = mCurrentPhotoPath;
            tempString[2] = img.getAbsolutePath();
            Log.i("the PATH", img.toString());
            FileOutputStream fos = null;
            Bitmap savedPic = null;
            try {
                savedPic = getOrientation(mCurrentPhotoPath);
                Log.i("the FILE is this***",savedPic.toString()+ " "+ img);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos = new FileOutputStream(img);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            savedPic.compress(Bitmap.CompressFormat.PNG, 90, fos);
            MainActivity.daysL.get(MainActivity.todayObj).allObj.add(tempString);
            try {
                fos.flush();
                fos.close();
                thisAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        }
    String mCurrentPhotoPath;
    String mCurrentThumbPath;
// This creates and image file from camera
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );




        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("*********", mCurrentPhotoPath);
        return image;
    }
    private File createThumbFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "thumb"+"_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentThumbPath = image.getAbsolutePath();
        Log.i("*********", mCurrentThumbPath);
        return image;
    }
    public Bitmap getOrientation(String myPhoto) throws IOException {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(myPhoto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Log.i("orientation", myPhoto + " " + String.valueOf(orientation));
        Bitmap greyBitmap = null;
        Bitmap scaledBitmap;
        Bitmap bitmapPre;
        Bitmap bitmap = BitmapFactory.decodeFile(myPhoto);
        int heightMod;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmap = rotateImage(bitmap, 90);
                Log.i("bitmap height", String.valueOf(bitmap.getHeight()));
                heightMod = (Integer) bitmap.getHeight() / (bitmap.getWidth() / MainActivity.w);
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, MainActivity.w, heightMod, false);
                Log.i("bitmap height", String.valueOf(scaledBitmap.getHeight()));
                bitmapPre = Bitmap.createBitmap(scaledBitmap, 0, scaledBitmap.getHeight() / 2 - 100, scaledBitmap.getWidth(),175);
                greyBitmap = toGrayscale(bitmapPre);
                break;



            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmap = rotateImage(bitmap, 180);
                Log.i("bitmap height", String.valueOf(bitmap.getHeight()));
                heightMod = (Integer) bitmap.getHeight() / (bitmap.getWidth() / MainActivity.w);
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, MainActivity.w, heightMod, false);
                Log.i("bitmap height", String.valueOf(scaledBitmap.getHeight()));
                bitmapPre = Bitmap.createBitmap(scaledBitmap, 0, scaledBitmap.getHeight() / 2 + 75, scaledBitmap.getWidth(), 150);
                greyBitmap = toGrayscale(bitmapPre);
                break;


            case ExifInterface.ORIENTATION_ROTATE_270:
                bitmap = rotateImage(bitmap, 270);
                Log.i("bitmap height", String.valueOf(bitmap.getHeight()));
                heightMod = (Integer) bitmap.getHeight() / (bitmap.getWidth() / MainActivity.w);
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, MainActivity.w, heightMod, false);
                Log.i("bitmap height", String.valueOf(scaledBitmap.getHeight()));
                bitmapPre = Bitmap.createBitmap(scaledBitmap, 0, scaledBitmap.getHeight() / 2 + 75, scaledBitmap.getWidth(), 150);
                greyBitmap = toGrayscale(bitmapPre);
                break;




        }

        return greyBitmap;
    }
    public Bitmap rotateImage(Bitmap source, float angle) {

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }



}
