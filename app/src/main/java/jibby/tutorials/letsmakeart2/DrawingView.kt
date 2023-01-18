package jibby.tutorials.letsmakeart2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attributes: AttributeSet): View(context, attributes) {

    //    Set up variables' initial values -- nullables prevent crashing
    // A variable of CustomPath inner class to use it further
    private var mDrawPath: CustomPath? = null

    // An instance of the Bitmap
    private var mCanvasBitmap: Bitmap? = null

    // holds the style and color information about how to draw geometries, text and bitmaps.
    private var mDrawPaint: Paint?  = null
    private var mCanvasPaint: Paint? = null

    // holds the stroke/brush size to draw on the canvas.
    private var mBrushSize: Float = 0.toFloat()

    // holds the color for the brush -- black by default
    private var color = Color.BLACK
    private var canvas : Canvas? = null

    private var mPaths = ArrayList<CustomPath>()

    // Runs when the class is created
    init {
        setUpDrawing()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

        /**
         * Draw the specified bitmap, with its top/left corner at (x,y), using the specified paint,
         * transformed by the current matrix.
         *
         *If the bitmap and canvas have different densities, this function will take care of
         * automatically scaling the bitmap to draw at the same density as the canvas.
         *
         * @param bitmap The bitmap to be drawn
         * @param left The position of the left side of the bitmap being drawn
         * @param top The position of the top side of the bitmap being drawn
         * @param paint The paint used to draw the bitmap (may be null)
         */

        for(path in mPaths) {
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas.drawPath(path, mDrawPaint!!)
        }

        if(!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when(event?.action) {
            // actionDown, actionUp, actionMove
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset()
                mDrawPath!!.moveTo(touchX!!, touchY!!)
            }

            MotionEvent.ACTION_MOVE -> {
                mDrawPath?.lineTo(touchX!!, touchY!!)
            }

            MotionEvent.ACTION_UP -> {
                mPaths.add(mDrawPath!!)

                mDrawPath = CustomPath(color, mBrushSize)
            }

            else -> return false
        }

        // Invalidate the whole view, if the view is visible
        invalidate()
        return true
    }

    // Adjusts the brush size for the device and converts size it relative to its screen size
    fun setSizeForBrush(newSize: Float) {
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, newSize, resources.displayMetrics)
        mDrawPaint!!.strokeWidth = mBrushSize
    }

    // internal - Only usable within DrawingView
    internal inner class CustomPath(var color: Int, var brushThickness : Float) : Path()

    private fun setUpDrawing() {
        // Actual initial values for member variables when class is created
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)

        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND

        mCanvasPaint = Paint(Paint.DITHER_FLAG)

        // no longer need default brushSize setting
//        mBrushSize = 15.toFloat()
    }

    //    Overriding function inherited from View
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    fun setColor(newColor: String) {
        color = Color.parseColor(newColor)
        mDrawPaint!!.color = color
    }
}