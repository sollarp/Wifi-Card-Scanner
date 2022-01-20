package com.example.wificardscanner

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaRecorder.VideoSource.CAMERA
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.wificardscanner.databinding.ActivityMainBinding
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var resultBlock: Task<Text>
    private val generateQR = GenerateQR()
    private val textProcessor = TextProcessor()
    private val autoConnet = AutoConnectToWifi()
    private var ssid = " "
    private var password = " "
    private var preSharedKey = "WPA2"
    private lateinit var layout: View
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root.rootView
        layout = binding.relativeLayout
        setContentView(view)

        photoBtn.setOnClickListener {
            onClickRequestPermission(view)
        }
        submitButton.setOnClickListener {
            getSet()
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
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }
    private fun getSet() {
        val inputTextSSID = findViewById<EditText>(R.id.autoCompleteTextView)
        ssid = inputTextSSID.text.toString()
        val inputTextPass = findViewById<EditText>(R.id.autoCompleteTextView2)
        password = inputTextPass.text.toString()

    }
    private fun onClickRequestPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_INDEFINITE,
                    getString(R.string.ok)
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
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
            textProcess()
        }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
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
        if (ssid==" " || password==" " ) {
            Toast.makeText(this@MainActivity,
                "No SSID or Password was selected",
                Toast.LENGTH_SHORT).show()
        }else {
            val qrImage = generateQR.generator(preSharedKey,ssid,password)
            imageView.setImageBitmap(qrImage)
            val context = applicationContext
            Thread.sleep(2000)
            autoConnet.wifiConnect(ssid, password, context)
        }
    }
}
fun View.showSnackbar(
    view: View,
    msg: String,
    length: Int,
    actionMessage: CharSequence?,
    action: (View) -> Unit,
) {
    val snackbar = Snackbar.make(view, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            action(this)
        }.show()
    } else {
        snackbar.show()
    }
}
object Utils{

    fun showToast(mContext: Context?, message: String?) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }
}





