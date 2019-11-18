package com.example.testing

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {



    private var btn: Button? = null
    private var imageview: ImageView? = null
    private val GALLERY = 1
    private val CAMERA = 2
    private val MY_CAMERA_REQUEST_CODE = 100
    private var imageUri: Uri? = null
    private var imageUriTemp: Uri? = null
    private var imageFilePath = ""
    private var imageFilePathTemp = ""
    private var path = ""
    var myAdapter: CustomAdapter? = null
    var placeWorkModel: PlaceWorkModel? = null
    var myList: ArrayList<PlaceWorkModel> = ArrayList()
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var mListner: RemoveClickListner? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn = findViewById<View>(R.id.btn) as Button
        imageview = findViewById<View>(R.id.iv) as ImageView

        val recyclerView = findViewById(R.id.recycler_view) as RecyclerView

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        myAdapter = CustomAdapter(myList, this)

        recyclerView.adapter = myAdapter


        try {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val mImageUriCamera = preferences.getString("imageCamera", null)

            Log.e("one", mImageUriCamera)
            if (mImageUriCamera != null) {
                Glide.with(this).load(mImageUriCamera).into(imageview)

            }
        } catch (e: Exception) {
        }

        btn!!.setOnClickListener { showPictureDialog() }

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            var text = "You selected: "
            text += if (R.id.radioMale == checkedId) "male" else "female"
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }


    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                MY_CAMERA_REQUEST_CODE
            )

        } else {
            val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                    return
                }
                imageUriTemp = FileProvider.getUriForFile(this, "com.orbit.android.provider", photoFile!!)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriTemp)
                startActivityForResult(intent, CAMERA)
            }
        }

    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        imageFilePathTemp = image.getAbsolutePath()
        return image
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        /* if (resultCode == this.RESULT_CANCELED)
         {
         return
         }*/
        if (requestCode == GALLERY) {
            if (data != null) {
                imageUri = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                    val myPath = myStorage(bitmap)
                    Log.e("OYE", myPath.toString())
                    Toast.makeText(this@MainActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
                    /*  val IntentCamera = Intent(this, SecondActivity::class.java)
                      IntentCamera.putExtra("CapturedImgUri", imageUri)
                      startActivity(IntentCamera)*/
                    imageview!!.setImageBitmap(bitmap)


                    val preferences = PreferenceManager.getDefaultSharedPreferences(this)
                    val editor = preferences.edit()

                    editor.putString("imageCamera", myPath)
                    editor.commit()
                    Log.e("Gallery+++1", myPath)


                    val mLog = PlaceWorkModel()
                    mLog.setPhot_url(myPath)
                    myList.add(mLog);


                    //now adding the adapter to recyclerview
                    Log.e("YOU GALLERY", myList.size.toString())

                    recycler_view.adapter = myAdapter
                    myAdapter!!.notifyData(myList);

//                    Log.e("Raj",placeWorkModel!!.getPhot_url())


                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {
            imageUri = imageUriTemp
            imageFilePath = imageFilePathTemp
            try {
                Glide.with(this).load(imageFilePath).into(iv);

                iv!!.setImageURI(Uri.parse(imageFilePath))

            } catch (e: IOException) {
                e.printStackTrace()
//            Toast.makeText(this@MainActivity, "Failed!", Toast.LENGTH_SHORT).show()
            }



            Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()

            val mLog = PlaceWorkModel()
            mLog.setPhot_url(path)
            myList.add(mLog);


            //now adding the adapter to recyclerview
            Log.e("YOU GALLERY", myList.size.toString())

            recycler_view.adapter = myAdapter
            myAdapter!!.notifyData(myList);

        }
    }

    private fun myStorage(bitmap: Bitmap?): String {

        // Initializing a new file
        // The bellow line return a directory in internal storage
        val wrapper = ContextWrapper(applicationContext)

        // Initializing a new file
        // The bellow line return a directory in internal storage
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        // Create a file to save the image
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            val out = FileOutputStream(file)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath
    }

    // 2019-11-05 12:15:53.838 5824-5824/com.example.testing E/OYE: /storage/emulated/0/saved_images/Image-1561.jpg


    companion object {
        private val IMAGE_DIRECTORY = "/demonuts"
    }



}