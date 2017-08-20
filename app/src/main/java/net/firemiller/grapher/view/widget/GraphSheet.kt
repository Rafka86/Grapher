package net.firemiller.grapher.view.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import net.firemiller.grapher.model.system.GrapherCore
import net.firemiller.grapher.view.activity.MainActivity
import java.nio.FloatBuffer
import java.text.DecimalFormat
import java.util.*

class GraphSheet(context: Context?, attrs: AttributeSet?)
  : View(context, attrs), Observer, OnScaleGestureListener {
  private val mActivity = context as MainActivity
  lateinit var grapherCore: GrapherCore
  private val mScaleGestureDetector = ScaleGestureDetector(mActivity, this)
  private var mScaleGesture = false

  var graphResolution = 100
    set(value) {
      mXArray = FloatArray(value)
      field = value
    }
  private var mXArray = FloatArray(graphResolution)

  private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val mTouchPaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private val mDecimalFormat = DecimalFormat("#.#")

  private var mCandidateTouchX: Float = 0.0f
  private var mCandidateTouchY: Float = 0.0f

  private var mTouchX: Float? = null
  private var mTouchY: Float? = null

  private var mTouchViewXOld = 0.0f
  private var mTouchViewYOld = 0.0f

  private val SAME_TOUCH_RANGE = 25.0f
  private val SENSITIVITY = 0.01f

  init {
    mTextPaint.run {
      style = Paint.Style.FILL_AND_STROKE
      strokeWidth = 1.0f
      textSize = 40.0f
      color = Color.GRAY
    }
    mTouchPaint.run {
      style = Paint.Style.FILL_AND_STROKE
      strokeWidth = 2.0f
      textSize = 40.0f
      color = Color.HSVToColor(floatArrayOf(0.0f, 0.5f, 1.0f))
      pathEffect = DashPathEffect(floatArrayOf(5.0f, 5.0f), 0.0f)
    }
    mDecimalFormat.run{
      maximumIntegerDigits = 3
      maximumFractionDigits = 5
    }
  }

  override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)

    fun drawGrid() {
      val xAxisYPosition = grapherCore.maxY / grapherCore.deltaY
      val yAxisXPosition = -grapherCore.minX / grapherCore.deltaX
      val gridSpanPixel = grapherCore.gridSpan / grapherCore.deltaX

      fun drawXAxis() {
        mPaint.run {
          strokeWidth = 3.0f
          color = Color.GRAY
        }
        canvas?.drawLine(0.0f, xAxisYPosition, width.toFloat(), xAxisYPosition, mPaint)
      }

      fun drawYAxis() {
        mPaint.run {
          strokeWidth = 3.0f
          color = Color.GRAY
        }
        canvas?.drawLine(yAxisXPosition, 0.0f, yAxisXPosition, height.toFloat(), mPaint)
      }

      fun drawVerticalGridMinus() {
        var x = yAxisXPosition - gridSpanPixel
        if (x > width) x -= gridSpanPixel * ((x - width) / gridSpanPixel).toInt()
        var number = -grapherCore.gridSpan * ((yAxisXPosition - x) / gridSpanPixel).toInt()
        while (x > 0.0f) {
          canvas?.drawLine(x, 0.0f, x, height.toFloat(), mPaint)
          canvas?.drawText(mDecimalFormat.format(number), x, 20.0f, mTextPaint)
          canvas?.drawText(mDecimalFormat.format(number), x, height.toFloat(), mTextPaint)
          x -= gridSpanPixel
          number -= grapherCore.gridSpan
        }
      }

      fun drawVerticalGridPlus() {
        var x = yAxisXPosition + gridSpanPixel
        if (x < 0.0f) x += gridSpanPixel * (-x / gridSpanPixel).toInt()
        var number = grapherCore.gridSpan * ((x - yAxisXPosition) / gridSpanPixel).toInt()
        while (x < width) {
          canvas?.drawLine(x, 0.0f, x, height.toFloat(), mPaint)
          canvas?.drawText(mDecimalFormat.format(number), x, 20.0f, mTextPaint)
          canvas?.drawText(mDecimalFormat.format(number), x, height.toFloat(), mTextPaint)
          x += gridSpanPixel
          number += grapherCore.gridSpan
        }
      }

      fun drawHorizontalGridMinus() {
        var y = xAxisYPosition + gridSpanPixel
        if (y < 0.0f) y += gridSpanPixel * (-y / gridSpanPixel).toInt()
        var number = -grapherCore.gridSpan * ((y - xAxisYPosition) / gridSpanPixel).toInt()
        while (y < height) {
          canvas?.drawLine(0.0f, y, width.toFloat(), y, mPaint)
          canvas?.drawText(mDecimalFormat.format(number), 0.0f, y, mTextPaint)
          canvas?.drawText(mDecimalFormat.format(number), width - 25.0f, y, mTextPaint)
          y += gridSpanPixel
          number -= grapherCore.gridSpan
        }
      }

      fun drawHorizontalGridPlus() {
        var y = xAxisYPosition - gridSpanPixel
        if (y > height) y -= gridSpanPixel * ((y - height) / gridSpanPixel).toInt()
        var number = grapherCore.gridSpan * ((xAxisYPosition - y) / gridSpanPixel).toInt()
        while (y > 0.0f) {
          canvas?.drawLine(0.0f, y, width.toFloat(), y, mPaint)
          canvas?.drawText(mDecimalFormat.format(number), 0.0f, y, mTextPaint)
          canvas?.drawText(mDecimalFormat.format(number), width - 25.0f, y, mTextPaint)
          y -= gridSpanPixel
          number += grapherCore.gridSpan
        }
      }

      if (xAxisYPosition in 0.0f..height.toFloat()) drawXAxis()
      if (yAxisXPosition in 0.0f..width.toFloat()) drawYAxis()

      mPaint.run {
        strokeWidth = 1.0f
        color = Color.GRAY
      }
      if (grapherCore.minX < 0.0f) drawVerticalGridMinus()
      if (grapherCore.maxX > 0.0f) drawVerticalGridPlus()
      if (grapherCore.minY < 0.0f) drawHorizontalGridMinus()
      if (grapherCore.maxY > 0.0f) drawHorizontalGridPlus()
    }

    fun drawFunctions() {
      val step = grapherCore.sizeX / graphResolution.toFloat()
      val floatBuffer = FloatBuffer.allocate(4 * graphResolution * 10)

      mXArray[0] = grapherCore.minX
      for (i in 1 until mXArray.size) mXArray[i] = mXArray[i - 1] + step
      val xBase = grapherCore.minX
      val yBase = grapherCore.minY + grapherCore.sizeY

      mPaint.strokeWidth = 2.0f
      for (function in grapherCore.functions) {
        if (!function.visible) continue
        val values = function.getValues(mXArray)
        for (j in 1 until graphResolution) {
          floatBuffer.put((mXArray[j - 1] - xBase) / grapherCore.deltaX)
          floatBuffer.put((yBase - values[j - 1]) / grapherCore.deltaY)
          floatBuffer.put((mXArray[j] - xBase) / grapherCore.deltaX)
          floatBuffer.put((yBase - values[j]) / grapherCore.deltaY)
        }
        mPaint.color = function.color
        canvas?.drawLines(floatBuffer.array(), mPaint)
      }
    }

    fun drawTouchLine() {
      if (mTouchX == null || mTouchY == null) return
      val drawX = (mTouchX!! - grapherCore.minX) / grapherCore.deltaX
      val drawY = (grapherCore.minY + grapherCore.sizeY - mTouchY!!) / grapherCore.deltaY

      canvas?.drawLine(0.0f, drawY, width.toFloat(), drawY, mTouchPaint)
      canvas?.drawLine(drawX, 0.0f, drawX, height.toFloat(), mTouchPaint)
      mTouchPaint.strokeWidth = 1.0f
      canvas?.drawText("( " + mDecimalFormat.format(mTouchX!!) + "," + mDecimalFormat.format(mTouchY!!) + " )",
          drawX + 5.0f, drawY - 5.0f, mTouchPaint)
    }

    drawGrid()
    drawFunctions()
    drawTouchLine()
  }

  override fun performClick(): Boolean {
    return super.performClick()
  }

  override fun onTouchEvent(event: MotionEvent?): Boolean {
    super.onTouchEvent(event)
    mScaleGestureDetector.onTouchEvent(event)

    fun checkRange(x: Float, y: Float): Boolean
        = (mCandidateTouchX - x) * (mCandidateTouchX - x) + (mCandidateTouchY - y) * (mCandidateTouchY - y) in 0.0f..SAME_TOUCH_RANGE

    fun transFromViewToPosX(viewX: Float): Float
        = viewX * grapherCore.deltaX + grapherCore.minX

    fun transFromViewToPosY(viewY: Float): Float
        = grapherCore.minY + grapherCore.sizeY - viewY * grapherCore.deltaY

    if (event?.pointerCount == 1) {
      when (event.action) {
        MotionEvent.ACTION_DOWN -> {
          mScaleGesture = false
          mTouchViewXOld = event.x
          mTouchViewYOld = event.y
          mCandidateTouchX = transFromViewToPosX(event.x)
          mCandidateTouchY = transFromViewToPosY(event.y)
        }
        MotionEvent.ACTION_MOVE -> {
          if (mScaleGesture) return true
          grapherCore.addCenter(
              (mTouchViewXOld - event.x) * SENSITIVITY * grapherCore.gridSpan,
              (event.y - mTouchViewYOld) * SENSITIVITY * grapherCore.gridSpan
          )
          mTouchViewXOld = event.x
          mTouchViewYOld = event.y
        }
        MotionEvent.ACTION_UP -> {
          performClick()
          if (mScaleGesture) return true
          grapherCore.addCenter(
              (mTouchViewXOld - event.x) * SENSITIVITY * grapherCore.gridSpan,
              (event.y - mTouchViewYOld) * SENSITIVITY * grapherCore.gridSpan
          )
          mTouchViewXOld = 0.0f
          mTouchViewYOld = 0.0f
          if (checkRange(transFromViewToPosX(event.x), transFromViewToPosY(event.y))) {
            mTouchX = mCandidateTouchX
            mTouchY = mCandidateTouchY
          }
        }
        MotionEvent.ACTION_CANCEL -> {
          mTouchViewXOld = 0.0f
          mTouchViewYOld = 0.0f
        }
      }
    }

    return true
  }

  override fun onScaleEnd(detector: ScaleGestureDetector?) {
    return
  }

  override fun onScale(detector: ScaleGestureDetector?): Boolean {
    grapherCore.multipleSizeScale(1.0f + (1.0f - (detector?.scaleFactor ?: 1.0f)))
    return true
  }

  override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
    mScaleGesture = true
    return true
  }

  override fun update(o: Observable?, arg: Any?) {
    invalidate()
  }
}