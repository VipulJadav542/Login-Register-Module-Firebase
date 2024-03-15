package com.example.vipuljadav

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PlashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.plash)


        val start=findViewById<Button>(R.id.start)
        start.setOnClickListener {
            val intent = Intent(this, WelcomeScreen::class.java)
            startActivity(intent)
            finish()
        }
    }
}

