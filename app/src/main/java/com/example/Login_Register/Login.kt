package com.example.Login_Register

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
//    private lateinit var googleAuth:GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
//
//        //sign  in with google
//        val signInRequest = BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(getString(R.string.default_web_client_id))
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(true)
//                    .build())
//            .build()

        findViewById<EditText>(R.id.email)
        auth = FirebaseAuth.getInstance()

        val login=findViewById<Button>(R.id.login)

        login.setOnClickListener {
            if (checkForInternet(this)) {

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
            else {
                Toast.makeText(this, "please turn on the internet connection", Toast.LENGTH_SHORT)
                    .show()
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