package com.example.testing

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_second.*
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.graphics.Bitmap
import java.nio.file.Files.delete
import java.nio.file.Files.exists
import android.os.Environment.getExternalStorageDirectory




class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        setSupportActionBar(toolbar)




        val selectedImgUri = intent.data
        val selectedImgPath = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = contentResolver.query(
            selectedImgUri!!,
            selectedImgPath, null, null, null
        )
        cursor!!.moveToFirst()

        val indexCol = cursor.getColumnIndex(selectedImgPath[0])
        val imgPath = cursor.getString(indexCol)
        cursor.close()

        /* Getting Image from Camera from Main Activity */
        val CapturedImgUri = intent.getParcelableExtra<Uri>("CapturedImgUri")
        Log.e("URI", "" + CapturedImgUri)


    }



}
