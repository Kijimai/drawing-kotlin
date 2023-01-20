package jibby.tutorials.letsmakeart2

import android.Manifest
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentColor: ImageButton? = null

    val openGalleryLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if(result.resultCode == RESULT_OK && result.data != null) {
            val ivImageBackground: ImageView = findViewById(R.id.ivBackground)
            ivImageBackground.setImageURI(result.data?.data)
        }
    }

    private val requestPermission: ActivityResultLauncher<Array<String>> = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
        permission.entries.forEach {
            val permissionName = it.key
            val isGranted = it.value

            if (isGranted) {
                Toast.makeText(this, "Permission granted for gallery!", Toast.LENGTH_LONG).show()
                val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                openGalleryLauncher.launch(pickIntent)

            } else {
                if (permissionName == Manifest.permission.READ_EXTERNAL_STORAGE) {
                    Toast.makeText(this, "Permission denied for gallery access", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

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

        val btnGallery : ImageButton? = findViewById(R.id.ibImgGallery)
        btnGallery?.setOnClickListener{
            requestStoragePermission()
        }
    }

    private fun requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )) {
            showRationaleDialog("Let's Make Art", "Let's Make Art needs permission to access your External Storage to allow adding background image.")
        } else {
            requestPermission.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            //TODO - Add writing external storage permission

        }
    }

    // Generic display function to display a rationale dialog
    private fun showRationaleDialog(
        title: String,
        message: String
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title).setMessage(message).setPositiveButton("Cancel") {
            dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun showBrushSizeDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Select brush size: ")
        val xsSmallBtn = brushDialog.findViewById<ImageButton>(R.id.ibXsSmallBrush)
        xsSmallBtn.setOnClickListener {
            drawingView?.setSizeForBrush(5.toFloat())
            brushDialog.dismiss()
        }

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