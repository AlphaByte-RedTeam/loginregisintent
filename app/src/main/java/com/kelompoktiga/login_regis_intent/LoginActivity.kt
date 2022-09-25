package com.kelompoktiga.login_regis_intent

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnGoogleLogin: Button
    private lateinit var btnLogin: Button
    private lateinit var auth: FirebaseAuth

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        val tvFooter: TextView = findViewById(R.id.txtFooterLogin)

        etEmail = findViewById(R.id.editEmailLogin)
        etPassword = findViewById(R.id.editPasswordLogin)

        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener() {
            email = etEmail.text.toString()
            password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(it, "Email dan Password tidak boleh kosong", Snackbar.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            Snackbar.make(it, "Email atau Password salah", Snackbar.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        val foregroundColorSpan = ForegroundColorSpan(getColor(R.color.pickled_bluewood))
        val boldSpan = StyleSpan(Typeface.BOLD)

        val spannableString = SpannableString(tvFooter.text.toString())
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(drawState: TextPaint) {
                super.updateDrawState(drawState)
                drawState.isUnderlineText = true
            }
        }
        spannableString.setSpan(
            boldSpan,
            23,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            foregroundColorSpan,
            23,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            clickableSpan,
            23,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tvFooter.text = spannableString
        tvFooter.movementMethod = LinkMovementMethod.getInstance()

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            updateUI(currentUser)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnLogin -> {
                email = etEmail.text.toString()
                password = etPassword.text.toString()

                if (isInputValid()) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val toHome = Intent(this, HomeActivity::class.java)
                                startActivity(toHome)
                            } else {
                                Snackbar.make(btnLogin, "Login failed", Snackbar.LENGTH_LONG).show()
                            }
                        }
                } else {
                    Snackbar.make(btnLogin, "Input is not valid", Snackbar.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun isInputValid(): Boolean {
        email = etEmail.text.toString()
        password = etPassword.text.toString()

        // TODO: create input validation

        return true
    }
}