package com.clutter.note.main;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;
/**
 * Created by csimcik on 10/24/2017.
 */
public class AudioFrag extends Fragment {
   // ImageView startBtn;
    RecordView recordButton;
    TextView fileName;
    //visual set-up
    public static ImageView imageView;
    public static Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    boolean mShouldContinue = true, isRecording = false;
    int counter = 0;
    CircleText timerView;
    public static String bmpFile;
    private static final int SAMPLING_RATE = 44100, SAMPLING_RATE_NXT = 8000;
    private long startTime = 0L;
    int spacer = 1000;
    int secs = 0;
    int mins = 0;
    int numberOfShort, numVis;
    String millis = null;
    String minutes = null;
    String seconds = null;
    String theTimer = null;
    String rawFileString;
    String audioSave;
    FileOutputStream outputStream;
    //DataOutputStream dataOutputStream;
    FileOutputStream os;
    public int last_count =0 ;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    ArrayList<ArrayList> graphs;
    private int mBufferSize,mBufferSizeNxt;
    AudioRecord record,visRec;
    int sampleSize = 1024;
    private short[] mAudioBuffer, visBuffer;
    private short[] buffer;
    public static ArrayList<ArrayList> graphQueue;
    private ArrayList<short[]> allSamples = new ArrayList<>();
    public static ArrayList<Bitmap> myBits;
    public static ArrayList<Integer> magnitudes;
    public static ArrayList<int[]> minMax;
    byte[] mBuffer;
    File voice;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.audio_view, container, false);


        Log.i("this is the number",String.valueOf(MainActivity.w/sampleSize));
        magnitudes = new ArrayList<>();
        recordButton= (RecordView) view.findViewById(R.id.recordbutton);
        fileName = (TextView) view.findViewById(R.id.name_of_file);
        timerView = (CircleText) view.findViewById(R.id.timerview);
        try {
            voice = createSoundFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ViewGroup.MarginLayoutParams params1 = (ViewGroup.MarginLayoutParams)recordButton.getLayoutParams();
        params1.rightMargin = ((MainActivity.w/3)/2)-35;
        Log.i("this is a view", String.valueOf(MainActivity.w/3));
        recordButton.setLayoutParams(params1);
        timerView.setStrokeColor("#808080");
        timerView.setStrokeWidth(2);
        timerView.setSolidColor("#ffffff");
        timerView.setGravity(Gravity.CENTER);
        timerView.setWidth(200);
        timerView.setHeight(200);
        try {
            createThumbFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // create and write raw audio data to a temporary File to be converted to .wav  ;
        try {
            os = new FileOutputStream(createRawFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Visual set-up
        imageView = (ImageView) view.findViewById(R.id.spectrogram);
        bitmap = Bitmap.createBitmap(MainActivity.w+50,200,Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        imageView.setImageBitmap(bitmap);
        graphs = new ArrayList<>();
        myBits = new ArrayList<>();
        // end visual setup
        PropertyValuesHolder pvhXscale = PropertyValuesHolder.ofFloat("scaleX",1.075f);
        PropertyValuesHolder pvhYscale = PropertyValuesHolder.ofFloat("scaleY",1.075f);
        PropertyValuesHolder pvhXscaleB = PropertyValuesHolder.ofFloat("scaleX",1.075f);
        PropertyValuesHolder pvhYscaleB = PropertyValuesHolder.ofFloat("scaleY",1.075f);
        PropertyValuesHolder pvhAlphaB = PropertyValuesHolder.ofFloat("alpha",.75f);
        final ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(recordButton,pvhXscaleB,pvhYscaleB,pvhAlphaB);
        final ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(recordButton,pvhXscale,pvhYscale);

        recordButton.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {
                                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                                    if( !recordButton.active) {
                                                        recordButton.illum = true;
                                                        recordButton.invalidate();
                                                        animator.setDuration(200).start();

                                                    }else{
                                                        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
                                                        objectAnimator.setRepeatCount(1);
                                                        objectAnimator.setDuration(200).start();
                                                        objectAnimator.addListener(new Animator.AnimatorListener() {
                                                            @Override
                                                            public void onAnimationStart(Animator animation) {

                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animator animation) {

                                                            }

                                                            @Override
                                                            public void onAnimationCancel(Animator animation) {

                                                            }

                                                            @Override
                                                            public void onAnimationRepeat(Animator animation) {

                                                            }
                                                        });
                                                    }
                                                    return true;
                                                }
                                                if (event.getAction() == MotionEvent.ACTION_UP) {
                                                    recordButton.illum = false;
                                                    startTime = SystemClock.uptimeMillis();
                                                    if (!recordButton.active) {
                                                        //recordButton.active = false;
                                                        recordButton.active = true;
                                                        isRecording = true;
                                                        recordButton.invalidate();
                                                        animator.setDuration(200).reverse();
                                                        customHandler.post(updateTimerThread);
                                                        recordAudio();
                                                        return true;
                                                    }else{
                                                        isRecording = false;
                                                        timeSwapBuff += timeInMilliseconds;
                                                        customHandler.removeCallbacks(updateTimerThread);
                                                        mShouldContinue = false;
                                                        MainActivity.viewSwitcher.showNext();
                                                        getFragmentManager().popBackStack();
                                                        return true;
                                                    }

                                                }
                                                return false;
                                            }
                                        }
            );
        return view;
    }




    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            if(isRecording) {
                timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
                updatedTime = timeSwapBuff + timeInMilliseconds;
                String lngth = String.valueOf(updatedTime);
                int dur = lngth.length();
                char[] chars = new char[2];
                if (dur >= 3) {
                    String.valueOf(updatedTime).getChars(dur - 3, dur - 1, chars, 0);
                }
                millis = String.valueOf(chars[0]) + String.valueOf(chars[1]);

                if (updatedTime > spacer) {
                    secs += 1;
                    spacer += 1000;
                }
                if (secs > 60) {
                    secs = 0;
                    mins += 1;
                }

                if (mins < 10) {
                    minutes = "0" + String.valueOf(mins);
                } else {
                    minutes = String.valueOf(mins);
                }
                if (secs < 10) {
                    seconds = "0" + String.valueOf(secs);
                } else {
                    seconds = String.valueOf(secs);
                }
                theTimer = minutes + ":" + seconds + ":" + millis;
                timerView.setText(theTimer);
                customHandler.post(this);
            }

        }
    };
    void recordAudio() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
                Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

                // buffer size in bytes
                mBufferSizeNxt = 1024;
                        // AudioRecord.getMinBufferSize(SAMPLING_RATE_NXT, AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
                mBufferSize = AudioRecord.getMinBufferSize(SAMPLING_RATE, AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
                mAudioBuffer = new short[mBufferSize];
                visBuffer = new short[mBufferSizeNxt];
                visRec = new AudioRecord(MediaRecorder.AudioSource.MIC,SAMPLING_RATE_NXT,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT,mBufferSizeNxt);
              //  record = new AudioRecord(MediaRecorder.AudioSource.MIC,SAMPLING_RATE,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT,mBufferSize);
               // if (record.getState() != AudioRecord.STATE_INITIALIZED) {
               //     Log.i("*AUDIO*", "Audio Record can't initialize!");
               //     return;
               // }
                long shortsRead = 0;
                visRec.startRecording();
               // record.startRecording();
                Log.i("*AUDIO*", "Start recording");
                Log.i("MshoudlCont.", String.valueOf(mShouldContinue));
                bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                while (mShouldContinue) {
                   // numberOfShort = record.read(mAudioBuffer, 0, mBufferSize);
                    numVis = visRec.read(visBuffer,0,mBufferSizeNxt);

                    allSamples.add(Arrays.copyOf(visBuffer,mBufferSizeNxt));
                    PassClass passClass = new PassClass(allSamples.get(allSamples.size()-1),bitmap);
                    new AsyncVis().execute(passClass);
                    //shortsRead += numberOfShort;
                    shortsRead += numVis;
                  //  Log.i("STUFF", String.valueOf(numVis) + " > " + String.valueOf(numberOfShort)+ " " + shortsRead);
                 //   mBuffer = short2byte(mAudioBuffer);
                    mBuffer = short2byte(visBuffer);
                    try {
                        os.write(mBuffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    counter ++;

                    // draw sample visualizer *here*

                }
              //  record.stop();
                visRec.stop();
               // record.release();
                visRec.release();

                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File deleteFile = new File(rawFileString);
                try {
                    FileInputStream fis;
                    fis = new FileInputStream(rawFileString);
                    byte[] rawData = new byte[(int)shortsRead*2];
                    fis.read(rawData);
                    outputStream = new FileOutputStream(voice.getPath());
                    // WAVE header
                    // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
                    writeString(outputStream, "RIFF"); // chunk id
                    writeInt(outputStream, 36 + rawData.length); // chunk size
                    writeString(outputStream, "WAVE"); // format
                    writeString(outputStream, "fmt "); // subchunk 1 id
                    writeInt(outputStream, 16); // subchunk 1 size
                    writeShort(outputStream, (short) 1); // audio format (1 = PCM)
                    writeShort(outputStream, (short) 1); // number of channels
                    writeInt(outputStream, SAMPLING_RATE_NXT); // sample rate
                    writeInt(outputStream,  SAMPLING_RATE_NXT * 2); // byte rate
                    writeShort(outputStream, (short) 2); // block align
                    writeShort(outputStream, (short) 16); // bits per sample
                    writeString(outputStream, "data"); // subchunk 2 id
                    writeInt(outputStream, rawData.length); // subchunk 2 size
                    // Audio data (conversion big endian -> little endian)
                    short[] shorts = new short[rawData.length/2];
                    ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
                    ByteBuffer bytes = ByteBuffer.allocate(shorts.length*2);
                    for (short s : shorts) {
                        bytes.putShort(s);
                    }

                    outputStream.write(fullyReadFileToBytes(deleteFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                PassClassEnd passClassEnd = null;
                passClassEnd = new PassClassEnd(magnitudes,allSamples,bmpFile,audioSave,theTimer);
                Bitmap passBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.play_icon);
                new AsyncSpect(context,passBitmap).execute(passClassEnd);
                deleteFile.delete();
                Log.i("this is a file > ", deleteFile.getAbsolutePath());
                Log.i("*AUDIO*", String.format("Recording stopped. Samples read: %d", shortsRead));
            }
        }).start();
    }

    // header for wav
    private void writeInt(final FileOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private void writeShort(final FileOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private void writeString(final FileOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }
    byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }
    private byte[] short2byte(short[] sData) {
        int shortArrsize = sData.length;
        byte[] bytes = new byte[shortArrsize * 2];
        for (int i = 0; i < shortArrsize; i++) {
            bytes[i * 2] = (byte) (sData[i] & 0x00FF);
            bytes[(i * 2) + 1] = (byte) (sData[i] >> 8);
            sData[i] = 0;
        }
        return bytes;

    }

    private File createThumbFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        Log.i("this is the activity", "tell me the activity");
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        }catch (IOException e){
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        bmpFile = image.getAbsolutePath();
        return image;
    }

    private File createSoundFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "WAV_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File voice = null;
        try {
            voice = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".wav",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        audioSave = voice.getAbsolutePath();
fileName.setText(imageFileName);
        return voice;
    }

    private File createRawFile(){
        String tempFile = "tempAudio";
        File tempAudio = getActivity().getExternalFilesDir(null);
        File audioTemp = null;
        try {
            audioTemp = File.createTempFile(
                    tempFile,".pcm",tempAudio
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        rawFileString = audioTemp.getAbsolutePath();
        return audioTemp;
    }
    @Override
    public void onPause(){
        super.onPause();
    }



}


