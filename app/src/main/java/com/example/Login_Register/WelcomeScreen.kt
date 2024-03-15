package com.example.Login_Register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WelcomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)

        val sharedpref=getSharedPreferences("mypre", Context.MODE_PRIVATE)
        val email1=sharedpref.getString("email","")
        val password=sharedpref.getString("password","")
        if(email1 !="" && password !=" ")
        {
            val intent=Intent(this,Home1::class.java)
            startActivity(intent)
            finish()
        }

        val register=findViewById<Button>(R.id.register1)
        register.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)

        }
        val login=findViewById<Button>(R.id.login)
        login.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)

        }
    }
}