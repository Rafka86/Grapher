package net.firemiller.grapher.controller.util.converter

data class Token(val id: TokenType, val string: String) {
  val priority: Int = when (string) {
    "+", "-" -> 2
    "*", "/", "%", "_" -> 1
    "^" -> 0
    else -> Int.MAX_VALUE
  }
  val leftCat: Boolean = string != "^" && string != "_"
}