package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val formatter = NumberFormat.getInstance(Locale.US)
    private val calCore: CalculatorHandler = CalculatorHandler(this, formatter)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_linear)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    fun onClick(v: View) {
        val id = v.id
        val mainText = findViewById<TextView>(R.id.screen)
        val tempText = findViewById<TextView>(R.id.screen_temp)
        if (mainText.text.isNotEmpty() && mainText.text.matches(Regex(".*[-*+/]$"))) {
            mainText.append(" ")
        }
        when (id) {
            R.id.but_0, R.id.but_1, R.id.but_2, R.id.but_3,
            R.id.but_4, R.id.but_5, R.id.but_6, R.id.but_7,
            R.id.but_8, R.id.but_9 -> calCore.handleNumber(mainText, (v as Button).text.toString().toInt())

            R.id.add, R.id.sub, R.id.mul, R.id.div -> {
                if (calCore.currOp.isNotEmpty() && calCore.operands[1] != "0" && !calCore.inCalculation) {
                    calCore.handleCalculation(mainText, tempText)
                }
                calCore.currOp = (v as Button).text.toString()
                calCore.handleOperator(mainText, tempText)
            }

            R.id.dot -> {
                if (!calCore.operands[calCore.currOperand].contains(".")) {
                    mainText.text = StringBuilder("${formatter.format(calCore.operands[calCore.currOperand].toDouble())}.").toString()
                    calCore.operands[calCore.currOperand] += "."
                }
            }

            R.id.reset_ses -> {
                if (calCore.inCalculation) calCore.reset(mainText, tempText)
                else {
                    calCore.operands[calCore.currOperand] = "0"
                    mainText.text = getString(R.string.but_0)
                }
            }

            R.id.backspace -> {
                if (calCore.inCalculation) {
                    tempText.text = getString(R.string.empty)
                    calCore.currOperand = 0
                    calCore.operands[1] = "0"
                }
                else {
                    calCore.operands[calCore.currOperand] =
                        if (calCore.operands[calCore.currOperand].length > 1) calCore.operands[calCore.currOperand].dropLast(1) else "0"
                    mainText.text = formatter.format(calCore.operands[calCore.currOperand].toDouble())
                }
            }

            R.id.reset -> calCore.reset(mainText, tempText)

            R.id.sign -> {
                if (calCore.operands[calCore.currOperand] != "0") {
                    calCore.operands[calCore.currOperand] = if (calCore.operands[calCore.currOperand].startsWith("-")) {
                        calCore.operands[calCore.currOperand].drop(1)
                    } else {
                        "-" + calCore.operands[calCore.currOperand]
                    }
                    mainText.text = formatter.format(calCore.operands[calCore.currOperand].toDouble())
                }
            }

            R.id.eq -> {
                if (calCore.currOperand == 0) {
                    calCore.currOp = getString(R.string.eq_but)
                    calCore.handleOperator(mainText, tempText)
                } else {
                     calCore.handleCalculation(mainText, tempText)
                }
            }
        }
    }
}