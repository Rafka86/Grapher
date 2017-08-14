package net.firemiller.grapher.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.text.style.AbsoluteSizeSpan
import android.util.AttributeSet
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import net.firemiller.grapher.model.system.GrapherCore
import net.firemiller.grapher.view.activity.MainActivity
import java.text.DecimalFormat
import java.util.*

class GraphSheet(context: Context?, attrs: AttributeSet?)
  : View(context, attrs), Observer, OnScaleGestureListener {
  private val activity = context as MainActivity
  lateinit var grapherCore: GrapherCore
  private val scaleGestureDetector = ScaleGestureDetector(activity, this)

  var graphResolution = 100
    set(value) {
      xArray = FloatArray(value)
      field = value
    }
  var xArray = FloatArray(graphResolution)

  private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val touchPaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val decimalFormat = DecimalFormat("#.#")

  private var touchStartX: Float = 0.0f
  private var touchStartY: Float = 0.0f

  init {
    textPaint.run {
      style = Paint.Style.FILL_AND_STROKE
      strokeWidth = 1.0f
      textSize = 20.0f
      color = Color.GRAY
    }
    touchPaint.run {
      style = Paint.Style.STROKE
      strokeWidth = 2.0f
      color = Color.HSVToColor(floatArrayOf(0.0f, 0.5f, 1.0f))
      pathEffect = DashPathEffect(floatArrayOf(5.0f, 5.0f), 0.0f)
    }
    decimalFormat.run{
      maximumIntegerDigits = 3
      maximumFractionDigits = 5
    }
  }

  fun drawGrid(canvas: Canvas) {
    val xAxisYPosition = grapherCore.maxY / grapherCore.deltaY
    val yAxisXPosition = -grapherCore.minX / grapherCore.deltaX
    val gridSpanPixel = grapherCore.gridSpan / grapherCore.deltaX

    fun drawXAxis() {
      paint.run {
        strokeWidth = 3.0f
        color = Color.GRAY
      }
      canvas.drawLine(0.0f, xAxisYPosition, width.toFloat(), xAxisYPosition, paint)
    }

    fun drawYAxis() {
      paint.run {
        strokeWidth = 3.0f
        color = Color.GRAY
      }
      canvas.drawLine(yAxisXPosition, 0.0f, yAxisXPosition, height.toFloat(), paint)
    }

    fun drawVerticalGridMinus() {
      var x = yAxisXPosition - gridSpanPixel
      if (x > width) x -= gridSpanPixel * ((x - width) / gridSpanPixel).toInt()
      var number = -grapherCore.gridSpan * ((yAxisXPosition - x) / gridSpanPixel).toInt()
      while (x > 0.0f) {
        canvas.drawLine(x, 0.0f, x, height.toFloat(), paint)
        canvas.drawText(decimalFormat.format(number), x, 20.0f, textPaint)
        canvas.drawText(decimalFormat.format(number), x, height.toFloat(), textPaint)
        x -= gridSpanPixel
        number -= grapherCore.gridSpan
      }
    }

    fun drawVerticalGridPlus() {
      var x = yAxisXPosition + gridSpanPixel
      if (x < 0.0f) x += gridSpanPixel * (-x / gridSpanPixel).toInt()
      var number = grapherCore.gridSpan * ((x - yAxisXPosition) / gridSpanPixel).toInt()
      while (x < width) {
        canvas.drawLine(x, 0.0f, x, height.toFloat(), paint)
        canvas.drawText(decimalFormat.format(number), x, 20.0f, textPaint)
        canvas.drawText(decimalFormat.format(number), x, height.toFloat(), textPaint)
        x += gridSpanPixel
        number += grapherCore.gridSpan
      }
    }

    fun drawHorizontalGridMinus() {
      var y = xAxisYPosition + gridSpanPixel
      if (y < 0.0f) y += gridSpanPixel * (-y / gridSpanPixel).toInt()
      var number = -grapherCore.gridSpan * ((y - xAxisYPosition) / gridSpanPixel).toInt()
      while (y < height) {
        canvas.drawLine(0.0f, y, width.toFloat(), y, paint)
        canvas.drawText(decimalFormat.format(number), 0.0f, y, textPaint)
        canvas.drawText(decimalFormat.format(number), width - 25.0f, y, textPaint)
        y += gridSpanPixel
        number -= grapherCore.gridSpan
      }
    }

    fun drawHorizontalGridPlus() {
      var y = xAxisYPosition - gridSpanPixel
      if (y > height) y -= gridSpanPixel * ((y - height) / gridSpanPixel).toInt()
      var number = grapherCore.gridSpan * ((xAxisYPosition - y) / gridSpanPixel).toInt()
      while (y < height) {
        canvas.drawLine(0.0f, y, width.toFloat(), y, paint)
        canvas.drawText(decimalFormat.format(number), 0.0f, y, textPaint)
        canvas.drawText(decimalFormat.format(number), width - 25.0f, y, textPaint)
        y -= gridSpanPixel
        number += grapherCore.gridSpan
      }
    }

    if (xAxisYPosition in 0.0f..height.toFloat()) drawXAxis()
    if (yAxisXPosition in 0.0f..width.toFloat()) drawYAxis()

    paint.run {
      strokeWidth = 1.0f
      color = Color.GRAY
    }
    if (grapherCore.minX < 0.0f) drawVerticalGridMinus()
    if (grapherCore.maxX > 0.0f) drawVerticalGridPlus()
    if (grapherCore.minY < 0.0f) drawHorizontalGridMinus()
    if (grapherCore.maxY > 0.0f) drawHorizontalGridPlus()
  }

  override fun onScaleEnd(detector: ScaleGestureDetector?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onScale(detector: ScaleGestureDetector?): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun update(o: Observable?, arg: Any?) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}