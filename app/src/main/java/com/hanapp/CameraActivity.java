package com.hanapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.text.TextRecognizer;
import com.hanapp.camera.CameraSource;
import com.hanapp.camera.CameraSourcePreview;
import com.hanapp.ocr.GraphicOverlay;
import com.hanapp.ocr.OcrDetectorProcessor;
import com.hanapp.ocr.OcrGraphic;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "OCR-reader";
    public static final String OCRObject = "OCR";

    private static final int RC_HANDLE_GMS = 9001; //update play services
    private static final int RC_HANDLE_CAMERA_PERM = 2; //permission request codes
    private Switch flash_switch; // For Flash
    boolean autoFocus = true; // set always to true
    boolean useFlash = false;
    Boolean is_preview = false;

    private CameraSource mCameraSource;
    private CameraSourcePreview mCameraPreview;
    private GraphicOverlay<OcrGraphic> mGraphicOverlay;

    private ImageButton captureBtn;
    private ImageButton sendBtn;
    Dialog promptDialog;

    Bitmap bitmap = null;

    // helper objects for detecting taps and pinches.
    private ScaleGestureDetector scaleGestureDetector;

    public static Camera cam = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mCameraPreview = (CameraSourcePreview) findViewById(R.id.texture_preview);
        mGraphicOverlay = (GraphicOverlay<OcrGraphic>) findViewById(R.id.graphic_overlay);

        flash_switch = (Switch) findViewById(R.id.use_flash);
        flash_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                useFlash = isChecked;
                if (isChecked) {
                    mCameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                } else {
                    mCameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                }
            }
        });

        int req_prm = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (req_prm == PackageManager.PERMISSION_GRANTED) {
            createCameraSource(autoFocus, useFlash);
        } else {
            requestCameraPermission();
        }

        captureBtn = (ImageButton) findViewById(R.id.capture_button);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    private void takePicture(){

        is_preview = true;
        try {
            mCameraSource.takePicture(null, new CameraSource.PictureCallback() {

                @Override
                public void onPictureTaken(byte[] bytes) {
                try {
                    mCameraSource.release();

                    bitmap = BitmapFactory.decodeByteArray(bytes ,0, bytes.length);

                    captureBtn.setVisibility(View.GONE);
                    sendBtn = (ImageButton) findViewById(R.id.send_image);
                    sendBtn.setVisibility(View.VISIBLE);

                    sendBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent data = new Intent(CameraActivity.this, ItemListActivity.class);
                            //data.putExtra(OCRObject, bitmap);
                            startActivity(data);
                            finish();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
                }

            });
        } catch (Exception ex){
        }
    }

    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        findViewById(R.id.cam_container).setOnClickListener(listener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean zoom = scaleGestureDetector.onTouchEvent(e);
        return zoom || super.onTouchEvent(e);
    }

    @SuppressLint("InlinedApi")
    private void createCameraSource(boolean autoFocus, boolean useFlash) {
        Context context = getApplicationContext();

        TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        textRecognizer.setProcessor(new OcrDetectorProcessor(mGraphicOverlay));

        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies are not yet available.");
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show();
                Log.w(TAG, getString(R.string.low_storage_error));
            }
        }

        // Open Camera
        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1920)
                .setRequestedFps(15.0f);

        // Auto Focus
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            builder = builder.setFocusMode(
                    autoFocus ? Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE : null);
        }

        // Use Flash
        mCameraSource = builder
                .setFlashMode(useFlash ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraPreview != null) {
            mCameraPreview.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraPreview != null) {
            mCameraPreview.release();
        }
    }

    @Override
    public void onBackPressed(){
        if (is_preview) {
            showPrompt(true);
        } else {
            Intent input_to_home_intent = new Intent(CameraActivity.this, HomeActivity.class);
            startActivity(input_to_home_intent);
            finish();
        }
    }

    public void showPrompt(Boolean prompt_exit) {
        ImageButton cancel;
        ImageButton submit;
        TextView prompt;

        promptDialog = new Dialog(this);
        promptDialog.setContentView(R.layout.prompt);
        prompt = (TextView) promptDialog.findViewById(R.id.prompt_text);
        cancel = (ImageButton) promptDialog.findViewById(R.id.submit_no);
        submit = (ImageButton) promptDialog.findViewById(R.id.submit_yes);

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                promptDialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                promptDialog.dismiss();
                Intent input_to_home_intent = new Intent(CameraActivity.this, HomeActivity.class);
                startActivity(input_to_home_intent);
                finish();
            }
        });

        if(prompt_exit) prompt.setText("Are you sure you want to exit?");
        promptDialog.show();
    }

    // Permission
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            createCameraSource(autoFocus, useFlash);
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    // Starts or Restarts Camera Source
    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mCameraPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }


    private class ScaleListener implements ScaleGestureDetector.OnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }
        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            mCameraSource.doZoom(detector.getScaleFactor());
        }
    }


}
