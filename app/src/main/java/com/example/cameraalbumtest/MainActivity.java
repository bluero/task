package com.example.cameraalbumtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.sax.StartElementListener;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;

    private ImageView picture;

    private Uri imageuri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button takePhoto = (Button) findViewById(R.id.take_photo);
        picture = (ImageView) findViewById(R.id.picture);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将照片存在手机SD卡的应用关联缓存目录（SD卡中专门用与存放当前缓存数据的位置）下
                //调用getExternalCacheDir（）方法可以得到应用关联缓存目录
                File outputimage=new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputimage.exists()){
                        outputimage.delete();
                    }
                    outputimage.createNewFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //判断设备的系统版本是否打于7.0
                if(Build.VERSION.SDK_INT>=24){
                    //将File对象转换成uri对象
                    //FileProvider是一种特殊的内容提供器，它使用了和内容提供器类似的机制来对数据保护,可以选择性地将封装过的uri共享给外部，提高安全性
                    imageuri= FileProvider.getUriForFile(MainActivity.this,"com.example.cameraalbumtest.fileprovider",outputimage);
                }else{
                    //将File对象转换成一个封装过uri对象
                    imageuri=Uri.fromFile(outputimage);
                }

                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                //指定图片的输出地址
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageuri);
                startActivityForResult(intent, TAKE_PHOTO);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageuri));
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
}