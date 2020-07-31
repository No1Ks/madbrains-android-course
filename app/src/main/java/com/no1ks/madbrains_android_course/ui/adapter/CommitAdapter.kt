package com.no1ks.madbrains_android_course.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.no1ks.madbrains_android_course.R
import com.no1ks.madbrains_android_course.entity.Commit

class CommitAdapter(val context: Context)
    : RecyclerView.Adapter<CommitViewHolder>() {

    var commits: MutableList<Commit> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewHolder {
        val rootView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.commit_view_layout, parent, false)
        return CommitViewHolder(rootView)
    }

    override fun getItemCount() = commits.size

    override fun onBindViewHolder(holder: CommitViewHolder, position: Int) {
        holder.bind(commits[position])
    }
}

class CommitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val dateField: TextView = itemView.findViewById(R.id.tv_commit_date)
    private val messageField: TextView = itemView.findViewById(R.id.tv_commit_message)
    private val authorAvatar: ImageView = itemView.findViewById(R.id.img_author_avatar)
    private val authorName: TextView = itemView.findViewById(R.id.tv_author_name)

    fun bind(commit: Commit) {
        dateField.text = commit.date
        messageField.text = commit.message
        authorName.text = commit.authorName
        Glide.with(itemView).load(commit.authorAvatarUrl).apply(RequestOptions.circleCropTransform()).into(authorAvatar)
    }
}
