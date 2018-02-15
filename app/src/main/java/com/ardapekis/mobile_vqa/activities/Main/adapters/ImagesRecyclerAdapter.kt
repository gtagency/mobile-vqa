package com.ardapekis.mobile_vqa.activities.Main.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.ardapekis.mobile_vqa.R
import com.ardapekis.mobile_vqa.activities.Question.QuestionActivity
import com.ardapekis.mobile_vqa.models.ImageData

import java.util.ArrayList

/**
 * @author Ilya Golod
 */
class ImagesRecyclerAdapter(val context: Context, val data: ArrayList<ImageData>)
    : RecyclerView.Adapter<ImagesRecyclerAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView: TextView = v.findViewById(R.id.card_text)
        val imageView: ImageView = v.findViewById(R.id.card_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.image_card_view, parent, false)
        //view.setOnClickListener {_ -> onCardClick()}
        return ViewHolder(view)
    }

    override fun onBindViewHolder(h: ViewHolder?, position: Int) {
        val holder = h as ViewHolder
        val textView = holder.textView
        val imageView = holder.imageView

        val date = data[position].datetime
        textView.text = date.toString()

        imageView.setImageBitmap(data[position].bitmap)
        imageView.setOnClickListener {_ -> onCardClick(position)}
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun onCardClick(position: Int) {
        val intent = Intent(context, QuestionActivity::class.java)
        intent.putExtra("ImageDataIndex", position)
        context.startActivity(intent)
    }
}
