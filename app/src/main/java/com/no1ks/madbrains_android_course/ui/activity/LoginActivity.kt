package com.no1ks.madbrains_android_course.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.no1ks.madbrains_android_course.LoggedUser
import com.no1ks.madbrains_android_course.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var lastLogin: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.buttonEnterId).setOnClickListener {
            if (checkLogin()) {
                LoggedUser.username = editTextNameId.text.toString()
                LoggedUser.password = editTextPasswordId.text.toString()
                val checkBoxRememberMe = findViewById<CheckBox>(R.id.checkBoxRememberId)
                if (checkBoxRememberMe.isChecked) {
                    saveLastLogin()
                } else {
                    clearLastLogin()
                }
                openRepositoriesActivity(this)
            } else {
                Toast.makeText(this, "Fill up all fields", Toast.LENGTH_SHORT).show()
            }
        }
        loadLastLogin()
    }

    private val P_LOGIN = "P_LOGIN"
    private val P_PASSW = "P_PASSW"

    private fun saveLastLogin() {
        lastLogin = getPreferences(Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = lastLogin.edit()
        editor.putString(P_LOGIN, LoggedUser.username)
        editor.putString(P_PASSW, LoggedUser.password)
        editor.commit()
    }

    private fun clearLastLogin() {
        editTextNameId.setText("")
        editTextPasswordId.setText("")
        lastLogin = getPreferences(Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = lastLogin.edit()
        editor.putString(P_LOGIN, "")
        editor.putString(P_PASSW, "")
        editor.commit()
    }

    private fun loadLastLogin() {
        lastLogin = getPreferences(Context.MODE_PRIVATE)
        editTextNameId.setText(lastLogin.getString(P_LOGIN, ""))
        editTextPasswordId.setText(lastLogin.getString(P_PASSW, ""))
    }

    private fun checkLogin(): Boolean {
        val name: String = editTextNameId.text.toString()
        val pass: String = editTextPasswordId.text.toString()
        if (name.isNotEmpty() && pass.isNotEmpty()) {
            // in a perfect world I should check whether credentials are correct. but not today
            return true
        }
        return false
    }

    private fun openRepositoriesActivity(context: Context) {
        val intent = Intent(context, RepositoriesActivity::class.java)
        context.startActivity(intent)
    }
}