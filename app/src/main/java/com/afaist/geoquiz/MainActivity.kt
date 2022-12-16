package com.afaist.geoquiz

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var oldColors: ColorStateList
    private lateinit var resultLabel: TextView
    private lateinit var apiLabel: TextView
    private lateinit var cheatsLabel: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]
    }

    /**
     * Обрабатывает данные, переданные из CheatActivity
     */
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val isCheater = intent?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
                quizViewModel.isCheater = isCheater
                if (isCheater) {
                    quizViewModel.numberOfHints--
                }
            }
            updateButtons()
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        // Восстанавливаем текущий индекс в случае поворота экрана
        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        resultLabel = findViewById(R.id.result_text_view)
        apiLabel = findViewById(R.id.version_text_view)
        apiLabel.text = getString(R.string.api_level, Build.VERSION.SDK_INT)
        cheatsLabel = findViewById(R.id.cheats_text_view)

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
        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startForResult.launch(intent)
        }
        updateQuestion()
        updateButtons()
    }

    /**
     * Сохраняем текущий индекс на случай поворота экрана или перекрытия
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    /**
     * Обновляем текст вопроса
     */
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
        var color = ContextCompat.getColor(this, R.color.blue)

        val messageResId = when {
            quizViewModel.isCheater && quizViewModel.isRightAnswer -> {
                color = ContextCompat.getColor(this, R.color.purple_200)
                R.string.judgment_toast
            }
            userAnswer == correctAnswer -> R.string.correct_message
            else -> {
                color = ContextCompat.getColor(this, R.color.red)
                R.string.incorrect_message
            }
        }
        val snackBar: Snackbar = Snackbar.make(view, messageResId, Snackbar.LENGTH_SHORT)
        snackBar.setBackgroundTint(color)
        snackBar.show()
        updateButtons()
    }

    /**
     * Обновляем интерфейс
     */
    private fun updateButtons() {
        if (quizViewModel.isAnswered) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
            cheatButton.isEnabled = false
            questionTextView.setTextColor(
                ContextCompat.getColor(
                    this,
                    when {
                        !quizViewModel.isRightAnswer -> R.color.red
                        quizViewModel.isCheater -> R.color.purple_200
                        quizViewModel.isRightAnswer -> R.color.blue
                        else -> R.color.black
                    }
                )
            )
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
            cheatButton.isEnabled = quizViewModel.numberOfHints > 0
            questionTextView.setTextColor(oldColors)
        }
        resultLabel.text = getString(R.string.result, quizViewModel.percentRightAnswers)
        cheatsLabel.text = getString(R.string.you_have_cheats, quizViewModel.numberOfHints)
    }
}