package com.example.calculator

import android.content.Context
import android.widget.TextView
import java.text.NumberFormat

class CalculatorHandler(private val context: Context, private val formatter: NumberFormat) {
    val operands: Array<String> = Array(2) { "0" }
    var currOperand: Int = 0
    var currOp: String = ""
    var inCalculation: Boolean = false

    fun handleNumber(screen: TextView, num: Int) {
        if (operands[currOperand].length >= 16) return
        if (operands[currOperand] == "0") operands[currOperand] = num.toString()
        else operands[currOperand] += num.toString()
        screen.text = formatter.format(operands[currOperand].toDouble())
    }

    fun handleOperator(screen: TextView, screenTemp: TextView) {
        var currTemp = screenTemp.text
        if (inCalculation) {
            currTemp = ""
            currOperand = 0
            operands[1] = "0"
            inCalculation = false
        }
        if (currOperand == 0) {
            screenTemp.text = StringBuilder(
                "${if (currTemp.isEmpty()) formatter.format(operands[currOperand].toDouble())
                else currTemp.dropLast(2).toString()} $currOp"
            ).toString()
            currOperand++
        } else {
            if (operands[currOperand] == "0") {
                screenTemp.text = StringBuilder("${currTemp.dropLast(2)} $currOp").toString()
                screen.text = formatter.format(operands[currOperand - 1].toDouble())
            } else {
                handleCalculation(screen, screenTemp)
            }
        }
        if (screen.text.last() == '.') {
            screen.text = screen.text.dropLast(1)
        }
    }

    fun reset(screen: TextView, screenTemp: TextView) {
        screen.text = context.getString(R.string.but_0)
        screenTemp.text = context.getString(R.string.empty)
        currOperand = 0
        currOp = ""
        operands.fill("0")
    }

    fun handleCalculation(screen: TextView, screenTemp: TextView) {
        if (operands[1] == "0" && currOp == "/") {
            screen.text = context.getString(R.string.error)
        } else {
            val temp: String = operands[0]
            when (currOp) {
                "+" -> {
                    operands[0] = (operands[0].toDouble() + operands[1].toDouble()).toString()
                }

                "-" -> {
                    operands[0] = (operands[0].toDouble() - operands[1].toDouble()).toString()
                }

                "x" -> {
                    operands[0] = (operands[0].toDouble() * operands[1].toDouble()).toString()
                }

                "/" -> {
                    operands[0] = (operands[0].toDouble() / operands[1].toDouble()).toString()
                }
            }
            screen.text = formatter.format(operands[0].toDouble())
            screenTemp.text = StringBuilder(
                "${formatter.format(temp.toDouble())} $currOp ${formatter.format(operands[1].toDouble())} ="
            ).toString()
            inCalculation = true
        }
    }
}