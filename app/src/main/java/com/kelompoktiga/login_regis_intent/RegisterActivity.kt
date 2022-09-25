package com.kelompoktiga.login_regis_intent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var email : String
    private lateinit var name : String
    private lateinit var password : String

    private lateinit var etEmail : EditText
    private lateinit var etName : EditText
    private lateinit var etPassword : EditText

    private lateinit var btnRegister : Button
    private lateinit var btnGoogleRegis : Button

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etEmail = findViewById(R.id.editEmailRegis)
        etName = findViewById(R.id.editNameRegis)
        etPassword = findViewById(R.id.editPasswordRegis)

        val tvFooter: TextView = findViewById(R.id.txtFooterRegis)
        btnRegister = findViewById(R.id.btnRegister)
        btnGoogleRegis = findViewById(R.id.btnGoogleRegis)

        btnRegister.setOnClickListener(this)

        val foregroundColorSpan = ForegroundColorSpan(getColor(R.color.pickled_bluewood))
        val boldSpan = StyleSpan(android.graphics.Typeface.BOLD)

        val spannableString = SpannableString(tvFooter.text.toString())
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            override fun updateDrawState(drawState: TextPaint) {
                super.updateDrawState(drawState)
                drawState.isUnderlineText = true
            }
        }
        spannableString.setSpan(boldSpan, 25, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(foregroundColorSpan, 25, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickableSpan, 25, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvFooter.text = spannableString
        tvFooter.movementMethod = LinkMovementMethod.getInstance()

        auth = Firebase.auth
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.btnRegister -> {
                if (isInputValid()) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("regis", "createUserWithEmail:success")
                                Snackbar.make(btnRegister, "Sign in success", Snackbar.LENGTH_LONG).show()
                                val user = auth.currentUser
//                                updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("regis", "createUserWithEmail:failure", task.exception)
                                Snackbar.make(btnRegister, "Sign in failed", Snackbar.LENGTH_LONG).show()
//                                updateUI(null)
                            }
                        }
                } else {
                    Snackbar.make(btnRegister, "Input is not valid", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun isInputValid() : Boolean {
        val etEmail = findViewById<EditText>(R.id.editEmailRegis)
        val etName = findViewById<EditText>(R.id.editNameRegis)
        val etPassword = findViewById<EditText>(R.id.editPasswordRegis)

        email = etEmail.text.toString()
        name = etName.text.toString()
        password = etPassword.text.toString()

        // TODO: create input validation

        return true
    }
}