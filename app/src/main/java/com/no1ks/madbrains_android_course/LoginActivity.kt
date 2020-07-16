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
                // TODO save credentials for further login

                openRepositoriesActivity(this)
            }
        }
    }

    private fun checkLogin(): Boolean {
        // TODO check here
        val name: String = editTextNameId.text.toString()
        val pass: String = editTextPasswordId.text.toString()
//        if (name.isNotEmpty() && pass.isNotEmpty()) {
//            // TODO try to login
//            // TODO on success save this as current user credentials
//            return true
//        } else {
//            Toast.makeText(this, "Fill up credentials first", Toast.LENGTH_SHORT).show()
//        }
//        return false
        return true
    }

    private fun openRepositoriesActivity(context: Context) {
        val intent: Intent = Intent(context, RepositoriesActivity::class.java)
        context.startActivity(intent)
    }
}