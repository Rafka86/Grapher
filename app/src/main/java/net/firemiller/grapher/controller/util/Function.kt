package net.firemiller.grapher.controller.util

import android.graphics.Color
import net.firemiller.grapher.model.system.GrapherCore
import java.util.*
import kotlin.collections.HashMap

class Function(graphercore: GrapherCore) : Observable() {
  private val gc = graphercore

  private val exp = Expression("")
  var rawExpression = ""
    set (value) {
      exp.Remake(value)
      field = value
      updated()
    }

  private val vals = HashMap<String, Float>()
  var variableName = "x"
    set (value) {
      if (!vals.containsKey(value)) {
        vals.run {
          remove(field)
          put(value, 0.0f)
        }
        field = value
        updated()
      }
    }

  var color = Color.BLUE
    set (value) {
      field = value
      updated()
    }

  var visible = false
    set (value) {
      field = value
      updated()
    }

  init {
    addObserver(gc)
    vals.clear()
    vals.put(variableName, 0.0f)
  }

  fun getValue(x: Float): Float {
    vals.put(variableName, x)
    return exp.Eval(vals)
  }

  private fun updated() {
    setChanged()
    notifyObservers()
  }
}