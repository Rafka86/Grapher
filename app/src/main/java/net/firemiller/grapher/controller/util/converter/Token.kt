package net.firemiller.grapher.controller.util.converter

data class Token(private val identity: TokenType, private val buffer: String) {
  val ID: TokenType = identity
  val Str: String = buffer
  val Priority: Int = when (buffer) {
    "+", "-" -> 2
    "*", "/", "%", "_" -> 1
    "^" -> 0
    else -> Int.MAX_VALUE
  }
  val LeftCat: Boolean = buffer != "^" && buffer != "_"
}