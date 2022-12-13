package com.afaist.geoquiz

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var oldColors: ColorStateList
    private lateinit var resultLabel: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

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
            quizViewModel.moveToNext()
            updateQuestion()
            updateButtons()
        }
        updateQuestion()
        updateButtons()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
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
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    /**
     * Check user answer
     */
    private fun checkAnswer(userAnswer: Boolean, view: View) {
        quizViewModel.userAnswer = userAnswer
        val correctAnswer = quizViewModel.currentQuestionAnswer
        var messageResId = R.string.correct_message
        var color = ContextCompat.getColor(this, R.color.blue)
        if (userAnswer != correctAnswer) {
            messageResId = R.string.incorrect_message
            color = ContextCompat.getColor(this, R.color.red)
        }
        val snackBar: Snackbar = Snackbar.make(view, messageResId, Snackbar.LENGTH_SHORT)
        snackBar.setBackgroundTint(color)
        snackBar.show()
        updateButtons()
    }

    private fun updateButtons() {
        if (quizViewModel.isAnswered) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            questionTextView.setTextColor(
                ContextCompat.getColor(
                    this,
                    if (quizViewModel.isRightAnswer) R.color.blue else R.color.red
                )
            )
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            questionTextView.setTextColor(oldColors)
        }
        resultLabel.text = getString(R.string.result, quizViewModel.percentRightAnswers)
    }
}