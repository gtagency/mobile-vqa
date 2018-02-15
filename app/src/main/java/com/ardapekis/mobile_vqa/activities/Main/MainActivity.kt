package com.ardapekis.mobile_vqa.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.ardapekis.mobile_vqa.R
import com.ardapekis.mobile_vqa.adapters.ImagesRecyclerAdapter

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.main_recycler_view)
        recycler.layoutManager = LinearLayoutManager(this)

        val data = arrayListOf<String>("Test1", "Test2", "Test3")
        recycler.adapter = ImagesRecyclerAdapter(data)

        fab_main.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
}
