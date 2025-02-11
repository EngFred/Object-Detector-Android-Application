package com.engineerfred.ai3

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraScreen(
    lifecycleCameraController: LifecycleCameraController,
    modifier: Modifier = Modifier,
    detectionResult: DetectionResult?
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PreviewView(it).apply {
                    this.controller = lifecycleCameraController
                    lifecycleCameraController.bindToLifecycle(lifeCycleOwner)
                }
            }
        )

        ObjectDetectionOverlay(
            detectionResult = detectionResult,
            modifier = Modifier.fillMaxSize()
        )
    }
}