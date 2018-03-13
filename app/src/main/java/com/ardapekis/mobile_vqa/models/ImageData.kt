package com.ardapekis.mobile_vqa.models

import android.graphics.Bitmap
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date
import java.util.UUID

/**
 * @author Ilya Golod
 */
class ImageData(val uuid: UUID, val bitmap: Bitmap,
                val filename: String?, val datetime: Date,
                var processed: Boolean) {
    var questions: ArrayList<String> = ArrayList()
        private set

    var answers: ArrayList<String> = ArrayList()
        private set

    fun addQA(question: String, answer: String) {
        questions.add(question)
        answers.add(answer)
    }


}