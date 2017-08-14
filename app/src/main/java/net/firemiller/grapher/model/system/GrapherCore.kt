package net.firemiller.grapher.model.system

import net.firemiller.grapher.controller.util.Function
import java.util.*
import kotlin.collections.ArrayList

class GrapherCore : Observable(), Observer {
  private val centerPlusLimit = 500.0f
  private val centerMinusLimit = -500.0f
  private val sizeMaxLimit = 100.0f
  private val sizeMinLimit = 1e-4f
  private val funcMaxSize = 10

  var centerX = 0.0f
    private set
  var centerY = 0.0f
    private set
  var sizeX = 10.0f
    private set
  var sizeY = 0.0f
    private set
  var maxX = 5.0f
    private set
  var minX = -5.0f
    private set
  var maxY = 0.0f
    private set
  var minY = 0.0f
    private set
  var deltaX = 0.0f
    private set
  var deltaY = 0.0f
    private set
  var gridSpan = 1.0f
    private set

  private var viewWidth: Float = 0.0f
  private var viewHeight: Float = 0.0f
  private var viewRate: Float = 0.0f
  var viewSizeFlag = false
    private set

  val functions = ArrayList<Function>()

  init {
    functions.clear()
  }

  fun addFunction() {
    functions.add(Function(this))
    updated()
  }

  fun removeFunctionAt(index: Int) {
    functions.removeAt(index)
    updated()
  }

  fun hasSpaceOfNewFunction() = functions.size < funcMaxSize

  private fun calculationArguments() {
    sizeY = sizeX * viewRate

    maxX = centerX + sizeX / 2.0f
    minX = centerX - sizeX / 2.0f
    maxY = centerY + sizeY / 2.0f
    minY = centerY - sizeY / 2.0f

    deltaX = sizeX / viewWidth
    deltaY = sizeY / viewHeight
  }

  fun setCenter(x: Float, y: Float) {
    centerX = if (x in centerMinusLimit..centerPlusLimit) x else if (x > 0.0f) centerPlusLimit else centerMinusLimit
    centerY = if (y in centerMinusLimit..centerPlusLimit) y else if (y > 0.0f) centerPlusLimit else centerMinusLimit

    calculationArguments()
    updated()
  }

  fun multipleSizeScale(value: Float) {
    sizeX *= value
    if (sizeX !in sizeMinLimit..sizeMaxLimit)
      sizeX = if (sizeX > sizeMaxLimit) sizeMaxLimit else sizeMinLimit

    if (gridSpan * 2.0f > sizeX) gridSpan /= 10.0f
    else if (gridSpan * 20.0f < sizeX) gridSpan *= 10.0f

    calculationArguments()
    updated()
  }

  fun setViewSize(width: Int, height: Int) {
    viewWidth = width.toFloat()
    viewHeight = height.toFloat()
    viewRate = viewHeight / viewWidth

    calculationArguments()
    viewSizeFlag = true

    updated()
  }

  fun updated() {
    setChanged()
    notifyObservers()
  }

  override fun update(o: Observable?, arg: Any?) {
    updated()
  }
}