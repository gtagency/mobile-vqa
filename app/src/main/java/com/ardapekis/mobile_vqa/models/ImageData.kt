package com.ardapekis.mobile_vqa.models

import android.graphics.Bitmap
import java.util.Date
import java.util.UUID


/**
 * @author Ilya Golod
 */
class ImageData(val uuid: UUID, val bitmap: Bitmap, val datetime: Date) {


    var questions: ArrayList<String> = ArrayList()
        private set

    var answers: ArrayList<String> = ArrayList()
        private set

    fun addQA(question: String, answer: String) {
        questions.add(question)
        answers.add(answer)
    }

}