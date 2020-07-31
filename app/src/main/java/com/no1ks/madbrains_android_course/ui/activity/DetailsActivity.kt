package com.no1ks.madbrains_android_course.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.no1ks.madbrains_android_course.LoggedUser
import com.no1ks.madbrains_android_course.R
import com.no1ks.madbrains_android_course.RealmInterface
import com.no1ks.madbrains_android_course.RepositoriesLoader
import com.no1ks.madbrains_android_course.entity.Repository
import com.no1ks.madbrains_android_course.ui.adapter.CommitAdapter

class DetailsActivity : AppCompatActivity(),
    RepositoriesLoader.ResponseListener {

    lateinit var repository: Repository

    private var mCommitsAdapter = CommitAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setupActionBar()
        getIncomingIntent()

        RepositoriesLoader.setCustomListener(this)
        val queue = Volley.newRequestQueue(this)
        RepositoriesLoader.loadRepositoryCommitsFromNetwork(queue, repository)

        val favouriteButton = findViewById<Button>(R.id.btn_switch_favourite)
        favouriteButton.setOnClickListener {
            switchFavourite()
        }

        setButtonText()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onResponseReady() {
        if (RepositoriesLoader.numberOfRequestsQueued <= 0) {
            mCommitsAdapter.commits = repository.commits.toMutableList()
            mCommitsAdapter.notifyDataSetChanged()
        }
    }

    override fun onResponseFailed() {
        Toast.makeText(this,
            "Failed to download commits: \n${RepositoriesLoader.queueResult}\n",
            Toast.LENGTH_LONG).show()
    }

    private fun setButtonText() {
        val favouriteButton = findViewById<Button>(R.id.btn_switch_favourite)
        favouriteButton.text =
            if (repository.isFavourite) {
                getString(R.string.remove_from_favourite)
            } else {
                getString(R.string.add_to_favourite)
            }
    }

    private fun getIncomingIntent() {
        if (intent.hasExtra(R.string.repository_full_name.toString())) {
            val fullName = intent.getStringExtra(R.string.repository_full_name.toString())
            val repo = RepositoriesLoader.repositories.find { r -> r.full_name == fullName }
            if (repo != null) {
                repository = repo
                show(repository)
            }
        }
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.back)
        }
    }

    private fun switchFavourite() {
        repository.isFavourite = !repository.isFavourite
        RepositoriesLoader.repositories.find { r -> r.full_name == repository.full_name }
            ?.isFavourite = repository.isFavourite
        if (repository.isFavourite) {
            RealmInterface.addRepositoryToFavourite(LoggedUser.username, repository)
        } else {
            RealmInterface.removeRepositoryFromFavourite(LoggedUser.username, repository)
        }
        RealmInterface.backupToDatabase()
        setButtonText()
    }

    private fun show(repository: Repository) {
        val descriptionField: TextView = findViewById(R.id.tv_repository_description)
        val nameField: TextView = findViewById(R.id.tv_repository_name)
        val authorAvatar: ImageView = findViewById(R.id.img_author_avatar)
        val authorName: TextView = findViewById(R.id.tv_author_name)
        val forksField: TextView = findViewById(R.id.tv_forks_number)
        val starsField: TextView = findViewById(R.id.tv_stars_number)
        val languageField: TextView = findViewById(R.id.tv_language)
        val urlField: TextView = findViewById(R.id.tv_url_field)

        descriptionField.text = repository.description
        nameField.text = repository.name
        starsField.text = repository.starsNumber.toString()
        forksField.text = repository.forksNumber.toString()
        authorName.text = repository.owner_login
        languageField.text = repository.language
        Glide.with(this).load(repository.owner_avatar_url).apply(RequestOptions.circleCropTransform()).into(authorAvatar)
        urlField.text = repository.html_url
        urlField.movementMethod = LinkMovementMethod.getInstance()

        val recycler = findViewById<RecyclerView>(R.id.rclr_commits)
        recycler.adapter = mCommitsAdapter
        recycler.layoutManager = LinearLayoutManager(this)
    }
}