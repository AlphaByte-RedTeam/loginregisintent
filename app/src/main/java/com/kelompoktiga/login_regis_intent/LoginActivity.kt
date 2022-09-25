package com.kelompoktiga.login_regis_intent

import android.content.Intent
import android.content.IntentSender
import android.graphics.Typeface
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
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
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

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var oneTapClient: SignInClient

    private val REQ_ONE_TAP = 2 // can be any integer unique to the activity
    private var showOneTapUI = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        val tvFooter: TextView = findViewById(R.id.txtFooterLogin)

        etEmail = findViewById(R.id.editEmailLogin)
        etPassword = findViewById(R.id.editPasswordLogin)

        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .setAutoSelectEnabled(true)
            .build()

        oneTapClient = Identity.getSignInClient(this)
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender,
                        REQ_ONE_TAP,
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow,
                // or do nothing and continue presenting the signed-out UI

                // See: https://developers.google.com/identity/one-tap/android/create-new-accounts?authuser=0
                // for further details on how to implement One Tap sign-up.
                // val inten: Intent = Intent(this, RegisterActivity::class.java)
                Log.e("OneTap", "Error getting pending intent", e)
            }

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

        btnGoogleLogin = findViewById(R.id.btnGoogleLogin)
        btnGoogleLogin.setOnClickListener() {
            signIn()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            @Suppress("DEPRECATION")
            super.onActivityResult(requestCode, resultCode, data)

            when (requestCode) {
                REQ_ONE_TAP -> {
                    try {
                        val credential = oneTapClient.getSignInCredentialFromIntent(data)
                        val idToken = credential.googleIdToken
                        val username = credential.id
                        val password = credential.password
                        when {
                            idToken != null -> {
                                // Got an ID token from Google. Use it to authenticate with Firebase
                                Log.d(TAG, "Got ID token")
                            }
                            password != null -> {
                                // Got a saved username and password. Use them to authenticate with Firebase
                                Log.d(TAG, "Got password")
                            }
                            else -> {
                                // Shouldn't happen
                                Log.d(TAG, "No ID token!")
                            }
                        }
                    } catch (e: ApiException) {
                        when (e.statusCode) {
                            CommonStatusCodes.CANCELED -> {
                                Log.d(TAG, "One-tap dialog was closed.")
                                // Don't re-prompt the user
                                showOneTapUI = false
                            }
                            CommonStatusCodes.NETWORK_ERROR -> {
                                Log.d(TAG, "One-tap encountered a network error.")
                                // Try again or just ignore
                            }
                            else -> {
                                Log.d(TAG, "Couldn't get credential from result: ${e.localizedMessage}")
                            }
                        }
                    }
                }
            }
        }

    private fun signIn() {
        // TODO("Implement sign in method in android studio kotlin using Firebase auth")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
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

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}