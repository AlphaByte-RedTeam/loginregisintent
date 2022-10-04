package com.kelompoktiga.login_regis_intent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    lateinit var name : String
    private lateinit var user : FirebaseUser
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        user = Firebase.auth.currentUser!!

        name = user.displayName!!
        findViewById<TextView>(R.id.txtWelcomeUser).text = "Hi, $name 👋"

        val magicButton: Button = findViewById(R.id.btnMagic)
        val numInput: EditText = findViewById(R.id.editNumber)

        magicButton.setOnClickListener {
            val num = numInput.text.toString().toInt()
            if (num % 2 == 0) {
                findViewById<TextView>(R.id.txtOddEven).text = "Even"
            } else {
                findViewById<TextView>(R.id.txtOddEven).text = "Odd"
            }
        }

        val signOutButton: Button = findViewById(R.id.btnSignOut)
        signOutButton.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        auth.signOut()
        val logoutIntent = Intent(this, LoginActivity::class.java)
        logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(logoutIntent)
    }
}