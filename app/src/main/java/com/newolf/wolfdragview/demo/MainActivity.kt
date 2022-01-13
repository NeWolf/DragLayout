package com.newolf.wolfdragview.demo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.customview.widget.ViewDragHelper
import java.util.*

class MainActivity : AppCompatActivity() {
    private var   handler:Handler? = null
    companion object{
       const val MSG_WHAT  = 0x001
       const val MSG_DELAY  = 1000L

    }
    private lateinit var tvInfo:TextView
    private lateinit var ivIcon:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
ivIcon = findViewById<ImageView>(R.id.iv_icon)
        ivIcon.setOnClickListener {
            Toast.makeText(applicationContext, "iv", Toast.LENGTH_SHORT).show()
        }
        tvInfo = findViewById(R.id.tv_info)

        updateInfo()
    }

    private fun updateInfo() {
        handler = @SuppressLint("HandlerLeak")
        object :Handler(){
            override fun dispatchMessage(msg: Message) {
                super.dispatchMessage(msg)
                tvInfo.text = UUID.randomUUID().toString()
                ivIcon.visibility = if (ivIcon.visibility == View.GONE) View.VISIBLE else View.GONE
                repeatMsg()
            }
        }
        handler?.sendEmptyMessage(MSG_WHAT)
    }

    private fun repeatMsg() {
        handler?.sendEmptyMessageDelayed(MSG_WHAT,MSG_DELAY)
    }


}