package com.no1ks.madbrains_android_course.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.no1ks.madbrains_android_course.R
import com.no1ks.madbrains_android_course.RepositoriesLoader
import com.no1ks.madbrains_android_course.Repository

class RepositoriesActivity : AppCompatActivity(),
    RepositoriesLoader.ResponseListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)

        val sectionsPagerAdapter = SectionsPagerAdapter(
            this,
            supportFragmentManager
        )
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        setupActionBar()

        val queue = Volley.newRequestQueue(this)
        RepositoriesLoader.setCustomListener(this)
        startLoadingScreen()
        RepositoriesLoader.loadRepositoriesFromNetwork(queue)
    }

    private fun startLoadingScreen() {
        val intent: Intent = Intent(this, LoadingActivity::class.java)
        this.startActivity(intent)
    }

    private fun finishLoadingScreen() {
        sendBroadcast(Intent("closeLoadingScreen"))
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = "Logout"
        }
    }

    fun setList(repositories: List<Repository>) {
        val adapter =
            RepositoryAdapter(repositories)
        val recycler = findViewById<RecyclerView>(R.id.recyclerRepositoriesId)
        recycler.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        recycler.layoutManager = layoutManager
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    // interface methods implementation
    override fun onResponseReady() {
        if (RepositoriesLoader.numberOfRequestQueued <= 0) {
            setList(RepositoriesLoader.repositories)
            finishLoadingScreen()
        }
    }

    override fun onResponseFailed() {
        Toast.makeText(this,
            "Failed to download repositories: \n${RepositoriesLoader.queueResult}",
            Toast.LENGTH_SHORT).show()
        finishLoadingScreen()
        finish()
    }
}