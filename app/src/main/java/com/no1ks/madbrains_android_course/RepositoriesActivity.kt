package com.no1ks.madbrains_android_course

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_repositories.*

class RepositoriesActivity : AppCompatActivity(), RepositoriesLoader.ResponseListener {
    private val repositoriesLoader = RepositoriesLoader()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)
        setupActionBar()

        val queue = Volley.newRequestQueue(this)
        repositoriesLoader.setCustomListener(this)
        repositoriesLoader.loadRepositoriesFromNetwork(queue)
        // From this point Activity will wait for response.
        // When callback comes onResponseReady() fires or onResponseFailed()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = "Logout"
        }
    }

    fun setList(repositories: List<Repository>) {
        val adapter = RepositoryAdapter(repositories)
        recyclerRepositoriesId.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        recyclerRepositoriesId.layoutManager = layoutManager
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    // interface methods implementation
    override fun onResponseReady() {
        Toast.makeText(this, repositoriesLoader.queueResult, Toast.LENGTH_SHORT).show()
        // TODO show repositories
        setList(repositoriesLoader.repositories)
    }

    override fun onResponseFailed() {
        Toast.makeText(this,
            "Failed to download repositories: \n"
                    + repositoriesLoader.queueResult, Toast.LENGTH_SHORT).show()
    }
}