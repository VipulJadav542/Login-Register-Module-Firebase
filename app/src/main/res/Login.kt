package com.example.vipuljadav

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        findViewById<EditText>(R.id.email)
        auth = FirebaseAuth.getInstance()

        val login=findViewById<Button>(R.id.login)

        login.setOnClickListener {
            if(checking())
            {
                val email=findViewById<EditText>(R.id.email).text.toString()
                val password=findViewById<EditText>(R.id.pwd).text.toString()
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "login successfully", Toast.LENGTH_SHORT).show()

                            val sharedpref=getSharedPreferences("mypre", Context.MODE_PRIVATE)
                            val editer=sharedpref.edit()
                            editer.putString("email",email)
                            editer.putString("password",password)
                            editer.apply()
                            val intent=Intent(this,Home1::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "wrong details!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else
            {
                Toast.makeText(this,"Enter your details!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checking():Boolean
    {

        val email=findViewById<TextView>(R.id.email)
        val password=findViewById<TextView>(R.id.pwd)
        if(email.text.toString().trim{it<=' '}.isNotEmpty() && password.text.toString().trim{it<=' '}.isNotEmpty())
        {
            return true
        }
        return false
    }
}