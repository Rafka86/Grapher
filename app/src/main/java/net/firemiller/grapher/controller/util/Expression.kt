package net.firemiller.grapher.controller.util

import net.firemiller.grapher.controller.util.converter.Token
import net.firemiller.grapher.controller.util.converter.TokenType
import net.firemiller.grapher.model.system.excepsions.InvalidExpException
import java.util.*

class Expression(expression: String) {
  private var expr = Convert(Tokenize(expression.replace(" ", "")))
  var ArgumentSize = expr.size

  fun Eval(values: HashMap<String, Float>?): Float {
    val ans = Stack<Float>()

    fun VarOpr(token: Token) {
      ans.push(
          if (values?.containsKey(token.Str) ?: throw InvalidExpException("Missing variables\' data."))
            values[token.Str] else throw InvalidExpException("Missing variables\' data."))
    }

    fun ConstOpr(token: Token) {
      when (token.Str.toUpperCase(Locale.ENGLISH)) {
        "PI" -> ans.push(Math.PI.toFloat())
        "E" -> ans.push(Math.E.toFloat())
        "EPS" -> ans.push(Float.MIN_VALUE)
      }
    }

    fun FunOpr(token: Token) {
      when (token.Str) {
        "sin" -> ans.push(Math.sin(ans.pop().toDouble()).toFloat())
        "cos" -> ans.push(Math.cos(ans.pop().toDouble()).toFloat())
        "tan" -> ans.push(Math.tan(ans.pop().toDouble()).toFloat())
        "Sin" -> ans.push(Math.asin(ans.pop().toDouble()).toFloat())
        "Cos" -> ans.push(Math.acos(ans.pop().toDouble()).toFloat())
        "Tan" -> ans.push(Math.atan(ans.pop().toDouble()).toFloat())
        "exp" -> ans.push(Math.exp(ans.pop().toDouble()).toFloat())
        "log" -> ans.push(Math.log(ans.pop().toDouble()).toFloat())
        "sinh" -> ans.push(Math.sinh(ans.pop().toDouble()).toFloat())
        "cosh" -> ans.push(Math.cosh(ans.pop().toDouble()).toFloat())
        "tanh" -> ans.push(Math.tanh(ans.pop().toDouble()).toFloat())
      }
    }

    fun OprOpr(token: Token) {
      val p = ans.pop()
      when (token.Str) {
        "+" -> ans.push(ans.pop() + p)
        "-" -> ans.push(ans.pop() - p)
        "*" -> ans.push(ans.pop() * p)
        "/" -> ans.push(ans.pop() / p)
        "%" -> ans.push(ans.pop() % p)
        "^" -> ans.push(Math.pow(ans.pop().toDouble(), p.toDouble()).toFloat())
        "_" -> ans.push(-p)
      }
    }

    for (i in 0 until ArgumentSize) {
      val token = expr.poll()
      when (token.ID) {
        TokenType.NUMBER -> ans.push(token.Str.toFloat())
        TokenType.VARIABLE -> try {
          VarOpr(token)
        } catch (e: InvalidExpException) {
          throw e
        }
        TokenType.CONSTANT -> ConstOpr(token)
        TokenType.FUNCTION -> FunOpr(token)
        TokenType.OPERATOR -> OprOpr(token)
        else -> {
        }
      }
      expr.add(token)
    }

    return ans.pop()
  }

  fun Eval() = Eval(null)

  fun Remake(newExp: String) {
    expr = Convert(Tokenize(newExp.replace(" ", "")))
    ArgumentSize = expr.size
  }

  fun isEmpty() = expr.isEmpty()

  override fun toString(): String {
    val sb = StringBuilder()

    for (i in 0 until ArgumentSize) {
      val token = expr.poll()
      sb.run {
        append(token.Str)
        append(if (i != ArgumentSize - 1) " " else "")
      }
      expr.add(token)
    }

    return sb.toString()
  }
}