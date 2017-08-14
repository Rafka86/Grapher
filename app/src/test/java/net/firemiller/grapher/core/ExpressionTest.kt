package net.firemiller.grapher.core

import net.firemiller.grapher.controller.util.Expression
import org.junit.Assert.assertEquals
import org.junit.Test

class ExpressionTest {
  @Test(timeout = 2000)
  fun eval() {
    val expr = Expression("-(3+ 1)*  5-100 / 25 ")
    assertEquals(-(3.0f + 1.0f) * 5.0f - 100.0f / 25.0f, expr.Eval())
    expr.Remake("sin(PI/2)")
    assertEquals(1.0f, expr.Eval())
  }
}