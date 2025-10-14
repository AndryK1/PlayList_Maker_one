package com.practicum.playlist_maker_one.ui.CustomViews

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.practicum.playlist_maker_one.R
import kotlin.coroutines.coroutineContext
import kotlin.math.min

class PlaybackButtonView @JvmOverloads constructor(
    context : Context,
    attrs: AttributeSet ?= null,
    @AttrRes defStyleAttr : Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes){

    private var isPlaying : Boolean = false
    private var playBitmap : Bitmap?
    private var pauseBitmap : Bitmap?
    private var isExternallyControlled: Boolean = false
    private var currentButtonStateBitmap : Bitmap? = null
    private val minButtonSize = resources.getDimensionPixelSize(
        R.dimen.minButtonSize
    )

    private var imageRect = RectF(0f,0f,0f,0f)

    init {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try{
                playBitmap = getDrawable(R.styleable.PlaybackButtonView_imagePlaySrc)?.toBitmap()
                pauseBitmap = getDrawable(R.styleable.PlaybackButtonView_imagePauseSrc)?.toBitmap()
            }finally {
                recycle()
            }
        }

        updateSrc()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val contentWidth = when(widthMode){
            MeasureSpec.UNSPECIFIED -> {
                minButtonSize
            }
            MeasureSpec.EXACTLY ->{
                widthSize
            }
            MeasureSpec.AT_MOST ->{
                widthSize
            }
            else -> {error(resources.getString(R.string.buttonErrorW) + widthMode)}
        }

        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val contentHeight = when(heightMode){
            MeasureSpec.UNSPECIFIED ->{
                minButtonSize
            }
            MeasureSpec.EXACTLY->{
                heightSize
            }
            MeasureSpec.AT_MOST->{
                heightSize
            }
            else -> {error(resources.getString(R.string.buttonErrorH) + widthMode)}
        }
        val size = min(contentWidth, contentHeight)

        // Устанавливаем посчитанные размеры
        setMeasuredDimension(size, size)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                return true
            }
            MotionEvent.ACTION_UP->{
                performClick()
                if (!isExternallyControlled) {
                    updateSrc()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun updateSrc(){
        if(isPlaying){
            currentButtonStateBitmap = pauseBitmap
            isPlaying = false
        }else{
            currentButtonStateBitmap = playBitmap
            isPlaying = true
        }

        invalidate()
    }

    fun setButton(play: Boolean){
        isExternallyControlled = true

        if(play){
            currentButtonStateBitmap = playBitmap
            this.isPlaying = true
        }else{
            currentButtonStateBitmap = pauseBitmap
            this.isPlaying = false
        }

        invalidate()

    }

    fun enableInternalControl() {
        isExternallyControlled = false
    }


    override fun performClick(): Boolean {
        super.performClick()

        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f,0f, w.toFloat(),h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        currentButtonStateBitmap?.let {
            canvas.drawBitmap(it, null, imageRect, null)
        }
    }
}