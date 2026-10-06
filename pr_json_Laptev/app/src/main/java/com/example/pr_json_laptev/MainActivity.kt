package com.example.pr_json_laptev

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editPrice: EditText
    private lateinit var editTags: EditText
    private lateinit var editJson: EditText
    private lateinit var btnSerialize: Button
    private lateinit var btnDeserialize: Button
    private lateinit var btnClear: Button
    private lateinit var tvOutput: TextView

    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editName = findViewById(R.id.editName)
        editPrice = findViewById(R.id.editPrice)
        editTags = findViewById(R.id.editTags)
        editJson = findViewById(R.id.editJson)
        btnSerialize = findViewById(R.id.btnSerialize)
        btnDeserialize = findViewById(R.id.btnDeserialize)
        btnClear = findViewById(R.id.btnClear)
        tvOutput = findViewById(R.id.tvOutput)

        btnSerialize.setOnClickListener {
            val name = editName.text.toString().trim()
            val priceStr = editPrice.text.toString().trim()
            val tagsStr = editTags.text.toString().trim()

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_fill_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = priceStr.toDoubleOrNull() ?: 0.0
            val tags = if (tagsStr.isNotEmpty()) {
                tagsStr.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            } else {
                emptyList()
            }

            val product = Product(name, price, tags)
            val json = gson.toJson(product)
            editJson.setText(json)
            Log.d("JSON_APP", json)
            tvOutput.text = json
            Toast.makeText(this, getString(R.string.toast_serialized), Toast.LENGTH_SHORT).show()
        }

        btnDeserialize.setOnClickListener {
            val json = editJson.text.toString().trim()
            if (json.isEmpty()) {
                Toast.makeText(this, getString(R.string.toast_json_empty), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val product = gson.fromJson(json, Product::class.java)
                Log.d("JSON_APP", product.toString())
                tvOutput.text = "name: ${product.name}\nprice: ${product.price}\ntags: ${product.tags.joinToString(", ")}"
                Toast.makeText(this, getString(R.string.toast_deserialized), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("JSON_APP", e.message ?: "")
                tvOutput.text = e.message
                Toast.makeText(this, getString(R.string.toast_error), Toast.LENGTH_SHORT).show()
            }
        }

        btnClear.setOnClickListener {
            editName.setText("")
            editPrice.setText("")
            editTags.setText("")
            editJson.setText("")
            tvOutput.text = getString(R.string.result_default)
        }
    }
}
