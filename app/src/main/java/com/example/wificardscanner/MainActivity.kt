package com.example.wificardscanner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text

class MainActivity : AppCompatActivity() {

    private lateinit var resultBlock: Task<Text>
    private val generateQR = GenerateQR()
    private val textProcessor = TextProcessor()
    private var ssid = ""
    private var password = ""
    private var preSharedKey = "WPA2"

    companion object {
        private const val CAMERA_PERMISSON_CODE = 1
        private const val CAMERA = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        photoBtn.setOnClickListener {
            // [Check camera permission and start camera]
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_PERMISSON_CODE
                )
            }
        }
        detectBtn.setOnClickListener {
            Toast.makeText(this@MainActivity, "No Image was selected", Toast.LENGTH_SHORT).show()
        }
        submitButton.setOnClickListener {
            generateQR()
        }
        // [Get list of items from strings.xml and adding array adapter to AutoCompleteTextView]
        val keyLists: Array<String> = resources.getStringArray(R.array.shared_keys)
        val listAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.dropdown_item, keyLists)
        val autocompleteTV3 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView3)
        autocompleteTV3.setAdapter(listAdapter)
        // [Listen for selected item in drop down menu]
        autocompleteTV3.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItemText3 = parent.getItemAtPosition(position)
                preSharedKey = selectedItemText3.toString()
            }

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSON_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA)
            } else {
                Toast.makeText(this@MainActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            // [Get image bitmap and set it to imageView]
            val thumbNail: Bitmap = data!!.extras!!.get("data") as Bitmap
            imageView.setImageBitmap(thumbNail)
            imageFromBitmap(thumbNail)
        } else {
            Toast.makeText(this@MainActivity, "something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun imageFromBitmap(bitmap: Bitmap) {
        val rotationDegrees = 0
        var resultText = String()
        // [START get_detector_default]
        // [END get_detector_default]
        // [START run_detector]
        // [START image_from_bitmap]
        val image = InputImage.fromBitmap(bitmap, rotationDegrees)
        // [END image_from_bitmap]
        val recognizer =  textProcessor.imageToText(image,txtView)
        resultBlock = recognizer.process(image).addOnSuccessListener { visionText ->
            txtView.text = visionText.text
            resultText = visionText.text
        }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }
        detectBtn.setOnClickListener {
            textProcess()
        }
    }
    private fun textProcess() {
        val listItem = textProcessor.detector(resultBlock)
        addElementsToMenus(listItem)
    }
    // [Add ]
    @SuppressLint("SetTextI18n")
    private fun addElementsToMenus(listItem: Array<String>) {
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, R.layout.dropdown_item, listItem)
        val autocompleteTV = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autocompleteTV.setAdapter(arrayAdapter)

        autocompleteTV.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                ssid = parent.getItemAtPosition(position).toString()
                testView.text = "selected: "
                //Toast.makeText(this@MainActivity, selectedItemText.toString(), Toast.LENGTH_SHORT).show()
            }
        val arrayAdapter2: ArrayAdapter<String> = ArrayAdapter(this, R.layout.dropdown_item, listItem)
        val autocompleteTV2 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
        autocompleteTV2.setAdapter(arrayAdapter2)

        autocompleteTV2.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                password = parent.getItemAtPosition(position).toString()

            }
    }
    private fun generateQR () {
        val qrImage = generateQR.generator(preSharedKey,ssid,password)
        imageView.setImageBitmap(qrImage)
    }
}






