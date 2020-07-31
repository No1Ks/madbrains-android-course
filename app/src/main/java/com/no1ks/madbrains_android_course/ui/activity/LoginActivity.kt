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

    private val PREF_LOGIN = "PREF_LOGIN"
    private val PREF_PASSW = "PREF_PASSW"

    private lateinit var mLastLogin: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.btn_enter).setOnClickListener {
            if (checkLogin()) {
                LoggedUser.username = et_name.text.toString()
                LoggedUser.password = et_password.text.toString()
                val checkBoxRememberMe = findViewById<CheckBox>(R.id.cb_remember)
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

    private fun saveLastLogin() {
        mLastLogin = getPreferences(Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = mLastLogin.edit()
        editor.putString(PREF_LOGIN, LoggedUser.username)
        editor.putString(PREF_PASSW, LoggedUser.password)
        editor.apply()
    }

    private fun clearLastLogin() {
        et_name.setText("")
        et_password.setText("")
        mLastLogin = getPreferences(Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = mLastLogin.edit()
        editor.putString(PREF_LOGIN, "")
        editor.putString(PREF_PASSW, "")
        editor.apply()
    }

    private fun loadLastLogin() {
        mLastLogin = getPreferences(Context.MODE_PRIVATE)
        et_name.setText(mLastLogin.getString(PREF_LOGIN, ""))
        et_password.setText(mLastLogin.getString(PREF_PASSW, ""))
    }

    private fun checkLogin(): Boolean {
        val name: String = et_name.text.toString()
        val pass: String = et_password.text.toString()
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