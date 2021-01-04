package com.tracking.smarttrack;

import android.*;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.tracking.constants.Constants;

import java.io.File;


public class MediaActivity extends Activity {

    int REQ_PICK_IMAGE = 101, REQ_CAPTURE_IMAGE = 102;
    Uri fileUri;

    String imageType = "", imagePath = "";
    LinearLayout mediaLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.media_activity);

        Intent i = getIntent();
        imageType = i.getStringExtra("type");

        isStoragePermissionGranted();


        mediaLay = (LinearLayout)findViewById(R.id.mediaLay);
        mediaLay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return false;
            }
        });

    }




    void openGallery(){
        Intent galIntent = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galIntent, REQ_PICK_IMAGE);



    }

    void openCamera(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Constants.getOutputMediaFileUri(Constants.PICK_FROM_CAMERA);
        try {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", new File(fileUri.getPath())) );
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQ_CAPTURE_IMAGE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
        }


    }


    void openMediaCamera(){
        if(imageType.equals("camera")){
            openCamera();
        }else{
            openGallery();
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted Storage");
                requestPermissionForCamera();

                return true;
            } else {
                Log.v("TAG","Permission is revoked Storage");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted Storage");
            openMediaCamera();
            return true;
        }

    }

    public boolean requestPermissionForCamera(){

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted Camera");
                openMediaCamera();

                return true;
            } else {
                Log.v("TAG","Permission is revoked Camera");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted Camera");
            openMediaCamera();
            return true;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {

            case 1:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v("TAG","Permission onRequest: "+permissions[0]+ "grantResults was: "+grantResults[0]);
                    //resume tasks needing this permission
                    requestPermissionForCamera();
                    //	checkPermissionForCamera();
                }else{
                    finish();
                }
                break;


            case 2:
                Log.v("TAG","Permission Granted onRequest ");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v("TAG","Permission: "+permissions[0]+ "grantResults was "+grantResults[0]);
                    //		checkPermissionForCamera();
                    openMediaCamera();
                }else{
                    finish();
                }


                break;

        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQ_PICK_IMAGE) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            fileUri = data.getData();

            if(fileUri != null){
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(fileUri,  filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);

                if (cursor != null) {
                    cursor.close();
                }

                long fileSize = Constants.getFileSizeInKb(imagePath);
                Log.d("fileSize", "fileSize: " + fileSize);

                options.inSampleSize = setCompressedValue(fileSize);

                int rotation_degree = Constants.getExifOrientation(imagePath);

                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
                Matrix matrix = new Matrix();
                matrix.postRotate(rotation_degree);

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);

                imagePath = Constants.SaveRotatedImage(bitmap);
                Log.e("", "===Pick Image: " + imagePath);

                FinishActivity(imagePath);



            }else {
                Constants.showToastMsg(mediaLay, "Image not found in your Sd card", 5000);
                finish();
            }

        } else if (resultCode == RESULT_OK && requestCode == REQ_CAPTURE_IMAGE) {

            if(fileUri != null){

                try {
                    imagePath = fileUri.getPath();
                    BitmapFactory.Options options = new BitmapFactory.Options();

                    long fileSize = Constants.getFileSizeInKb(imagePath);
                    Log.d("fileSize", "fileSize: " + fileSize);

                    options.inSampleSize = setCompressedValue(fileSize);

                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
                    int rotation_degree = Constants.getExifOrientation(imagePath);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(rotation_degree);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                    imagePath = Constants.SaveRotatedImage(bitmap);
                    Log.e("", "===Captured Image: " + imagePath );

                    // delete actual captured image
                    Constants.DeleteFile(new File(fileUri.getPath()));

                    // pass rotated final image
                    FinishActivity(imagePath);


                } catch (Exception e) {
                    Constants.showToastMsg(mediaLay, "" + e.toString(), 5000);
                    e.printStackTrace();
                    finish();
                }

            }
        }else{
            finish();
        }


    }


    // reduce image quality
    private int setCompressedValue(long fileSize){
        int compressVal = 0;
        if(fileSize > 600 && fileSize < 1024){
            compressVal = 2;
        }else if(fileSize > 1024 && fileSize < 1700){
            compressVal = 3;
        }else if(fileSize > 1700 && fileSize < 2500){
            compressVal = 4;
        }else if(fileSize > 2500){
            compressVal = 5;
        }
        return compressVal;
    }


    void FinishActivity(String image){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", image);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }


}
