package com.ardapekis.mobile_vqa.activities.Main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.ardapekis.mobile_vqa.R
import com.ardapekis.mobile_vqa.activities.Main.adapters.ImagesRecyclerAdapter
import com.ardapekis.mobile_vqa.models.ImageData

import kotlinx.android.synthetic.main.activity_main.*
import java.util.UUID
import java.util.Calendar
import android.content.Intent
import android.provider.MediaStore
import com.ardapekis.mobile_vqa.other.Globals

/**
 * @author Ilya Golod
 */
class MainActivity : AppCompatActivity() {

    val LOAD_IMAGE_CODE = 1

    private lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById<RecyclerView>(R.id.main_recycler_view)

        recycler.layoutManager = LinearLayoutManager(this)


        recycler.adapter = ImagesRecyclerAdapter(this, Globals.imagesData)


        fab_main.setOnClickListener { _ -> onFabClick() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOAD_IMAGE_CODE && resultCode == RESULT_OK && data != null) {

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
            val image = ImageData(UUID.randomUUID(), bitmap, Calendar.getInstance().time)
            Globals.imagesData.add(image)
            recycler.adapter = ImagesRecyclerAdapter(this, Globals.imagesData)
        }

    }

    fun onFabClick() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, ""), LOAD_IMAGE_CODE)
    }


}
