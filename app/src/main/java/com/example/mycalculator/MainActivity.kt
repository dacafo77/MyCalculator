package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.lang.NumberFormatException
import java.util.function.BinaryOperator

class MainActivity : AppCompatActivity() {
    private val expressionTextView: TextView by lazy {
        findViewById<TextView>(R.id.expressionTextView)
    }
    private val resultTextView: TextView by lazy {
        findViewById<TextView>(R.id.resultTextView)
    }

    private var isOperator = false
    private var hasisOperator = false
    private var isResultDisplayed = false // 추가 연산

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun buttonClicked(v: View) {
        when (v.id) {
            R.id.bt_0 -> numberButtonClicked("0")
            R.id.bt_1 -> numberButtonClicked("1")
            R.id.bt_2 -> numberButtonClicked("2")
            R.id.bt_3 -> numberButtonClicked("3")
            R.id.bt_4 -> numberButtonClicked("4")
            R.id.bt_5 -> numberButtonClicked("5")
            R.id.bt_6 -> numberButtonClicked("6")
            R.id.bt_7 -> numberButtonClicked("7")
            R.id.bt_8 -> numberButtonClicked("8")
            R.id.bt_9 -> numberButtonClicked("9")
            R.id.bt_plus -> operatorButtonClicked("+")
            R.id.bt_minus -> operatorButtonClicked("-")
            R.id.bt_multi -> operatorButtonClicked("*")
            R.id.bt_divider -> operatorButtonClicked("/")
            R.id.bt_modulo -> operatorButtonClicked("%")
            R.id.bt_dot -> operatorButtonClicked(".")
        }
    }

    private fun numberButtonClicked(number: String) {
        if (isResultDisplayed) {
            expressionTextView.text = ""
            isResultDisplayed = false
        }

        if (isOperator) {
            expressionTextView.append(" ")
        }

        isOperator = false

        val expressionText = expressionTextView.text.split(" ")
        if (expressionText.isNotEmpty() && expressionText.last().length >= 15) {
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        } else if (expressionText.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        expressionTextView.append(number)
    }

    private fun operatorButtonClicked(operator: String) {
        if (expressionTextView.text.isEmpty()) {
            return
        }

        if (isResultDisplayed) {
            expressionTextView.text = resultTextView.text
            resultTextView.text = ""
            isResultDisplayed = false
        }

        when {
            isOperator -> {
                val text = expressionTextView.text.toString()
                expressionTextView.text = text.dropLast(1) + operator
            }

            hasisOperator -> {
                Toast.makeText(this, "연산자는 한번만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            }

            else -> {
                expressionTextView.append(" $operator")
            }
        }

        val ssb = SpannableStringBuilder(expressionTextView.text)
        val color = ContextCompat.getColor(this, R.color.green)
        ssb.setSpan(
            ForegroundColorSpan(color),
            expressionTextView.text.length - 1,
            expressionTextView.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        expressionTextView.text = ssb

        isOperator = true
        hasisOperator = true
    }

    fun resultButtonClicked(v: View) {
        val expressionTexts = expressionTextView.text.split(" ")
        if (expressionTextView.text.isEmpty() || expressionTexts.size == 1) {
            return
        }
        if (expressionTexts.size != 3 && hasisOperator) {
            Toast.makeText(this, "아직 완성되지 않은 수식 입니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val resultText = calculateExpression()

        resultTextView.text = resultText

        isOperator = false
        hasisOperator = false
        isResultDisplayed = true
    }

    private fun calculateExpression(): String {
        val expressionTexts = expressionTextView.text.split(" ")

        if (hasisOperator.not() || expressionTexts.size != 3) {
            return ""
        } else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            return ""
        }
        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val op = expressionTexts[1]

        return when (op) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "*" -> (exp1 * exp2).toString()
            "/" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    fun historyButtonClicked(v: View) {
        // 구현 필요
    }

    fun clearButtonClicked(v: View) {
        expressionTextView.text = ""
        resultTextView.text = ""
        isOperator = false
        hasisOperator = false
        isResultDisplayed = false
    }

    fun String.isNumber(): Boolean {
        return try {
            this.toBigInteger()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
}