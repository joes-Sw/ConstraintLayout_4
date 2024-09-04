package com.example.constraintlayout

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.constraintlayout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var lastNumeric: Boolean = false
    private var stateError: Boolean = false
    private var lastDot: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNumericOnClcickListener()
        setOperatorOnClickListener()
    }

    private fun setNumericOnClcickListener()
    {
        val buttons = listOf(
            binding.button0, binding.button1, binding.button2, binding.button3, binding.button4,
            binding.button5, binding.button6, binding.button7, binding.button8, binding.button9
        )

        val listener = View.OnClickListener { v ->
            if (stateError) {
                binding.result.text = (v as Button).text
                stateError = false
            } else {
                binding.result.append((v as Button).text)
            }
            lastNumeric = true
        }

        for (button in buttons) {
            button.setOnClickListener(listener)
        }
    }


    private fun setOperatorOnClickListener() {
        val buttons = listOf(
            R.id.buttonplus, R.id.buttonminus, R.id.buttonkali, R.id.buttonslash, R.id.buttonpersen
        )

        val listener = View.OnClickListener { v ->
            if (lastNumeric && !stateError) {
                when (v.id) {
                    R.id.buttonpersen -> {
                        val text = binding.result.text.toString()
                        binding.result.text = (text.toDouble() / 100).toString()
                        lastNumeric = false
                        lastDot = false
                    }
                    else -> {
                        binding.result.append((v as Button).text)
                        lastNumeric = false
                        lastDot = false
                    }
                }
            }
        }

        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener(listener)
        }

        findViewById<Button>(R.id.buttonsamadengan).setOnClickListener {
            onEqual()
        }

        findViewById<Button>(R.id.buttonac).setOnClickListener {
            binding.result.text = ""
            lastNumeric = false
            stateError = false
            lastDot = false
        }

        findViewById<Button>(R.id.buttontitik).setOnClickListener {
            if (lastNumeric && !stateError && !lastDot) {
                binding.result.append(".")
                lastNumeric = false
                lastDot = true
            }
        }
    }

    private fun onEqual() {
        if (lastNumeric && !stateError) {
            val text = binding.result.text.toString()
            try {
                val result = evaluate(text)
                binding.result.text = result.toString()
                lastDot = true
            } catch (e: Exception) {
                binding.result.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }

    private fun evaluate(expression: String): Double {
        // Handle the percentage case separately
        var expr = expression
        if (expr.contains("%")) {
            expr = expr.replace("%", "")
            return expr.toDouble() / 100
        }

        // Evaluate the expression with arithmetic operators
        return when {
            expr.contains("+") -> {
                val parts = expr.split("+")
                evaluate(parts[0]).plus(evaluate(parts[1]))
            }
            expr.contains("-") -> {
                val parts = expr.split("-")
                evaluate(parts[0]).minus(evaluate(parts[1]))
            }
            expr.contains("X") -> {
                val parts = expr.split("X")
                evaluate(parts[0]).times(evaluate(parts[1]))
            }
            expr.contains("/") -> {
                val parts = expr.split("/")
                evaluate(parts[0]).div(evaluate(parts[1]))
            }
            else -> expr.toDouble()
        }
    }




}