package ru.mydispatcher.ui.imagecrop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.steelkiwi.cropiwa.CropIwaView
import com.steelkiwi.cropiwa.config.CropIwaSaveConfig
import ru.mydispatcher.R
import ru.mydispatcher.util.extensions.bindView
import java.io.File


class ImageCropActivity : AppCompatActivity() {

    private val cropView by bindView<CropIwaView>(R.id.cropView)
    private val buttonSelect by bindView<Button>(R.id.buttonSelect)
    private val buttonCancel by bindView<Button>(R.id.buttonCancel)

    private val uri by lazy { intent.getParcelableExtra<Uri>(IMAGE_URI) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_image)
        cropView.setImageUri(uri)
        cropView.setCropSaveCompleteListener { imageUri ->
            val intent = Intent().apply { putExtra(IMAGE_URI, imageUri) }
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

    companion object {
        private val IMAGE_URI = "extraImageUri"
        fun createStartIntent(context: Context, imageUri: Uri): Intent {
            val intent = Intent(context, ImageCropActivity::class.java)
            intent.putExtra(IMAGE_URI, imageUri)
            return intent
        }

        fun getImageUriFromIntent(intent: Intent): Uri = intent.getParcelableExtra(IMAGE_URI)
    }
}


