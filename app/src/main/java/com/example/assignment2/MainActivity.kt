package com.example.assignment2

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
        private const val CUSTOM_PERMISSION = "com.example.assignment2.MSE412"
    }

    private var pendingImplicitIntent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request the custom permission at runtime in onCreate
        requestCustomPermission()

        // Set up click listeners for buttons
        setupButtons()
    }

    private fun setupButtons() {
        // Button for explicit intent
        val btnExplicit: Button = findViewById(R.id.btnExplicit)
        btnExplicit.setOnClickListener {
            pendingImplicitIntent = false
            if (checkPermissionAndLaunchActivity()) {
                startSecondActivity(false)
            }
        }

        // Button for implicit intent
        val btnImplicit: Button = findViewById(R.id.btnImplicit)
        btnImplicit.setOnClickListener {
            pendingImplicitIntent = true
            if (checkPermissionAndLaunchActivity()) {
                startSecondActivity(true)
            }
        }

        // Button for third activity
        val btnViewImage: Button = findViewById(R.id.btnViewImage)
        btnViewImage.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }
    }

    private fun requestCustomPermission() {
        // Check if we already have the permission
        if (ContextCompat.checkSelfPermission(
                this,
                CUSTOM_PERMISSION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(CUSTOM_PERMISSION),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun checkPermissionAndLaunchActivity(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this,
                CUSTOM_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted
            true
        } else {
            // Request permission again
            ActivityCompat.requestPermissions(
                this,
                arrayOf(CUSTOM_PERMISSION),
                PERMISSION_REQUEST_CODE
            )
            false
        }
    }

    private fun startSecondActivity(useImplicit: Boolean) {
        val intent = if (useImplicit) {
            // Create an implicit intent using the action defined in manifest
            Intent("com.example.assignment2.SECOND_ACTIVITY")
        } else {
            // Create an explicit intent using the class
            Intent(this, SecondActivity::class.java)
        }
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
                Toast.makeText(
                    this,
                    "Permission granted to access challenges",
                    Toast.LENGTH_SHORT
                ).show()

                // If we were in the middle of launching an activity when permission was requested
                if (pendingImplicitIntent) {
                    startSecondActivity(true)
                }
            } else {
                // Permission denied
                Toast.makeText(
                    this,
                    "Permission denied. Cannot access challenges.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}