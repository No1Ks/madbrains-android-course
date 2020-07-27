package com.no1ks.madbrains_android_course.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.no1ks.madbrains_android_course.R
import com.no1ks.madbrains_android_course.entity.Repository
import com.no1ks.madbrains_android_course.ui.activity.DetailsActivity

class RepositoryAdapter(val context: Context)
    : RecyclerView.Adapter<RepositoryViewHolder>()
    , ItemTouchHelperAdapter {

    var repositories: MutableList<Repository> = mutableListOf()

    interface RepositoryListChangesListener {
        fun onRepositoryFavourited(repo: Repository)
        fun onRepositoryUnfavourited(repo: Repository)
    }
    private var mListener: RepositoryListChangesListener? = null

    fun setCustomListener(listener: RepositoryListChangesListener?) {
        mListener = listener
    }

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
        holder.bind(repositories[position])
        holder.parentLayout.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            val repositoryFullName = repositories[position].full_name
            intent.putExtra(R.string.repository_full_name.toString(), repositoryFullName)
            context.startActivity(intent)
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {}

    override fun onItemSwipedRight(position: Int) {
        mListener?.onRepositoryFavourited(repositories[position])
    }

    override fun onItemSwipedLeft(position: Int) {
        mListener?.onRepositoryUnfavourited(repositories[position])
    }
}

class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val parentLayout: ConstraintLayout = itemView.findViewById(R.id.parentLayoutId)

    private val descriptionField: TextView = itemView.findViewById(R.id.repositoryDescriptionId)
    private val nameField: TextView = itemView.findViewById(R.id.repositoryNameId)
    private val authorAvatar: ImageView = itemView.findViewById(R.id.authorAvatarId)
    private val authorName: TextView = itemView.findViewById(R.id.authorNameId)
    private val forksField: TextView = itemView.findViewById(R.id.forksNumberId)
    private val starsField: TextView = itemView.findViewById(R.id.starsNumberId)
    private val languageField: TextView = itemView.findViewById(R.id.languageId)

    fun bind(repository: Repository) {
        descriptionField.text = repository.description
        nameField.text = repository.name
        starsField.text = repository.starsNumber.toString()
        forksField.text = repository.forksNumber.toString()
        authorName.text = repository.owner_login
        languageField.text = repository.language
        Glide.with(itemView).load(repository.owner_avatar_url).apply(RequestOptions.circleCropTransform()).into(authorAvatar)
    }
}

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int)
    fun onItemSwipedRight(position: Int)
    fun onItemSwipedLeft(position:Int)
}

class SimpleItemTouchHelperCallback(private val mAdapter: ItemTouchHelperAdapter, private val swipesRight: Boolean) :
    ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder
    ): Int {
        return if (swipesRight) makeMovementFlags(0, ItemTouchHelper.RIGHT)
        else makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: ViewHolder,
        target: ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        return if (swipesRight) mAdapter.onItemSwipedRight(viewHolder.adapterPosition)
        else mAdapter.onItemSwipedLeft(viewHolder.adapterPosition)
    }
}