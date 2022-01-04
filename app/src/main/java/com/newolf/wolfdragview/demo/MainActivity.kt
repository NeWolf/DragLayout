package com.newolf.wolfdragview.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.customview.widget.ViewDragHelper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ImageView>(R.id.iv_icon).setOnClickListener {
            Toast.makeText(applicationContext, "iv", Toast.LENGTH_SHORT).show()
        }
    }


}