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
import android.view.View
import android.widget.TextView

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        
        val tvFooter: TextView = findViewById(R.id.txtFooterRegis)

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
    }
}