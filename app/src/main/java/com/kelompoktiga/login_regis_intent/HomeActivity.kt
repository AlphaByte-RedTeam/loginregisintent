package com.kelompoktiga.login_regis_intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    lateinit var name : String
    private lateinit var user : FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        user = Firebase.auth.currentUser!!

        name = user.displayName!!
        findViewById<TextView>(R.id.txtWelcomeUser).text = "Hi, $name"
    }
}