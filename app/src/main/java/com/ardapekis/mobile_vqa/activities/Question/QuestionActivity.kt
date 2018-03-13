package com.ardapekis.mobile_vqa.activities.Question

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.telephony.gsm.GsmCellLocation
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.ardapekis.mobile_vqa.R
import com.ardapekis.mobile_vqa.models.HttpHandler
import com.ardapekis.mobile_vqa.models.ImageData
import com.ardapekis.mobile_vqa.other.Globals
import kotlinx.android.synthetic.main.activity_question.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * @author Ilya Golod
 */
class QuestionActivity : AppCompatActivity() {

    lateinit var image: ImageData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        setSupportActionBar(toolbar_question)

        setTitle(R.string.title_activity_question)

        image = Globals.imagesData[intent.getIntExtra("ImageDataIndex", -1)]
        question_image.setImageBitmap(image.bitmap)

        ask_button.isEnabled = false

        ask_button.setOnClickListener {_ -> onAskButtonClick()}
        question_box.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                ask_button.isEnabled = question_box.text.toString() != ""
            }
        })

    }


    private fun onAskButtonClick() {

        val context = this
        val question: String = question_box.text.toString()
        question_box.text.clear()
        doAsync {
            val handler = HttpHandler(Globals.serverUrl)
            val resp = handler.getAnswer(image.uuid, question)
            uiThread {
                val toast = Toast.makeText(context, resp, Toast.LENGTH_SHORT)
                toast.show()
                answer_text.text = resp
            }
        }


    }
}
