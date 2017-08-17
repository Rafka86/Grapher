package net.firemiller.grapher.controller.util.converter

import java.util.*

fun tokenize(str: String): Queue<Token> {
  val expr = ArrayDeque<Token>()
  var startIndex: Int
  var i = 0

  fun isNumber(c: Char): Boolean = c in '0'..'9'

  fun isAlphabet(c: Char): Boolean = c in 'A'..'Z' || c in 'a'..'z'

  fun isOperator(c: Char): Boolean = when (c) {
    '+', '-', '*', '/', '%', '^' -> true
    else -> false
  }

  fun isBracket(c: Char): Boolean = c == '(' || c == ')'

  fun isFunction(string: String): Boolean
      = when (string.toUpperCase(Locale.ENGLISH)) {
    "SIN", "COS", "TAN", "SINH", "COSH", "TANH", "EXP", "LOG" -> true
    else -> false
  }

  fun isConstant(string: String): Boolean
    = when (string.toUpperCase(Locale.ENGLISH)) {
    "PI", "E", "EPS" -> true
    else -> false
  }

  expr.clear()
  while (i < str.length) {
    if (isNumber(str[i])) {
      startIndex = i
      while (i < str.length && (isNumber(str[i]) || str[i] == '.')) i++
      expr.add(Token(TokenType.NUMBER, str.substring(startIndex until i)))
    } else if (isAlphabet(str[i])) {
      startIndex = i
      while (i < str.length && (isNumber(str[i]) || isAlphabet(str[i]))) i++
      val buf = str.substring(startIndex until i)
      if (isFunction(buf)) expr.add(Token(TokenType.FUNCTION, buf))
      else if (isConstant(buf)) expr.add(Token(TokenType.CONSTANT, buf))
      else expr.add(Token(TokenType.VARIABLE, buf))
    } else if (isOperator(str[i])) {
      if ((i == 0 || str[i - 1] == '(') && str[i] == '-') expr.add(Token(TokenType.OPERATOR, "_"))
      else expr.add(Token(TokenType.OPERATOR, str.substring(i until i + 1)))
      i++
    } else if (isBracket(str[i])) {
      expr.add(Token(TokenType.BRACKET, str.substring(i until i + 1)))
      i++
    }
  }

  return expr
}