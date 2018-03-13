package com.ardapekis.mobile_vqa.activities.Main.adapters

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.ardapekis.mobile_vqa.R
import com.ardapekis.mobile_vqa.activities.Main.MainActivity
import com.ardapekis.mobile_vqa.activities.Question.QuestionActivity
import com.ardapekis.mobile_vqa.models.ImageData
import com.ardapekis.mobile_vqa.other.Globals

import java.util.ArrayList

/**
 * @author Ilya Golod
 */
class ImagesRecyclerAdapter(val context: MainActivity, var data: ArrayList<ImageData>)
    : RecyclerView.Adapter<ImagesRecyclerAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView: TextView = v.findViewById(R.id.card_text)
        val imageView: ImageView = v.findViewById(R.id.card_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.image_card_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(h: ViewHolder?, position: Int) {
        val holder = h as ViewHolder
        val textView = holder.textView
        val imageView = holder.imageView

        //val date = data[position].datetime
        if(data[position].processed) {
            textView.visibility = View.INVISIBLE
        } else {
            textView.visibility = View.VISIBLE
            textView.text = context.getString(R.string.Loading)
        }

        imageView.setImageBitmap(data[position].bitmap)
        imageView.setOnClickListener {_ -> onCardClick(position)}
        imageView.setOnLongClickListener {_ -> onCardLongClick(position)}
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun onCardClick(position: Int) {
        if (data[position].processed) {
            val intent = Intent(context, QuestionActivity::class.java)
            intent.putExtra("ImageDataIndex", position)
            context.startActivity(intent)
        }
    }

    private fun onCardLongClick(position: Int): Boolean{
        val builder = AlertDialog.Builder(context);
        builder.setTitle(R.string.delete_image)
        builder.setPositiveButton(R.string.delete, DialogInterface.OnClickListener {_, _ -> run {
            Globals.imagesData.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, data.size)
            notifyDataSetChanged()

            val noItemsText = context.findViewById<TextView>(R.id.no_items_text_view);
            if (Globals.imagesData.size == 0) {
                 noItemsText.visibility = View.VISIBLE
            } else {
                noItemsText.visibility = View.INVISIBLE
            }

        }})
        builder.setNegativeButton(R.string.cancel, DialogInterface.OnClickListener {_, _ -> })
        val dialog: AlertDialog = builder.create()
        dialog.show()
        return true
    }
}
