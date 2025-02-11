package com.engineerfred.ai3

import org.tensorflow.lite.task.vision.detector.Detection

data class DetectionResult(
    val result: MutableList<Detection>?,
    val inferenceTime: Long,
    val imageHeight: Int,
    val imageWidth: Int,
)
