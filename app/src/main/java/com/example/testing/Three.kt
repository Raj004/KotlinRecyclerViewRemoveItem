package com.example.testing

import android.os.Bundle

import android.graphics.BitmapFactory
import android.content.Intent
import android.app.Activity
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.io.File
import java.io.IOException


class Three : Activity() {
    private var imageView: ImageView? = null
    private var f: File? = null
    // Create directories if needed
    val albumDir: File
        get() {

            val storageDir = File(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                ),
                "BAC/"
            )
            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }

            return storageDir
        }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name

        val imageFileName = albumDir.toString() + "/image.jpg"
        return File(imageFileName)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three)
        this.imageView = this.findViewById(R.id.image) as ImageView
        val photoButton = this.findViewById<View>(R.id.btn) as Button
        photoButton.setOnClickListener {
            fun onClick(v: View) {
                try {
                    f = createImageFile()
                    val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
                    startActivityForResult(cameraIntent, CAMERA_REQUEST)
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }


            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            val photo = BitmapFactory.decodeFile(f!!.getAbsolutePath())
            imageView!!.setImageBitmap(photo)

            val i = Intent(Intent.ACTION_SEND)
            i.type = "message/rfc822"
            i.putExtra(Intent.EXTRA_EMAIL, arrayOf("raul.pop90@gmail.com"))
            i.putExtra(Intent.EXTRA_SUBJECT, "Prima poza")
            i.putExtra(Intent.EXTRA_TEXT, "body of email")

            val uri = Uri.fromFile(f)
            i.putExtra(Intent.EXTRA_STREAM, uri)
            try {
                startActivity(Intent.createChooser(i, "Send mail..."))
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(this@Three, "There are no email clients installed.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    companion object {
        private val CAMERA_REQUEST = 1888
    }
}