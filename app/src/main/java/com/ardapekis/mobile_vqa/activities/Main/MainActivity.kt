package com.ardapekis.mobile_vqa.activities.Main

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.ardapekis.mobile_vqa.R
import com.ardapekis.mobile_vqa.activities.Main.adapters.ImagesRecyclerAdapter
import com.ardapekis.mobile_vqa.models.ImageData

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import com.ardapekis.mobile_vqa.other.Globals
import kotlinx.android.synthetic.main.content_main.*
import android.graphics.Bitmap
import android.content.pm.PackageManager

import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BitmapFactory
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.ardapekis.mobile_vqa.models.HttpHandler
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toolbar
import org.jetbrains.anko.uiThread


/**
 * @author Ilya Golod
 */
class MainActivity : AppCompatActivity() {

    private val LOAD_IMAGE_CODE = 1
    private val IMAGE_CAPTURE_CODE = 2

    private val JPEG_FILE_PREFIX = "Image"
    private lateinit var curImageFile: File

    private lateinit var recycler: RecyclerView
    private lateinit var noItemsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar_main)

        recycler = main_recycler_view
        noItemsText = no_items_text_view

        recycler.layoutManager = LinearLayoutManager(this)

        recycler.adapter = ImagesRecyclerAdapter(this, Globals.imagesData)

        if (Globals.imagesData.size == 0) {
            noItemsText.visibility = View.VISIBLE
        } else {
            noItemsText.visibility = View.INVISIBLE
        }

        fab_existing_image.setOnClickListener { _ -> onImageFabClick() }
        fab_take_picture.setOnClickListener { _ -> onCameraFabClick() }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId

        if (id == R.id.action_refresh) {
            checkImagesProcessed()
        }
        recycler.adapter.notifyDataSetChanged()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            when (requestCode) {
            LOAD_IMAGE_CODE -> {
                if (resultCode == RESULT_OK && data != null) {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                    val image = ImageData(UUID.randomUUID(), bitmap,
                            data.data.path, Calendar.getInstance().time, false)
                    Globals.imagesData.add(image)
                    uploadImage(image)
                }
            }
            IMAGE_CAPTURE_CODE -> {
                try {
                    val bitmap = getImageBitmap(curImageFile)

                    val image = ImageData(UUID.randomUUID(), bitmap,
                            curImageFile.absolutePath, Calendar.getInstance().time, false)
                    Globals.imagesData.add(image)
                    uploadImage(image)
                } catch (e: Exception) {
                        // in case no photo was taken, do nothing
                }
            }
        }
        recycler.adapter = ImagesRecyclerAdapter(this, Globals.imagesData)
        if (Globals.imagesData.size == 0) {
            noItemsText.visibility = View.VISIBLE
        } else {
            noItemsText.visibility = View.INVISIBLE
        }

    }

    private fun onImageFabClick() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, ""), LOAD_IMAGE_CODE)
    }

    private fun onCameraFabClick() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = createImageFile()
        curImageFile = file
        val uri = FileProvider.getUriForFile(this,
                applicationContext.packageName + ".provider", file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA),
                    0)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, IMAGE_CAPTURE_CODE)
        }
    }

    private fun createImageFile(): File {

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = JPEG_FILE_PREFIX + timeStamp + "_"
        val image = File.createTempFile(
                imageFileName,
                ".jpg",
                getExternalFilesDir("my_images")
        )
        return image
    }

    private fun getImageBitmap(file: File): Bitmap {
        val bmOptions = BitmapFactory.Options()
        bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

        return BitmapFactory.decodeFile(file.absolutePath, bmOptions)
    }

    private fun uploadImage(data: ImageData) {

        val context = this
        doAsync {
            val handler = HttpHandler(Globals.serverUrl)
            val resp = handler.postImageData(data)
//            uiThread {
//                val toast = Toast.makeText(context, resp.toString(), Toast.LENGTH_SHORT)
//                toast.show()
//            }
        }
    }

    private fun checkImagesProcessed() {
        val context = this
        val toast = Toast.makeText(context, "Refreshing...", Toast.LENGTH_SHORT)
        toast.show()

        doAsync {
            val handler = HttpHandler(Globals.serverUrl)
            for (image in Globals.imagesData) {
                val result = handler.getProcessed(image.uuid)
                image.processed = result
            }
        }
        recycler.adapter.notifyDataSetChanged()
    }

}
