package com.ai.franchise.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SimpleLineChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1A237E") // Brand Primary
        strokeWidth = 8f
        style = Paint.Style.STROKE
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#009688") // Brand Secondary
        style = Paint.Style.FILL
    }

    private var dataPoints: List<Float> = emptyList()

    fun setData(data: List<Float>) {
        this.dataPoints = data
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (dataPoints.isEmpty()) return

        val width = width.toFloat()
        val height = height.toFloat()
        val padding = 50f
        
        val availableWidth = width - (2 * padding)
        val availableHeight = height - (2 * padding)
        
        val maxVal = dataPoints.maxOrNull() ?: 1f
        val stepX = availableWidth / (dataPoints.size - 1).coerceAtLeast(1)

        var startX = padding
        var startY = height - padding - ((dataPoints[0] / maxVal) * availableHeight)

        // Draw points first to prepare path (Path logic simplified to lines for robustness)
        for (i in 1 until dataPoints.size) {
            val endX = padding + (i * stepX)
            val endY = height - padding - ((dataPoints[i] / maxVal) * availableHeight)
            
            canvas.drawLine(startX, startY, endX, endY, linePaint)
            
            startX = endX
            startY = endY
        }

        // Draw dots
        for (i in dataPoints.indices) {
            val cx = padding + (i * stepX)
            val cy = height - padding - ((dataPoints[i] / maxVal) * availableHeight)
            canvas.drawCircle(cx, cy, 12f, dotPaint)
        }
    }
}
