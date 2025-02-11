package com.engineerfred.ai3

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class ObjectsImageAnalyzer(
    private val objectDetectorHelper: ObjectDetectorHelper,
    private val onResults: (DetectionResult) -> Unit
): ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {
        val rotationDegrees = image.imageInfo.rotationDegrees
        val bitmap = image.toBitmap()

        val result0  = objectDetectorHelper.detect(bitmap, rotationDegrees)
        onResults(result0)

        image.close()
    }
}