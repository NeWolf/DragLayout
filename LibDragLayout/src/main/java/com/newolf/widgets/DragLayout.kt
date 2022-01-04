package com.newolf.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper


class DragLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    companion object {
        private const val TAG = "DragLayout"
    }

    private var isLogEnable = false
    private var mCurrentLeft = 0
    private var mCurrentTop = 0
    private var isAutoAttachEdge = true


    private val mViewDragHelper: ViewDragHelper =
        ViewDragHelper.create(this, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                log("tryCaptureView : child = $child , pointerId = $pointerId")
                return true
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                mCurrentLeft = left.coerceAtLeast(paddingLeft)
                    .coerceAtMost(width - child.width - paddingRight)
                log("clampViewPositionHorizontal : child = $child , left = $left, dx = $dx , mCurrentLeft = $mCurrentLeft")
                return mCurrentLeft

            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                mCurrentTop = top.coerceAtLeast(paddingTop)
                    .coerceAtMost(height - child.height - paddingBottom)
                log("clampViewPositionVertical : child = $child , top = $top, dy = $dy , mCurrentTop = $mCurrentTop")
                return mCurrentTop
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                log("getViewHorizontalDragRange : child = $child")
                return measuredWidth - child.measuredWidth
            }

            override fun getViewVerticalDragRange(child: View): Int {
                log("getViewVerticalDragRange : child = $child")
                return measuredHeight - child.measuredHeight
            }

            override fun onViewReleased(releasedChild: View, xVelocity: Float, yVelocity: Float) {
                super.onViewReleased(releasedChild, xVelocity, yVelocity)
                if (!isAutoAttachEdge){
                    return
                }
                log("onViewReleased : xVelocity=$xVelocity; yVelocity=$yVelocity")
                val childWidth = releasedChild.width
                val parentWidth = width
                val leftBound = paddingLeft // 左边缘
                val rightBound = width - releasedChild.width - paddingRight // 右边缘
                // 方块的中点超过 ViewGroup 的中点时，滑动到左边缘，否则滑动到右边缘
                if (childWidth / 2 + mCurrentLeft < parentWidth / 2) {
                    settleCapturedViewAt(leftBound, mCurrentTop)
                } else {
                    settleCapturedViewAt(rightBound, mCurrentTop)
                }
                invalidate()
            }

        })

    private fun settleCapturedViewAt(left: Int, top: Int) {
        mViewDragHelper.settleCapturedViewAt(left, top)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return mViewDragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mViewDragHelper.continueSettling(true)) {
            invalidate()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mViewDragHelper.processTouchEvent(event)
        return true
    }

    fun setIsAutoAttachEdge(isAuto: Boolean): DragLayout {
        this.isAutoAttachEdge = isAuto
        return this
    }

    fun isAutoAttachEdge(): Boolean {
        return isAutoAttachEdge
    }

    fun setLogEnable(enable: Boolean): DragLayout {
        isLogEnable = enable
        return this
    }


    private fun log(msg: String) {
        if (isLogEnable) {
//            Log.d(TAG, msg)
        }
    }
}