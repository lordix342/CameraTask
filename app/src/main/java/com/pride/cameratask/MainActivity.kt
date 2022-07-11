package com.pride.cameratask

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private lateinit var plauncher: ActivityResultLauncher<String>
    private var checked : MutableState<Boolean> = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cameraExecutor = Executors.newSingleThreadExecutor()
        setContent {
            if (checked.value) {
                CameraView(
                    executor = cameraExecutor,
                    onError = { Log.e("error", "View error: $it") }
                )
            }
        }
        checkPermission()
    }
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA) != 0) {
            permissionListener()
            plauncher.launch(Manifest.permission.CAMERA)
        } else checked.value = true
    }

    private fun permissionListener() {
        plauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it) checkPermission()
        }
    }
}
