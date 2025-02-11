package com.engineerfred.ai3

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.engineerfred.ai3.ui.theme.AI3Theme
import org.tensorflow.lite.task.vision.detector.Detection

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AI3Theme {

                var detection by remember {
                    mutableStateOf<DetectionResult?>(null)
                }

                val objectDetectorHelper = remember {
                    ObjectDetectorHelper(context = applicationContext).apply {
                        setupObjectDetector() // Explicitly call it here
                    }
                }

                var isCameraPermissionGranted by remember {
                    mutableStateOf(hasCameraPermission())
                }

                LaunchedEffect(hasCameraPermission()) {
                    isCameraPermissionGranted = hasCameraPermission()
                }

                val analyzer = remember {
                    ObjectsImageAnalyzer(
                        objectDetectorHelper = objectDetectorHelper,
                        onResults = {
                            detection = it
                        }
                    )
                }

                val lfCameraController = remember {
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(CameraController.IMAGE_ANALYSIS) //we are using cameraX for image analysis, not taking picture or videos
                        setImageAnalysisAnalyzer(
                            ContextCompat.getMainExecutor(applicationContext),
                            analyzer
                        )
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isCameraPermissionGranted) {
                        CameraScreen(
                            lifecycleCameraController = lfCameraController,
                            modifier = Modifier.padding(innerPadding),
                            detectionResult = detection
                        )
                    } else {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 0)
                    }
                }
            }
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
}