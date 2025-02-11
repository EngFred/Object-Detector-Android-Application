package com.engineerfred.ai3

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas

@SuppressLint("DefaultLocale")
@Composable
fun ObjectDetectionOverlay(
    detectionResult: DetectionResult?,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        detectionResult?.result?.forEach { detection ->
            val box = detection.boundingBox
            val paint = Paint().apply {
                color = android.graphics.Color.RED
                strokeWidth = 5f
                style = Paint.Style.STROKE
            }

            val textPaint = Paint().apply {
                color = android.graphics.Color.RED
                textSize = 40f
                typeface = Typeface.DEFAULT_BOLD
            }

            val left = (box.left / detectionResult.imageWidth) * size.width
            val top = (box.top / detectionResult.imageHeight) * size.height
            val right = (box.right / detectionResult.imageWidth) * size.width
            val bottom = (box.bottom / detectionResult.imageHeight) * size.height

            drawIntoCanvas { canvas ->
                // Draw the bounding box
                canvas.nativeCanvas.drawRect(left, top, right, bottom, paint)

                val label = detection.categories.firstOrNull()?.label ?: "Unknown"
                val score = detection.categories.firstOrNull()?.score ?: 0f
                val percentage = String.format("%.0f%%", score * 100) // Format as percentage

                // Ensure label is drawn *just above* the bounding box
                val textX = left
                val textY = top - 10 // Small offset to keep it above the box
                val adjustedTextY = if (textY < 40) top + 40 else textY // Prevent label from going off-screen

                // Draw label and confidence
                canvas.nativeCanvas.drawText(
                    "$label: $percentage",
                    textX,
                    adjustedTextY,
                    textPaint
                )
            }
        }
    }
}

