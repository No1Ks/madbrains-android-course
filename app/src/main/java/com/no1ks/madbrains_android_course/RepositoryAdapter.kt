package com.no1ks.madbrains_android_course

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class RepositoryAdapter(private val repositories: List<Repository>) : RecyclerView.Adapter<RepositoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val rootView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.repository_view_layout, parent, false)
        return RepositoryViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return repositories.size
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(repositories.get(position))
    }
}

class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val descriptionField: TextView = itemView.findViewById(R.id.repositoryDescriptionId)
    private val nameField: TextView = itemView.findViewById(R.id.repositoryNameId)
    private val authorAvatar: ImageView = itemView.findViewById(R.id.authorAvatarId)
    private val authorName: TextView = itemView.findViewById(R.id.authorNameId)
    private val forksField: TextView = itemView.findViewById(R.id.forksNumberId)
    private val starsField: TextView = itemView.findViewById(R.id.starsNumberId)
    private val languageField: TextView = itemView.findViewById(R.id.languageId)

    fun bind (repository: Repository) {
        descriptionField.text = repository.description
        nameField.text = repository.name
        starsField.text = repository.starsNumber.toString()
        forksField.text = repository.forksNumber.toString()
        authorName.text = repository.owner_login
        languageField.text = repository.language
        Glide.with(itemView).load(repository.owner_avatar_url).apply(RequestOptions.circleCropTransform()).into(authorAvatar)
    }
}