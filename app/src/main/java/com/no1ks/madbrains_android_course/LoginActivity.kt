package com.no1ks.madbrains_android_course

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.buttonEnterId).setOnClickListener {
            if (checkLogin()) {
                LoggedUser.username = editTextNameId.text.toString()
                LoggedUser.password = editTextPasswordId.text.toString()
                openRepositoriesActivity(this)
            } else {
                Toast.makeText(this, "Fill up all fields", Toast.LENGTH_SHORT).show()
            }
        }
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
        val intent: Intent = Intent(context, RepositoriesActivity::class.java)
        context.startActivity(intent)
    }
}