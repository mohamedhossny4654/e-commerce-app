package com.devahmed.techx.onlineshop.Screens.AddProducts.AddProductUseCase;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GetImageUseCase {

    private Activity context;
    public int CameraREQUEST_CODE = 1;
    public int GalleryREQUEST_CODE = 2;
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE" , Manifest.permission.READ_EXTERNAL_STORAGE };
    private final String[] CAMERA_PERMISSION = new String[]{"android.permission.CAMERA" };
    private final String[] STORAGE_PERMISSION = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE" };

    public GetImageUseCase(Activity context) {
        this.context = context;
    }

    public String getCapturedPhotoPath(Intent data) {
        assert data != null;
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), thumbnail, "Title", null);
        return path;
    }

    public void openCamera(){
        if(CameraPermissionGranted()){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            context.startActivityForResult(intent, CameraREQUEST_CODE);
        }else{
            askForCameraPermission();
            openCamera();
        }
    }


    public void openGallery() {
        if(StoragePermissionGranted()){
            //Open galleryIntent intent and wait for user to pick an image
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            context.startActivityForResult(galleryIntent, GalleryREQUEST_CODE);
        }else{
            askForStoragePermission();
            openGallery();
        }


    }

    public void askForCameraPermission(){
        ActivityCompat.requestPermissions(context, CAMERA_PERMISSION, REQUEST_CODE_PERMISSIONS);
    }
    public void askForStoragePermission(){
        ActivityCompat.requestPermissions(context, STORAGE_PERMISSION, REQUEST_CODE_PERMISSIONS);
    }

    private boolean CameraPermissionGranted(){

            if(ContextCompat.checkSelfPermission(context, CAMERA_PERMISSION[0]) != PackageManager.PERMISSION_GRANTED){
                return false;
            }

        return true;
    }
    private boolean StoragePermissionGranted(){

        if(ContextCompat.checkSelfPermission(context, STORAGE_PERMISSION[0]) != PackageManager.PERMISSION_GRANTED){
            return false;
        }

        return true;
    }


    private boolean allPermissionsGranted(){
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
}
