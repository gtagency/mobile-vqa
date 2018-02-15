package com.ardapekis.mobile_vqa.activities.Main.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.ardapekis.mobile_vqa.R
import com.ardapekis.mobile_vqa.models.ImageData

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList
import java.util.Date

/**
 * Created by igolo on 14.02.2018.
 */

class ImagesRecyclerAdapter(private val data: ArrayList<ImageData>) : RecyclerView.Adapter<*>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val textView: TextView
        private val imageView: ImageView

        init {
            textView = v.findViewById(R.id.card_text)
            imageView = v.findViewById(R.id.card_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.image_card_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(h: RecyclerView.ViewHolder, position: Int) {
        val holder = h as ViewHolder
        val textView = holder.textView
        val imageView = holder.imageView

        val date = data[position].datetime
        textView.text = date.toString()

        imageView.setImageBitmap(data[position].bitmap)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
