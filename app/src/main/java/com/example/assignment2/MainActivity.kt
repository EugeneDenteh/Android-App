package com.example.assignment2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Locate buttons from the layout
        val btnExplicit: Button = findViewById(R.id.btnExplicit)
        val btnImplicit: Button = findViewById(R.id.btnImplicit)
        val btnViewImage: Button = findViewById(R.id.btnViewImage)

        // Start SecondActivity using an explicit intent
        btnExplicit.setOnClickListener {
            val explicitIntent = Intent(this, SecondActivity::class.java)
            startActivity(explicitIntent)
        }

        // Start SecondActivity using an implicit intent
        btnImplicit.setOnClickListener {
            val implicitIntent = Intent("com.example.assignment2.SECOND_ACTIVITY")
            if (implicitIntent.resolveActivity(packageManager) != null) {
                startActivity(implicitIntent)
            }
        }

        // Start ThirdActivity (Image Capture Activity)
        btnViewImage.setOnClickListener {
            val imageIntent = Intent(this, ThirdActivity::class.java)
            startActivity(imageIntent)
        }
    }
}