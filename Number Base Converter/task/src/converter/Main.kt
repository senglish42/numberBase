package converter

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

val baseLst = listOf("Enter two numbers in format: {source base} {target base} (To quit type /exit) ", "/exit")

fun convLst(lst: List<Int>) = listOf("Enter number in base ${lst[0]} to convert to base ${lst[1]} (To go back type /back) ", "/back")

fun toDec(from: Int, fracPart: String): String {
    var num = BigDecimal.ZERO
    for (i in fracPart.indices) {
        num += fracPart[i].toString().toBigInteger(from).toString(10).toBigDecimal() * from.toBigDecimal().pow(-(i + 1), MathContext.DECIMAL64)
    }
    return num.setScale(5, RoundingMode.CEILING).toString()
}

fun fractional(lst: List<Int>, fracPart: String): String {
    val str = MutableList(5) {"0"}
    var num = (if (lst[0] != 10) toDec(lst[0], fracPart) else "0.$fracPart").toBigDecimal()
    if (num == BigDecimal.ZERO) return str.joinToString("")
    val one = 1.toBigDecimal()
    val div = lst[1].toBigDecimal()
    for(i in 0..4) {
        num *= div
        val scale = num.setScale(0, RoundingMode.DOWN)
        str[i] = scale.toString().toBigInteger(10).toString(lst[1])
        if (num > one) num -= scale
        if (num.remainder(one).toDouble() == 0.0) return str.joinToString("")
    }
    return str.joinToString("")
}

fun conv(i: String, lst: List<Int>) {
    val all = "0123456789abcdefghijklmnopqrstuvwxyz"
    val valid = all.substring(0, lst[0])
    val reg = "([$valid]+)".toRegex()
    if (reg.matches(i)) { println("Conversion result: ${i.toBigInteger(lst[0]).toString(lst[1])}") ; return }
    val regDot = "([$valid.]+)".toRegex()
    if (i[0] == '.' || i[i.lastIndex] == '.') return
    val intPart = i.substringBefore('.')
    val fracPart = i.substringAfter('.')
    if (regDot.matches(i)) println("Conversion result: ${intPart.toBigInteger(lst[0]).toString(lst[1])}.${fractional(lst, fracPart)}")
}

fun run(lst: List<String>) { if (lst.any { it.toIntOrNull() != null }) loop(lst.map { it.toInt() }) }

fun loop(lst: List<Int> = emptyList(), convLst: List<String> = if (lst.isEmpty()) baseLst else convLst(lst)) {
    while (true) {
        print(convLst[0])
        val i = readln()
        if (i == convLst[1]) return
        if (lst.isEmpty()) run(i.split(" ")) else conv(i, lst)
        println()
    }
}

fun main() = loop()