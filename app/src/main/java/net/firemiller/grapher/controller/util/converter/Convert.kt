package net.firemiller.grapher.controller.util.converter

import java.util.*

fun convert(expr: Queue<Token>): Queue<Token> {
  val mem = Stack<Token>()
  mem.clear()

  fun BracketOperate(token: Token) {
    if (token.Str == "(") mem.push(token)
    else if (token.Str == ")") {
      while (mem.peek().Str != "(") expr.add(mem.pop())
      mem.pop()
      if (mem.size != 0 && mem.peek().ID == TokenType.FUNCTION) expr.add(mem.pop())
    }
  }

  fun OperatorOperate(token: Token) {
    if (mem.size == 0) mem.push(token)
    else {
      while (mem.size != 0 && mem.peek().ID == TokenType.OPERATOR) {
        if ((token.LeftCat && token.Priority >= mem.peek().Priority) || token.Priority > mem.peek().Priority) expr.add(mem.pop())
        else break
      }
      mem.push(token)
    }
  }

  for (i in 0 until expr.size) {
    val t = expr.poll()
    when (t.ID) {
      TokenType.NUMBER, TokenType.CONSTANT, TokenType.VARIABLE -> expr.add(t)
      TokenType.FUNCTION -> mem.push(t)
      TokenType.BRACKET -> BracketOperate(t)
      TokenType.OPERATOR -> OperatorOperate(t)
    }
  }
  while (mem.size != 0) expr.add(mem.pop())

  return expr
}