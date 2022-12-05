package com.afaist.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        trueButton.setOnClickListener {
            val snackBar = Snackbar.make(it, R.string.correct_message, Snackbar.LENGTH_SHORT)
            snackBar.setTextColor(ContextCompat.getColor(this, R.color.white))
            snackBar.setBackgroundTint(ContextCompat.getColor(this, R.color.blue))

            snackBar.show()
        }
        falseButton.setOnClickListener {
            val snackBar = Snackbar.make(it, R.string.incorrect_message, Snackbar.LENGTH_SHORT)
            snackBar.setTextColor(ContextCompat.getColor(this, R.color.white))
            snackBar.setBackgroundTint(ContextCompat.getColor(this, R.color.red))
            snackBar.show()
        }
    }
}