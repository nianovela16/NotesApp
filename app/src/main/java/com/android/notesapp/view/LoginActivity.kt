package com.android.notesapp.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.android.notesapp.R
import com.android.notesapp.util.Constants
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userSp = getSharedPreferences(Constants.SP_USER, Context.MODE_PRIVATE)

        if(userSp.contains(Constants.CRED_NAME))
            startActivity(Intent(this, MainActivity::class.java))

        val usernameLay = findViewById<TextInputLayout>(R.id.login_username_lay)
        val passwordLay = findViewById<TextInputLayout>(R.id.login_password_lay)
        val usernameEt = findViewById<TextInputEditText>(R.id.login_username)
        val passwordEt = findViewById<TextInputEditText>(R.id.login_password)
        val loginBt = findViewById<Button>(R.id.login_action)

        loginBt.setOnClickListener {
            if(usernameEt.text.toString() != Constants.CRED_USERNAME) {
                usernameLay.error = "wrong username"
                usernameLay.isErrorEnabled = true
            } else
                usernameLay.isErrorEnabled = false
            if(passwordEt.text.toString() != Constants.CRED_PASSWORD) {
                passwordLay.error = "wrong password"
                passwordLay.isErrorEnabled = true
            } else
                passwordLay.isErrorEnabled = false

            if(!usernameLay.isErrorEnabled && !passwordLay.isErrorEnabled) {
                userSp.edit()
                    .putString(Constants.CRED_NAME, Constants.CRED_NAME)
                    .putString(Constants.CRED_USERNAME, Constants.CRED_USERNAME)
                    .putString(Constants.CRED_PASSWORD, Constants.CRED_PASSWORD)
                    .apply()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}