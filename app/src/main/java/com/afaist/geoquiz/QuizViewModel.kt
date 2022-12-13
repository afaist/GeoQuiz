package com.afaist.geoquiz

import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {
    var currentIndex = 0
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    val currentQuestionAnswer
        get() = questionBank[currentIndex].answer
    val currentQuestionText
        get() = questionBank[currentIndex].textResId
    var userAnswer
        get() = questionBank[currentIndex].userAnswer == true
        set(value) {
            questionBank[currentIndex].userAnswer = value
        }

    /**
     * Count percent right answers
     */
    val percentRightAnswers: Float
        get() {
            var correctAnswers = 0
            for (question in questionBank) {
                if (question.userAnswer != null) {
                    if (question.userAnswer == question.answer) {
                        correctAnswers++
                    }
                }
            }
            return (correctAnswers * 100.0 / questionBank.size).toFloat()
        }

    val isAnswered: Boolean
        get() = questionBank[currentIndex].userAnswer != null

    val isRightAnswer: Boolean
    get() {
        val question = questionBank[currentIndex]
        return question.userAnswer == question.answer
    }
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
}
