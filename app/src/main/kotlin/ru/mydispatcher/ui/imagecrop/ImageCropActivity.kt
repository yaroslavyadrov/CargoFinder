package ru.mydispatcher.ui.imagecrop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.steelkiwi.cropiwa.config.CropIwaSaveConfig
import kotlinx.android.synthetic.main.activity_crop_image.*
import ru.mydispatcher.R
import java.io.File


class ImageCropActivity : AppCompatActivity() {
    companion object {
        val IMAGE_URI = "extraImageUri"
        fun createStartIntent(context: Context, imageUri: Uri): Intent {
            val intent = Intent(context, ImageCropActivity::class.java)
            intent.putExtra(IMAGE_URI, imageUri)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_image)
        val uri: Uri = intent.getParcelableExtra(IMAGE_URI)
        cropView.setImageUri(uri)
        cropView.setCropSaveCompleteListener { uri ->
            val intent = Intent().apply { putExtra(IMAGE_URI, uri) }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        buttonSelect.setOnClickListener {
            cropView.crop(CropIwaSaveConfig.Builder(Uri.fromFile(File(filesDir, "${System.currentTimeMillis()}.png")))
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                    .setSize(500, 500)
                    .setQuality(99)
                    .build())
        }
        buttonCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}


