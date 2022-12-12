package com.afaist.geoquiz

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var oldColors: ColorStateList
    private lateinit var resultLabel: TextView
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0
    private var correctAnswers = 0.0 // Количество правильных ответов
    private var totalAnswers = 0 // Сколько ответов получено всего

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")

        setContentView(R.layout.activity_main)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        resultLabel = findViewById(R.id.result_text_view)

        oldColors = questionTextView.textColors
        trueButton.setOnClickListener {
            checkAnswer(true, it)
        }
        falseButton.setOnClickListener {
            checkAnswer(false, it)
        }
        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
            updateButtons()
        }
        updateQuestion()
        updateButtons()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    /**
     * Check user answer
     */
    private fun checkAnswer(userAnswer: Boolean, view: View) {
        totalAnswers++
        val question = questionBank[currentIndex]
        question.userAnswer = userAnswer
        val correctAnswer = question.answer
        var messageResId = R.string.correct_message
        var color = ContextCompat.getColor(this, R.color.blue)
        if (userAnswer != correctAnswer) {
            messageResId = R.string.incorrect_message
            color = ContextCompat.getColor(this, R.color.red)
        } else {
            correctAnswers++
        }

        val snackBar: Snackbar = Snackbar.make(view, messageResId, Snackbar.LENGTH_SHORT)
        snackBar.setBackgroundTint(color)
        snackBar.show()
        updateButtons()
    }

    private fun updateButtons() {
        val question = questionBank[currentIndex]
        if (question.userAnswer == null) {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            questionTextView.setTextColor(oldColors)

        } else {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            if (question.userAnswer == question.answer) {
                questionTextView.setTextColor(ContextCompat.getColor(this, R.color.blue))
            } else {
                questionTextView.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
        }
        if (totalAnswers == 0) {
            resultLabel.text = getString(R.string.result, 0.0)
        } else {
            resultLabel.text = getString(
                R.string.result,
                (correctAnswers / totalAnswers * 100)
            )
        }
    }
}