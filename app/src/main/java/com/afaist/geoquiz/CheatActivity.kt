package com.afaist.geoquiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val EXTRA_ANSWER_IS_TRUE = "com.afaist.android.geoquiz.answer_is_true"
const val EXTRA_ANSWER_SHOWN = "com.afaist.android.geoquiz.answer_shown"
private const val KEY_INDEX = "answerIsShown"
private const val TAG = "CheatActivity"

class CheatActivity : AppCompatActivity() {
    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private var answerIsTrue = false
    private var answerIsShown: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)
        Log.d(TAG, "OnCreate, answerIsShown $answerIsShown")
        answerIsShown = savedInstanceState?.getBoolean(KEY_INDEX, false) ?: false
        Log.d(TAG, "Read savedInstanceState answerIsShown $answerIsShown")
        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)

        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            answerIsShown = true

            setAnswerShownResult()
            updateAnswerText()
        }
        updateAnswerText()
        setAnswerShownResult()
    }

    /**
     * Возвращаем результат работы формы
     */
    private fun setAnswerShownResult() {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, answerIsShown)
        }
        setResult(RESULT_OK, data)
    }

    /**
     * Обновляем текст подсказки
     */
    private fun updateAnswerText() {
        val answerText = when {
            !answerIsShown -> R.string.empty
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        answerTextView.setText(answerText)
    }

    /**
     * Сохраняем состояние на случай поворота экрана
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState answerIsShown $answerIsShown")
        outState.putBoolean(KEY_INDEX, answerIsShown)
    }

    /**
     * Создаем объект активити с переданных параметром
     */
    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}