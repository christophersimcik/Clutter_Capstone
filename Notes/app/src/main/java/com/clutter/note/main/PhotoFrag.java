package com.clutter.note.main;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by csimcik on 10/21/2017.
 */
public class PhotoFrag extends Fragment {
    public static String mCurrentThumbPath;
    public static String mCurrentPhotoPath;
    private static final String TAG = "AndroidCameraApi";
    private CamButton takePictureButton;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private String cameraId;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest captureRequest;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler, optionsDelay;
    private HandlerThread mBackgroundThread;
    public  Size previewsize;
    private Size jpegSizes[] = null;
    private AutoFit textureView;
    private ImageView nightMode, torchMode, flashMode;
    private TextView exposure, exposureComp;
    private LinearLayout expBar;
    private ShadowCircle nightCirc,torchCirc,flashCirc;
    private CameraDevice cameraDevice;
    private CameraManager cameraManager;
    private CaptureRequest.Builder previewBuilder;
    private CameraCaptureSession previewSession;
    private FrameLayout cameraFrame;
    private float xVal;
    private Boolean pressed = false, nightToggle = false, torchToggle = false, flashToggle = false, interacting = false,autoToggle = true, canRun = true;
    private Range range;
    private long surfWidth, surfHeight;
    private int slideVal, alphaVal = 255, controlMode = CaptureRequest.CONTROL_MODE_AUTO;
    private Drawable[] flash = new Drawable[2], night = new Drawable[2], torch = new Drawable[2];
    Boolean canResize = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int orientationInt = this.getActivity().getResources().getConfiguration().orientation;
        View view;
            view = inflater.inflate(R.layout.camera_preview, container, false);
        optionsDelay = new Handler();
        cameraFrame = (FrameLayout) view.findViewById(R.id.camera_frame_layout);
        textureView = (AutoFit) view.findViewById(R.id.autoView);
        exposure = (TextView) view.findViewById(R.id.exposure);
        exposureComp = (TextView) view.findViewById(R.id.exp_comp);
        nightCirc = (ShadowCircle) view.findViewById(R.id.nightShadow);
        nightCirc.bringToFront();
        torchCirc = (ShadowCircle) view.findViewById(R.id.torchShadow);
        torchCirc.bringToFront();
        flashCirc = (ShadowCircle) view.findViewById(R.id.flashShadow);
        flashCirc.bringToFront();
        expBar = (LinearLayout) view.findViewById(R.id.linear_exp);
        exposureComp.setTextColor(Color.argb(100, 255, 255, 255));
        exposure.setTextColor(Color.argb(150, 255, 255, 255));
        exposureComp.setTextSize(16);
        exposure.setTextSize(64);
        flash[0] = getActivity().getDrawable(R.drawable.flash_on);
        flash[1] = getActivity().getDrawable(R.drawable.flash_off);
        night[0] = getActivity().getDrawable(R.drawable.night_on);
        night[1] = getActivity().getDrawable(R.drawable.night_off);
        torch[0] = getActivity().getDrawable(R.drawable.light_on);
        torch[1] = getActivity().getDrawable(R.drawable.light_off);
        flashMode = (ImageView) view.findViewById(R.id.flashmode);
        flashMode.bringToFront();
        torchMode = (ImageView) view.findViewById(R.id.torchmode);
        torchMode.bringToFront();
        nightMode = (ImageView) view.findViewById(R.id.nightmode);
        flashMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canRun = false;
                if (flashToggle) {
                    //alphaVal = 255;
                    flash[0].setAlpha(255);
                    flashMode.setImageDrawable(flash[1]);
                    flashToggle = false;
                    Toast.makeText(getActivity(), "AUTO FLASH OFF", Toast.LENGTH_SHORT).show();
                } else {
                   // alphaVal = 255;
                    flash[1].setAlpha(255);
                    flashMode.setImageDrawable(flash[0]);
                    flashToggle = true;
                    Toast.makeText(getActivity(), "AUTO FLASH ON", Toast.LENGTH_SHORT).show();
                }
                optionsDelay.post(decay);
                flashMode.invalidate();
                canRun = true;
            }

        });

        torchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canRun = false;
                if (!torchToggle) {
                    torchMode.setImageResource(R.drawable.light_on);
                    previewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                    Toast.makeText(getActivity(), "TORCH ON", Toast.LENGTH_SHORT).show();
                    try {
                        previewSession.setRepeatingRequest(previewBuilder.build(), null, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    torchToggle = true;
                } else {
                    torchMode.setImageResource(R.drawable.light_off);
                    previewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                    Toast.makeText(getActivity(), "TORCH OFF", Toast.LENGTH_SHORT).show();
                    try {
                        previewSession.setRepeatingRequest(previewBuilder.build(), null, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    torchToggle = false;
                }
                optionsDelay.post(decay);
                torchMode.invalidate();
                canRun = true;
            }
        });

        nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canRun = false;
                if (!nightToggle) {
                    autoToggle = false;
                   nightMode.setImageResource(R.drawable.night_on);
                    previewBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_USE_SCENE_MODE);
                    previewBuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CaptureRequest.CONTROL_SCENE_MODE_NIGHT);
                    Toast.makeText(getActivity(), "NIGHT MODE ON / auto mode off", Toast.LENGTH_SHORT).show();
                    try {
                        previewSession.setRepeatingRequest(previewBuilder.build(), null, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    nightToggle = true;
                } else {
                    autoToggle = true;
                    nightMode.setImageResource(R.drawable.night_off);
                    previewBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
                    Toast.makeText(getActivity(), "NIGHT MODE OFF / auto mode on", Toast.LENGTH_SHORT).show();
                    try {
                        previewSession.setRepeatingRequest(previewBuilder.build(), null, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    nightToggle = false;
                }
                optionsDelay.post(decay);
                nightMode.invalidate();
                canRun = true;
            }
        });
        assert textureView != null;
        textureView.setSurfaceTextureListener(surfaceTextureListener);
        textureView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //check if on/off
                        alphaVal = 255;
                        interacting = false;
                        nightCirc.setVals(100);
                        torchCirc.setVals(100);
                        flashCirc.setVals(100);
                        Log.i("alpha values ", String.valueOf(nightCirc.getAlpha()));
                        expBar.setBackgroundColor(Color.argb(100,0,0,0));
                        optionsDelay.removeCallbacksAndMessages(null);
                        if (!nightToggle) {
                            night[1].setAlpha(alphaVal);
                            nightMode.setImageDrawable(night[1]);
                            nightCirc.getLayoutParams().width = nightMode.getWidth();
                            nightCirc.setParams(nightCirc.getLayoutParams().width);
                            nightCirc.setVals(100);

                        } else {
                            night[0].setAlpha(alphaVal);
                            nightMode.setImageDrawable(night[0]);
                            nightCirc.getLayoutParams().width = nightMode.getWidth();
                            nightCirc.setParams(nightCirc.getLayoutParams().width);
                            nightCirc.setVals(100);
                        }

                        if (!torchToggle) {
                            torch[1].setAlpha(alphaVal);
                            torchMode.setImageDrawable(torch[1]);
                            torchCirc.getLayoutParams().width = torchMode.getWidth();
                            torchCirc.setParams(torchCirc.getLayoutParams().width);
                        } else {
                            torch[0].setAlpha(alphaVal);
                            torchMode.setImageDrawable(torch[0]);
                            torchCirc.getLayoutParams().width = torchMode.getWidth();
                            torchCirc.setParams(torchCirc.getLayoutParams().width);
                        }

                        if (!flashToggle) {
                            flash[1].setAlpha(alphaVal);
                            flashMode.setImageDrawable(flash[1]);
                            flashCirc.getLayoutParams().width = flashMode.getWidth();
                            flashCirc.setParams(flashCirc.getLayoutParams().width);
                        } else {
                            flash[0].setAlpha(alphaVal);
                            flashMode.setImageDrawable(flash[0]);
                            flashCirc.getLayoutParams().width = flashMode.getWidth();
                            flashCirc.setParams(flashCirc.getLayoutParams().width);
                        }
                        slideVal = mapRange(event.getX());
                        exposureComp.setText("exposure compensation");
                        exposure.setText("< " + slideVal + " >");
                        if (!pressed) {
                            Log.i("mouse_event", "touched");
                            xVal = event.getX();
                            pressed = true;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (pressed) {
                            if (event.getX() >= xVal) {
                                slideVal = mapRange(event.getX());
                                exposure.setText("< " + slideVal + " >");
                                Log.i("mouse_event", "right" + " > " + String.valueOf(slideVal));
                                if (previewBuilder != null) {
                                    if(!nightToggle) {
                                        previewBuilder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, slideVal);
                                        try {
                                            previewSession.setRepeatingRequest(previewBuilder.build(), null, mBackgroundHandler);
                                        } catch (CameraAccessException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                            } else {
                                if (event.getX() < xVal) {
                                    slideVal = mapRange(event.getX());
                                    exposure.setText("< " + slideVal + " >");
                                    Log.i("mouse_event", "left" + " > " + String.valueOf(slideVal));
                                    if (previewBuilder != null) {
                                        previewBuilder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, slideVal);
                                    }

                                }
                                xVal = event.getX();
                            }
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        if (!interacting) {
                            optionsDelay.post(decay);
                        }
                       /* exposureComp.setText("");
                        exposure.setText("");
                        nightMode.setImageResource(android.R.color.transparent);
                        flashMode.setImageResource(android.R.color.transparent);
                        torchMode.setImageResource(android.R.color.transparent);
                        */
                        if (previewBuilder != null) {
                            if (!nightToggle) {
                                previewBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
                                try {
                                    previewSession.setRepeatingRequest(previewBuilder.build(), null, mBackgroundHandler);
                                } catch (CameraAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Log.i("mouse_event", "UN- touched");
                        xVal = 0;
                        pressed = false;
                        textureView.clearFocus();
                }

                return true;
            }
        });
        takePictureButton = (CamButton) view.findViewById(R.id.btn_takepicture);
        PropertyValuesHolder pvhXscale = PropertyValuesHolder.ofFloat("scaleX",1.075f);
        PropertyValuesHolder pvhYscale = PropertyValuesHolder.ofFloat("scaleY",1.075f);
        PropertyValuesHolder pvhXscaleMinus = PropertyValuesHolder.ofFloat("scaleX",.925f);
        PropertyValuesHolder pvhYscaleMinus = PropertyValuesHolder.ofFloat("scaleY",.925f);
        final ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(takePictureButton,pvhXscale,pvhYscale);
        final ObjectAnimator animatorRev = ObjectAnimator.ofPropertyValuesHolder(takePictureButton,pvhXscaleMinus,pvhYscaleMinus);
        animatorRev.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getPicture();
                MainActivity.viewSwitcher.showNext();
                Log.i("picture Taken", " in theory");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        assert takePictureButton != null;
        takePictureButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        takePictureButton.illum = true;
                        takePictureButton.invalidate();
                        animator.setDuration(100).start();
                        break;
                    case MotionEvent.ACTION_UP:
                        takePictureButton.illum = false;
                        takePictureButton.invalidate();
                        animatorRev.setDuration(200).start();
                        break;
                }
                return true;
            }
        });

        return view;
    }


    void getPicture() {
        if (cameraDevice == null) {
            return;
        }
        final CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        checkParams(manager);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640, height = 480;
            if (jpegSizes != null && jpegSizes.length > 0) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder capturebuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            capturebuilder.addTarget(reader.getSurface());
            if(torchToggle) {
                capturebuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
            }
            if(flashToggle) {
                capturebuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
            }
            if(nightToggle) {
                capturebuilder.set(CaptureRequest.CONTROL_SCENE_MODE, CaptureRequest.CONTROL_SCENE_MODE_NIGHT);
            }
            if(autoToggle) {
                capturebuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            }
            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
            capturebuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            ImageReader.OnImageAvailableListener imageAvailableListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (Exception ee) {
                    } finally {
                        if (image != null)
                            image.close();
                    }
                    Log.i("THIS****", mCurrentThumbPath);
                    // insert photo obj data;
                    new BtmpProcBckgrnd(mCurrentPhotoPath, mCurrentThumbPath, MainActivity.todayObj).execute();
                    getFragmentManager().popBackStack();

                }

                void save(byte[] bytes) throws IOException {
                    File file12 = getOutputMediaFile();
                    createThumbFile();
                    OutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(file12);
                        outputStream.write(bytes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (outputStream != null)
                                outputStream.close();
                        } catch (Exception e) {
                        }
                    }
                }
            };
            HandlerThread handlerThread = new HandlerThread("takepicture");
            handlerThread.start();
            final Handler handler = new Handler(handlerThread.getLooper());
            reader.setOnImageAvailableListener(imageAvailableListener, handler);
            final CameraCaptureSession.CaptureCallback previewSSession = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
                    super.onCaptureStarted(session, request, timestamp, frameNumber);
                }

                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    startCamera();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    try {
                        session.capture(capturebuilder.build(), previewSSession, handler);
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, handler);
        } catch (Exception e) {
        }
    }

    public void openCamera(int width, int height) {
        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            String camerId = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(camerId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            previewsize =  chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    width, height,map.getOutputSizes(SurfaceTexture.class)[0]);
Log.i("preview_size!!!!!!",String.valueOf(previewsize));
            for(int i = 0; i < map.getOutputSizes(SurfaceTexture.class).length; i++) {
                Log.i("mapOutputSizes ", " value of i =  " + String.valueOf(i) + " sizes =  " + map.getOutputSizes(SurfaceTexture.class)[i].toString());
            }
            surfWidth = previewsize.getWidth();
            surfHeight = previewsize.getHeight();
            textureView.setAspectRatio(previewsize.getHeight(),previewsize.getWidth());
            double aspectFactor = (double)previewsize.getHeight()/(double)previewsize.getWidth();
            int tilePlacement = (int)(textureView.getWidth()*aspectFactor);
            int tileSize = (nightCirc.getWidth())*3;
            int tileSpace = ((tilePlacement - tileSize) /3)/4;
            RelativeLayout.LayoutParams paramsN = (RelativeLayout.LayoutParams) nightMode.getLayoutParams();
            RelativeLayout.LayoutParams paramsT = (RelativeLayout.LayoutParams) torchMode.getLayoutParams();
            RelativeLayout.LayoutParams paramsF = (RelativeLayout.LayoutParams) flashMode.getLayoutParams();
            RelativeLayout.LayoutParams shadowN = (RelativeLayout.LayoutParams) nightCirc.getLayoutParams();
            RelativeLayout.LayoutParams shadowT = (RelativeLayout.LayoutParams) torchCirc.getLayoutParams();
            RelativeLayout.LayoutParams shadowF = (RelativeLayout.LayoutParams) flashCirc.getLayoutParams();
            paramsN.setMargins(0,50,tileSpace,10);
            paramsT.setMargins(0,50,0,10);
            paramsF.setMargins(tileSpace,50,0,10);
            shadowN.setMargins(0,50,tileSpace,10);
            shadowT.setMargins(0,50,0,10);
            shadowF.setMargins(tileSpace,50,0,10);
            nightMode.setLayoutParams(paramsN);
            torchMode.setLayoutParams(paramsT);
            flashMode.setLayoutParams(paramsF);
            nightCirc.setLayoutParams(shadowN);
            torchCirc.setLayoutParams(shadowT);
            flashCirc.setLayoutParams(shadowF);
            // span controls to camera preview size
            if(canResize) {
                cameraFrame.getLayoutParams().width = (int) (textureView.getWidth() * aspectFactor);
                canResize = false;
            }
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        1);
                return;
            }

            manager.openCamera(camerId, stateCallback, null);
            cameraId = camerId;
            Log.i("Camera Id ", camerId);
        } catch (Exception e) {
        }

    }

    public void checkParams(CameraManager cameraManager) {

        Range expRange = null;

        try {
            Log.i("test", cameraDevice.getId() + " " + cameraManager.getCameraCharacteristics(cameraDevice.getId()).getKeys());
            expRange = cameraManager.getCameraCharacteristics(cameraDevice.getId()).get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE);
            range = expRange;

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        try {
            Log.i("Cam Params : ", String.valueOf(expRange.getLower()) + " < exposure > " + String.valueOf(expRange.getUpper()) + " " + cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL));
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private int mapRange(float xVal) {
        int expVal;
        float expPre;
        float input_end = surfWidth, output_start = Float.parseFloat(range.getLower().toString()), output_end = Float.parseFloat(range.getUpper().toString());
        expPre = (xVal) * ((output_end - output_start) * 2 / (input_end - 0)) + (output_start);
        expVal = (int) expPre;
        if (expVal < -12) {
            expVal = -12;
        }
        if(expVal > 12){
            expVal = 12;
        }

        return expVal;
    }

    private TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera(width,height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }
    };
    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            startCamera();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
        }

        @Override
        public void onError(CameraDevice camera, int error) {
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (cameraDevice != null) {
            cameraDevice.close();
        }
    }

    void startCamera() {
        if (cameraDevice == null || !textureView.isAvailable() || previewsize == null) {
            return;
        }
        SurfaceTexture texture = textureView.getSurfaceTexture();
        if (texture == null) {
            return;
        }
        texture.setDefaultBufferSize(previewsize.getWidth(), previewsize.getHeight());
        Surface surface = new Surface(texture);
        try {
            previewBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (Exception e) {
        }
        previewBuilder.addTarget(surface);
        try {
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    previewSession = session;
                    getChangedPreview();
                    Log.i("Configured", " Trigger");


                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                }
            }, null);
        } catch (Exception e) {
        }
    }

    void getChangedPreview() {
        if (cameraDevice == null) {
            return;
        }
        previewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        HandlerThread thread = new HandlerThread("changed Preview");
        thread.start();
        Handler handler = new Handler(thread.getLooper());
        try {
            previewSession.setRepeatingRequest(previewBuilder.build(), null, handler);
        } catch (Exception e) {

        }
        if(getActivity() != null) {
            cameraManager = (CameraManager) this.getActivity().getSystemService(Context.CAMERA_SERVICE);
            checkParams(cameraManager);
        }
    }


    private File getOutputMediaFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private File createThumbFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "thumb" + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                bitmapPre = Bitmap.createBitmap(scaledBitmap, 0, scaledBitmap.getHeight() / 2 - 100, scaledBitmap.getWidth(), 175);
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

    public Bitmap toGrayscale(Bitmap bmpOriginal) {
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

    private Runnable decay = new Runnable() {

        @Override
        public void run() {
            interacting = true;
            if (alphaVal <= 0) {
                exposureComp.setText("");
                exposure.setText("");
                nightMode.getDrawable().setAlpha(alphaVal);
                flashMode.getDrawable().setAlpha(alphaVal);
                torchMode.getDrawable().setAlpha(alphaVal);
                interacting = false;
                optionsDelay.removeCallbacksAndMessages(null);
            } else {
                exposureComp.setTextColor(Color.argb(alphaVal, 255, 255, 255));
                exposure.setTextColor(Color.argb(alphaVal, 255, 255, 255));
                nightMode.getDrawable().setAlpha(alphaVal);
                flashMode.getDrawable().setAlpha(alphaVal);
                torchMode.getDrawable().setAlpha(alphaVal);
                if(alphaVal<100){

                    expBar.setBackgroundColor(Color.argb(alphaVal,0,0,0));
                    nightCirc.setVals(alphaVal);
                    torchCirc.setVals(alphaVal);
                    flashCirc.setVals(alphaVal);
                }
                alphaVal -= .01;
                if (canRun) {
                    optionsDelay.post(this);
                } else {
                    optionsDelay.removeCallbacks(null);
                    alphaVal = 255;
                }
            }
        }
    };
    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<Size>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }
    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }
    private void configureTransform(int viewWidth, int viewHeight) {
        Activity activity = getActivity();
        if (null == textureView || null == previewsize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0,viewHeight, viewWidth);
        RectF bufferRect = new RectF(0, 0, previewsize.getHeight(), previewsize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / previewsize.getHeight(),
                    (float) viewWidth / previewsize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        textureView.setTransform(matrix);
    }
}

