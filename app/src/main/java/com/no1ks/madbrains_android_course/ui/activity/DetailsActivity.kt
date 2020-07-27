package com.no1ks.madbrains_android_course.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.no1ks.madbrains_android_course.LoggedUser
import com.no1ks.madbrains_android_course.R
import com.no1ks.madbrains_android_course.RealmInterface
import com.no1ks.madbrains_android_course.RepositoriesLoader
import com.no1ks.madbrains_android_course.entity.Repository
import com.no1ks.madbrains_android_course.ui.adapter.CommitAdapter

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setupActionBar()
        getIncomingIntent()

        val favouriteButton = findViewById<Button>(R.id.buttonSwitchFavouriteId)
        favouriteButton.text =
            if (repository.isFavourite) getString(R.string.remove_from_favourite)
            else getString(R.string.add_to_favourite)

        favouriteButton.setOnClickListener {
            switchFavourite()
        }
    }

    lateinit var repository: Repository
    private var mCommitsAdapter = CommitAdapter(this)

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
        val favouriteButton = findViewById<Button>(R.id.buttonSwitchFavouriteId)
        favouriteButton.text =
            if (repository.isFavourite) getString(R.string.remove_from_favourite)
            else getString(R.string.add_to_favourite)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun show(repository: Repository) {
        val descriptionField: TextView = findViewById(R.id.repositoryDescriptionId)
        val nameField: TextView = findViewById(R.id.repositoryNameId)
        val authorAvatar: ImageView = findViewById(R.id.authorAvatarId)
        val authorName: TextView = findViewById(R.id.authorNameId)
        val forksField: TextView = findViewById(R.id.forksNumberId)
        val starsField: TextView = findViewById(R.id.starsNumberId)
        val languageField: TextView = findViewById(R.id.languageId)
        val urlField: TextView = findViewById(R.id.textViewUrlId)

        descriptionField.text = repository.description
        nameField.text = repository.name
        starsField.text = repository.starsNumber.toString()
        forksField.text = repository.forksNumber.toString()
        authorName.text = repository.owner_login
        languageField.text = repository.language
        Glide.with(this).load(repository.owner_avatar_url).apply(RequestOptions.circleCropTransform()).into(authorAvatar)
        urlField.text = repository.html_url
        urlField.movementMethod = LinkMovementMethod.getInstance()

        mCommitsAdapter.commits = repository.commits.toMutableList()
        val recycler = findViewById<RecyclerView>(R.id.recyclerCommitsId)
        recycler.adapter = mCommitsAdapter
        recycler.layoutManager = LinearLayoutManager(this)
    }
}