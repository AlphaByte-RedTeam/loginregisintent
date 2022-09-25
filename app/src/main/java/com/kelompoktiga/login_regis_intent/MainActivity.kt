package com.kelompoktiga.login_regis_intent

import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes

class MainActivity : AppCompatActivity() {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private val REQ_ONE_TAP = 2 // can be any integer unique to the activity
    private var showOneTapUI = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContentView(R.layout.activity_main)

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


        val continueButton: Button = findViewById(R.id.btnContinue)
        continueButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
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

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}