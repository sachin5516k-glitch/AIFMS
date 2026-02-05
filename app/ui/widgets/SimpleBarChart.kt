package com.ai.franchise.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class SimpleBarChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1A237E") // Brand Primary
        style = Paint.Style.FILL
    }
    
    // Data: Label -> Value
    private var dataPoints: Map<String, Float> = emptyMap()

    fun setData(data: Map<String, Float>) {
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
        
        val maxVal = dataPoints.values.maxOrNull() ?: 1f
        val barWidth = availableWidth / dataPoints.size * 0.6f
        val spacing = availableWidth / dataPoints.size * 0.4f

        var currentX = padding

        dataPoints.forEach { (_, value) ->
            val barHeight = (value / maxVal) * availableHeight
            
            val left = currentX
            val top = height - padding - barHeight
            val right = currentX + barWidth
            val bottom = height - padding

            canvas.drawRect(left, top, right, bottom, barPaint)

            currentX += barWidth + spacing
        }
    }
}
