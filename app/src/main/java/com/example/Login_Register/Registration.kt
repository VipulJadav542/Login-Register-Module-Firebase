package com.example.Login_Register

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Registration : AppCompatActivity() {
    private lateinit var register: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_screen)


        register = findViewById(R.id.register1)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        register.setOnClickListener {

            if (checkForInternet(this)) {
                if (checking()) {
                    val fname = findViewById<EditText>(R.id.fname).text.toString()
                    val lname = findViewById<EditText>(R.id.lname).text.toString()
                    val email = findViewById<EditText>(R.id.email).text.toString()
                    val password = findViewById<EditText>(R.id.pwd).text.toString()
                    val user = hashMapOf(
                        "Name" to fname,
                        "Mobile" to lname,
                        "email" to email
                    )
                    val users = db.collection("USERS")
                    users.whereEqualTo("email", email).get()
                        .addOnSuccessListener {
                            if (it.isEmpty) {
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(this) { task ->
                                        if (task.isSuccessful) {
                                            users.document(email).set(user)
                                            Toast.makeText(
                                                this,
                                                "user added successfully , please login",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            val intent = Intent(this, Login::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "authentication failed!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            } else {
                                val intent = Intent(this, Login::class.java)
                                Toast.makeText(
                                    this,
                                    "Already registered ,please login...",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(intent)
                                finish()
                            }
                        }
                } else {
                    Toast.makeText(this, "Enter your details!", Toast.LENGTH_SHORT).show()
                }

            }
            else
            {
                Toast.makeText(this, "please turn on the internet connection", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checking(): Boolean {
        val fname = findViewById<EditText>(R.id.fname)
        val lname = findViewById<EditText>(R.id.lname)
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.pwd)
        if (fname.text.trim { it <= ' ' }.toString().isNotEmpty() && lname.text.trim { it <= ' ' }
                .toString().isNotEmpty() &&
            email.text.toString().trim { it <= ' ' }
                .isNotEmpty() && password.text.trim { it <= ' ' }.toString()
                .trim { it <= ' ' }.isNotEmpty()
        ) {
            return true
        }
        return false
    }


    //for checking internet connection
    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}
