package com.ai.franchise.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class SimplePieChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paints = listOf(
        Color.parseColor("#1A237E"), // Deep Blue
        Color.parseColor("#009688"), // Teal
        Color.parseColor("#B00020"), // Red
        Color.parseColor("#FF8F00")  // Amber
    ).map { color ->
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = color
            style = Paint.Style.FILL
        }
    }

    private var slices: List<Float> = emptyList()
    private val rect = RectF()

    fun setData(data: List<Float>) {
        this.slices = data
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (slices.isEmpty()) return

        val width = width.toFloat()
        val height = height.toFloat()
        val padding = 20f
        
        val minDim = width.coerceAtMost(height) - (2 * padding)
        val cx = width / 2
        val cy = height / 2

        rect.set(cx - minDim/2, cy - minDim/2, cx + minDim/2, cy + minDim/2)

        val total = slices.sum()
        var startAngle = -90f

        slices.forEachIndexed { index, value ->
            val sweepAngle = (value / total) * 360f
            val paint = paints[index % paints.size]
            
            canvas.drawArc(rect, startAngle, sweepAngle, true, paint)
            startAngle += sweepAngle
        }
    }
}
