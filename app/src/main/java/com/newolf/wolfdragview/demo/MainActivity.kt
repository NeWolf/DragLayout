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
import com.newolf.widgets.DragLayout
import java.util.*

class MainActivity : AppCompatActivity() {
    private var handler: Handler? = null

    companion object {
        const val MSG_WHAT = 0x001
        const val MSG_DELAY = 1000L

    }

    private lateinit var tvInfo: TextView
    private lateinit var ivIcon: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ivIcon = findViewById<ImageView>(R.id.iv_icon)
        ivIcon.setOnClickListener {
            Toast.makeText(applicationContext, "iv", Toast.LENGTH_SHORT).show()
        }
        tvInfo = findViewById(R.id.tv_info)

//        findViewById<DragLayout>(R.id.drag_root).layoutChild(R.id.iv_move, 0, 800)

        updateInfo()
    }

    private fun updateInfo() {
        handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun dispatchMessage(msg: Message) {
                super.dispatchMessage(msg)
                tvInfo.text = UUID.randomUUID().toString()
                ivIcon.visibility = if (ivIcon.visibility == View.GONE) View.VISIBLE else View.GONE
                repeatMsg()
            }
        }
        handler?.sendEmptyMessage(MSG_WHAT)
    }

    var left = true
    private fun repeatMsg() {
        handler?.sendEmptyMessageDelayed(MSG_WHAT, MSG_DELAY)
        left = !left
        var top = Random(System.currentTimeMillis()).nextInt() % 1920
        if (top < 0) {
            top = 0
        }
        findViewById<DragLayout>(R.id.drag_root).layoutChild(
            R.id.iv_move,
            if (left) 0 else 880,
            top
        )
    }

    override fun onResume() {
        super.onResume()
        findViewById<DragLayout>(R.id.drag_root).layoutChild(R.id.iv_move,500,800)
    }


}