package com.example.wificardscanner

import android.widget.TextView
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextProcessor {
    private var allItemListArray = ArrayList<ItemContainer>()

    private lateinit var resultBlock: Task<Text>
    var resultText = ""


    fun detector(resultBlock: Task<Text>): Array<String> {
        allItemListArray.clear()
        var listItem: Array<String> = arrayOf()
        // [Iterate through blocks than lines than elements to get list of words]
        for (inBlock in resultBlock.result.textBlocks) {
            for (inLine in inBlock.lines) {
                for (element in inLine.elements) {
                    listItem = append(listItem, element.text)
                }
            }
        }
        return listItem
    }
    // [Get elements=words from array and convert list to mutablelist]
    private fun append(arr: Array<String>, element: String): Array<String> {
        val list: MutableList<String> = arr.toMutableList()
        list.add(element)
        return list.toTypedArray()
    }
    fun imageToText(image: InputImage, txtView: TextView): TextRecognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

}