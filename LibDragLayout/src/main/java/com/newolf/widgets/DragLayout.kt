package com.newolf.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper
import kotlin.math.log


class DragLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    companion object {
        private const val TAG = "Wolf.DragLayout"
    }

    private var isLogEnable = false
    private var mCurrentLeft = 0
    private var mCurrentTop = 0
    private var isAutoAttachEdge = true


    private val mViewDragHelper: ViewDragHelper =
        ViewDragHelper.create(this, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                log("tryCaptureView : child = $child , pointerId = $pointerId")
                val layoutParams = child.layoutParams
                if (layoutParams !is DragLayoutLayoutParam) {
                    return false
                }
                log("tryCaptureView : child = $child , pointerId = $pointerId , isCanDrag = ${layoutParams.isCanDrag}")
                return layoutParams.isCanDrag
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
                if (child.layoutParams !is DragLayoutLayoutParam) {
                    return measuredWidth - child.measuredWidth
                }
                return if ((child.layoutParams as DragLayoutLayoutParam).isHorizontalScrollPriority) 0 else measuredWidth - child.measuredWidth
            }

            override fun getViewVerticalDragRange(child: View): Int {
                if (child.layoutParams !is DragLayoutLayoutParam) {
                    return measuredHeight - child.measuredHeight
                }
                return if ((child.layoutParams as DragLayoutLayoutParam).isVerticalScrollPriority) 0 else measuredWidth - child.measuredWidth
            }

            override fun onViewReleased(releasedChild: View, xVelocity: Float, yVelocity: Float) {
                super.onViewReleased(releasedChild, xVelocity, yVelocity)
                val layoutParams = releasedChild.layoutParams as DragLayoutLayoutParam
                log("onViewReleased : xVelocity=$xVelocity; yVelocity=$yVelocity , isAutoAttachEdge = $isAutoAttachEdge , layoutParams.isAutoAttachEdge = ${layoutParams.isAutoAttachEdge}")
                if (!isAutoAttachEdge || !layoutParams.isAutoAttachEdge) {
                    return
                }
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

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        log("generateLayoutParams attrs")
        return DragLayoutLayoutParam(context, attrs)
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
            Log.d(TAG, msg)
        }
    }

    @SuppressLint("CustomViewStyleable")
    class DragLayoutLayoutParam(context: Context, attrs: AttributeSet?) :
        LayoutParams(context, attrs) {
        var isCanDrag = true
        var isAutoAttachEdge = true
        var isVerticalScrollPriority = false
        var isHorizontalScrollPriority = false

        init {
            val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DragLayout)
            isCanDrag = a.getBoolean(R.styleable.DragLayout_is_can_drag, true)
            isAutoAttachEdge = a.getBoolean(R.styleable.DragLayout_is_auto_attach_edge, true)
            isVerticalScrollPriority =
                a.getBoolean(R.styleable.DragLayout_is_vertical_scroll_priority, false)
            isHorizontalScrollPriority =
                a.getBoolean(R.styleable.DragLayout_is_horizontal_scroll_priority, false)
            Log.d(TAG,
                "isCanDrag = $isCanDrag , " +
                        "isAutoAttachEdge = $isAutoAttachEdge ," +
                        "isVerticalScrollPriority = $isVerticalScrollPriority ," +
                        "isHorizontalScrollPriority = $isHorizontalScrollPriority ")
            a.recycle()
        }
    }
}