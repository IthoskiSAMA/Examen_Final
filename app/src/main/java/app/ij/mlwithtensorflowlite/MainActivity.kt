package app.ij.mlwithtensorflowlite
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import org.json.JSONObject
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

import app.ij.mlwithtensorflowlite.ml.Model


class MainActivity : AppCompatActivity() {
    var result: TextView? = null
    var confidence: TextView? = null
    var imageView: ImageView? = null
    var picture: Button? = null
    var information: Button? = null
    var imageSize = 224
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        result = findViewById(R.id.result)
        confidence = findViewById(R.id.confidence)
        imageView = findViewById(R.id.imageView)
        picture = findViewById(R.id.button)
        information = findViewById(R.id.informacion)
        picture.setOnClickListener(View.OnClickListener {
            // Launch camera if we have permission
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 1)
            } else {
                //Request camera permission if we don't have it.
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        })
        information.setOnClickListener(View.OnClickListener { })
    }

    fun classifyImage(image: Bitmap?) {
        try {
            val model: Model = Model.newInstance(
                applicationContext
            )

            // Creates inputs for reference.
            val inputFeature0: TensorBuffer =
                TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())

            // get 1D array of 224 * 224 pixels in image
            val intValues = IntArray(imageSize * imageSize)
            image!!.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            var pixel = 0
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 255f))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 255f))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs: Model.Outputs = model.process(inputFeature0)
            val outputFeature0: TensorBuffer = outputs.getOutputFeature0AsTensorBuffer()
            val confidences: FloatArray = outputFeature0.getFloatArray()
            // find the index of the class with the biggest confidence.
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            val classes = arrayOf(
                "AR",
                "BE",
                "BR",
                "CO",
                "CR",
                "EC",
                "ES",
                "FR",
                "GB",
                "JP",
                "MX",
                "PT",
                "SE",
                "UY"
            )
            result!!.text = classes[maxPos]
            var s = ""
            for (i in classes.indices) {
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100)
            }
            confidence!!.text = s


            // Releases model resources if no longer used.
            model.close()
        } catch (e: IOException) {
            // TODO Handle the exception
        }
    }

    public override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        @Nullable data: Intent?
    ) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            var image = data!!.extras!!["data"] as Bitmap?
            val dimension = Math.min(image!!.width, image.height)
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
            imageView.setImageBitmap(image)
            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
            classifyImage(image)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}