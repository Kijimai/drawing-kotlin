package jibby.tutorials.letsmakeart2

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentColor: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.dvDrawingView)
        drawingView?.setSizeForBrush(15.toFloat())

        val linearLayoutPaintColors = findViewById<LinearLayout>(R.id.llPaintColors)

        mImageButtonCurrentColor = linearLayoutPaintColors[0] as ImageButton
        mImageButtonCurrentColor!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.palette_selected))


        val btnBrushSize : ImageButton = findViewById(R.id.btnBrushSize)
        btnBrushSize.setOnClickListener {
            showBrushSizeDialog()
        }

    }

    private fun showBrushSizeDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Select brush size: ")
        val smallBtn = brushDialog.findViewById<ImageButton>(R.id.ibSmallBrush)
        smallBtn.setOnClickListener {
            drawingView?.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }

        val medBtn = brushDialog.findViewById<ImageButton>(R.id.ibMediumBrush)
        medBtn.setOnClickListener {
            drawingView?.setSizeForBrush(15.toFloat())
            brushDialog.dismiss()
        }

        val lrgBtn = brushDialog.findViewById<ImageButton>(R.id.ibLargeBrush)
        lrgBtn.setOnClickListener {
            drawingView?.setSizeForBrush(20.toFloat())
            brushDialog.dismiss()
        }

        val xlLrgBtn = brushDialog.findViewById<ImageButton>(R.id.ibXLLargeBrush)
        xlLrgBtn.setOnClickListener {
            drawingView?.setSizeForBrush(30.toFloat())
            brushDialog.dismiss()
        }
        brushDialog.show()
    }

    fun paintClicked(view: View) {
        if(view !== mImageButtonCurrentColor) {
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingView?.setColor(colorTag)

            imageButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.palette_selected))

            mImageButtonCurrentColor?.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.palette_normal))

            // overriding the existing color that was picked with what ImageButton was pressed
            mImageButtonCurrentColor = view
        }
    }
}