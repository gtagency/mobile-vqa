package com.ardapekis.mobile_vqa.activities.Question

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ardapekis.mobile_vqa.R
import com.ardapekis.mobile_vqa.models.ImageData
import com.ardapekis.mobile_vqa.other.Globals
import kotlinx.android.synthetic.main.activity_question.*

/**
 * @author Ilya Golod
 */
class QuestionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        setTitle(R.string.title_activity_question)

        val image = Globals.imagesData[intent.getIntExtra("ImageDataIndex", -1)]
        question_image.setImageBitmap(image.bitmap)
    }
}
