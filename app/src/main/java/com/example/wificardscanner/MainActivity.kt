package com.example.wificardscanner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.android.synthetic.main.activity_main.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text

class MainActivity : AppCompatActivity() {

    private var allItemListArray = ArrayList<ItemContainer>()
    private lateinit var result: Task<Text>

    companion object {
        private const val CAMERA_PERMISSON_CODE = 1
        private const val CAMERA = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        photoBtn.setOnClickListener {
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
            Toast.makeText(this@MainActivity, "No Image was selected", Toast.LENGTH_SHORT).show()
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
            val thumbNail: Bitmap = data!!.extras!!.get("data") as Bitmap
            imageView.setImageBitmap(thumbNail)
            imageFromBitmap(thumbNail)
        } else {
            Toast.makeText(this@MainActivity, "something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun imageFromBitmap(bitmap: Bitmap) {
        val rotationDegrees = 0
        // [START image_from_bitmap]
        val image = InputImage.fromBitmap(bitmap, rotationDegrees)
        // [END image_from_bitmap]
        detectBtn.setOnClickListener {
            recognizeText(image)
        }
    }

    private fun recognizeText(image: InputImage) {
        // [START get_detector_default]
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        // [END get_detector_default]
        // [START run_detector]
        result = recognizer.process(image).addOnSuccessListener { visionText ->
            txtView.text = visionText.text
            val resultText = visionText.text
            textProcess(resultText)
        }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }
    }
    private fun append(arr: Array<String>, element: String): Array<String> {
        val list: MutableList<String> = arr.toMutableList()
        list.add(element)
        return list.toTypedArray()
    }
    private fun textProcess(resultText: String) {
        txtView.text = resultText
        allItemListArray.clear()
        var listItem: Array<String> = arrayOf()
        for (inBlock in result.result.textBlocks) {
            for (inLine in inBlock.lines) {
                for (element in inLine.elements) {
                    listItem = append(listItem, element.text)
                    //println(listItem.contentToString())
                }
            }
        }
        addElementsToMenus(listItem)
    }
    private fun addElementsToMenus(listItem: Array<String>) {
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(this, R.layout.dropdown_item, listItem)
        val autocompleteTV = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autocompleteTV.setAdapter(arrayAdapter)

        autocompleteTV.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItemText = parent.getItemAtPosition(position)
                testView.text = "selected: "
                Toast.makeText(this@MainActivity, selectedItemText.toString(), Toast.LENGTH_SHORT).show()

            }
        val arrayAdapter2: ArrayAdapter<String> = ArrayAdapter(this, R.layout.dropdown_item, listItem)
        val autocompleteTV2 = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView2)
        autocompleteTV2.setAdapter(arrayAdapter2)

        autocompleteTV2.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItemText2 = parent.getItemAtPosition(position)
                Toast.makeText(this@MainActivity, selectedItemText2.toString(), Toast.LENGTH_SHORT).show()

            }
    }
}





