package com.no1ks.madbrains_android_course

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import com.no1ks.madbrains_android_course.ui.main.SectionsPagerAdapter

class RepositoriesActivity : AppCompatActivity(), RepositoriesLoader.ResponseListener {
    private val repositoriesLoader = RepositoriesLoader()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repositories)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        setupActionBar()

        val queue = Volley.newRequestQueue(this)
        repositoriesLoader.setCustomListener(this)
        repositoriesLoader.loadRepositoriesFromNetwork(queue)
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
        setList(repositoriesLoader.repositories)
    }

    override fun onResponseFailed() {
        Toast.makeText(this,
            "Failed to download repositories: \n${repositoriesLoader.queueResult}",
            Toast.LENGTH_SHORT).show()
    }
}